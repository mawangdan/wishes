/**
 * Copyright School of Informatics Xiamen University
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package cn.edu.xmu.wishes.gateway.localfiter;

import cn.edu.xmu.wishes.core.util.InternalReturnObject;
import cn.edu.xmu.wishes.core.util.JwtHelper;
import cn.edu.xmu.wishes.core.util.ReturnNo;
import cn.edu.xmu.wishes.gateway.microservice.PrivilegeService;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ming Qiu
 * @date Created in 2020/11/13 22:31
 * modifiedBy Ming Qiu 2021/12/3 12:20
 **/
public class AuthFilter implements GatewayFilter, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);
    private static final String LOGMEG = "%s: $s";

    private String USERKEY="CUS_%d";
    private static final String PRIVKEY = "%s-%d";

    private static final String RETURN = "{\"errno\": %d, \"errmsg\": \"%s\"}";


    private String tokenName;

    private PrivilegeService privilegeService;

    private RedisTemplate<String, Serializable> redisTemplate;

    private Integer jwtExpireTime = 3600;

    private Integer refreshJwtTime = 60;

    private WebClient webClient;

    private Integer maxTimes;

    public AuthFilter(Config config) {
        this.tokenName = config.getTokenName();
    }

    public void setPrivilegeService(PrivilegeService privilegeService) {
        this.privilegeService = privilegeService;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setJwtExpireTime(Integer jwtExpireTime) {
        this.jwtExpireTime = jwtExpireTime;
    }

    public void setRefreshJwtTime(Integer refreshJwtTime) {
        this.refreshJwtTime = refreshJwtTime;
    }

    /**
     * gateway001 权限过滤器
     * 1. 检查JWT是否合法,以及是否过期，如果过期则需要在response的头里换发新JWT，如果不过期将旧的JWT在response的头中返回
     * 2. 判断用户的shopid是否与路径上的shopid一致（0可以不做这一检查）
     * 3. 在redis中判断用户是否有权限访问url,如果不在redis中需要通过dubbo接口load用户权限
     * 4. 需要以微服务接口访问privilegeservice
     *
     * @param exchange
     * @param chain
     * @return
     * @author wwc
     * @date 2020/12/02 17:13
     */
    /**
     * 将判断token是否被ban的逻辑用lua脚本重写
     * @author Jianjian Chan
     * @date 2021/12/03
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();;
        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory factory = response.bufferFactory();
        logger.info(response.toString());
        // 获取请求参数
        String token = request.getHeaders().getFirst(tokenName);
        RequestPath url = request.getPath();
        logger.info(url.value());
        HttpMethod method = request.getMethod();
        // 判断token是否为空，无需token的url在配置文件中设置
        logger.debug("filter: token = " + token);
        if (StringUtil.isNullOrEmpty(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().set("Content-Type", "application/json;charset=UTF-8");
            String ret = String.format(RETURN, ReturnNo.AUTH_NEED_LOGIN.getCode(), ReturnNo.AUTH_NEED_LOGIN.getMessage());
            byte[] retByte = ret.getBytes(StandardCharsets.UTF_8);
            return response.writeWith(Mono.just(factory.wrap(retByte)));
        }
        // 判断token是否合法
        JwtHelper.UserAndDepart userAndDepart = new JwtHelper().verifyTokenAndGetClaims(token);
        if (userAndDepart == null) {
            // 若token解析不合法
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().set("Content-Type", "application/json;charset=UTF-8");
            String ret = String.format(RETURN, ReturnNo.AUTH_INVALID_JWT.getCode(), ReturnNo.AUTH_INVALID_JWT.getMessage());
            byte[] retByte = ret.getBytes(StandardCharsets.UTF_8);
            return response.writeWith(Mono.just(factory.wrap(retByte)));
        } else {
            // 若token合法
            // 判断该token是否被ban
            String[] banSetNames = {"BanJwt_0", "BanJwt_1"};
            String scriptPath = "scripts/check-jwt.lua";

            DefaultRedisScript<Boolean> script = new DefaultRedisScript<>();

            script.setScriptSource(new ResourceScriptSource(new ClassPathResource(scriptPath)));
            script.setResultType(Boolean.class);

            List<String> keyList = Arrays.asList(banSetNames);

            Boolean baned = redisTemplate.execute(script, keyList, token);

            if(baned) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                response.getHeaders().set("Content-Type", "application/json;charset=UTF-8");
                String ret = String.format(RETURN, ReturnNo.CUSTOMER_FORBIDDEN.getCode(), ReturnNo.CUSTOMER_FORBIDDEN.getMessage());
                byte[] retByte = ret.getBytes(StandardCharsets.UTF_8);
                return response.writeWith(Mono.just(factory.wrap(retByte)));
            }

            // 检测完了则该token有效
            // 解析userid和departid和有效期
            Long userId = userAndDepart.getUserId();
            Long departId = userAndDepart.getDepartId();
            String userName = userAndDepart.getUserName();
            Date expireTime = userAndDepart.getExpTime();
            Integer userLevel = userAndDepart.getUserLevel();
            // 检验api中传入token是否和departId一致
            if (url != null) {
                // 获取路径中的shopId
                Map<String, String> uriVariables = exchange.getAttribute(ServerWebExchangeUtils.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                String pathId = uriVariables.get("did");
                if (pathId != null && !departId.equals(0L)) {
                    // 若非空且解析出的部门id非0则检查是否匹配
                    if (!pathId.equals(departId.toString())) {
                        // 若id不匹配
                        logger.debug(String.format(LOGMEG,"filter","did不匹配:" + pathId));
                        response.setStatusCode(HttpStatus.FORBIDDEN);
                        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                        String ret = String.format(RETURN, ReturnNo.RESOURCE_ID_OUTSCOPE.getCode(), ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage());
                        byte[] retByte = ret.getBytes(StandardCharsets.UTF_8);
                        return response.writeWith(Mono.just(factory.wrap(retByte)));
                    }
                }
                logger.debug(String.format(LOGMEG,"filter","did匹配"));
            } else {
                logger.debug(String.format(LOGMEG,"filter","请求url为空"));
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                String ret = String.format(RETURN, ReturnNo.FIELD_NOTVALID.getCode(), ReturnNo.FIELD_NOTVALID.getMessage());
                byte[] retByte = ret.getBytes(StandardCharsets.UTF_8);
                return response.writeWith(Mono.just(factory.wrap(retByte)));
            }

            String jwt = token;
            // 判断redis中是否存在该用户的token，若不存在则重新load用户的权限
            String key = String.format(USERKEY, userId);
            int time = 0;
            String json = String.format("{\"token\": \"%s\"}",token);
            while (!redisTemplate.hasKey(key) && time < this.maxTimes) {
                // 如果redis中没有该键值
                // 通过内部调用将权限载入redis并返回新的token
//                Mono<InternalReturnObject> mono = webClient.put().uri(LOADUSER,userId)
//                        .header(tokenName,token).contentType(MediaType.APPLICATION_JSON)
//                        .bodyValue(json)
//                        .retrieve().bodyToMono(InternalReturnObject.class);
//                mono.subscribe(retObj ->{
//                    if (!retObj.getErrno().equals(ReturnNo.OK.getCode())){
//                        logger.error(String.format(LOGMEG,"filter","load user errno ="+retObj.getErrno()+" errmsg = "+retObj.getErrmsg()));
//                    }
//                }, e ->{
//                    logger.error(String.format(LOGMEG,"filter",e.getMessage()));
//                });
//                try {
//                    if (time > 0) {
//                        //网关跑太快了 权限为load到redis
//                        logger.info(String.format(LOGMEG,"filter","第"+(time+1)+"次load userId ="+key));
//                        Thread.sleep(WAITING_TIME);
//                    }
//                } catch (InterruptedException e) {
//                }
                time++;
            }

            response.getHeaders().set(tokenName, jwt);
            // 将url中的数字替换成{id}
            Pattern p = Pattern.compile("/(0|[1-9][0-9]*)");
            Matcher matcher = p.matcher(url.toString());
            String commonUrl = matcher.replaceAll("/{id}");
            logger.debug("获取通用请求路径:" + commonUrl);
            // 找到该url所需要的权限id
            return chain.filter(exchange).then(
                    Mono.fromRunnable(() -> {
                                // 判断该token有效期是否还长，load用户权限需要传token，将要过期的旧的token暂未放入banjwt中，有重复登录的问题
                                Long sec = expireTime.getTime() - System.currentTimeMillis();
                                if (sec < refreshJwtTime * 1000) {
                                    // 若快要过期了则重新换发token 创建新的token
                                    JwtHelper jwtHelper = new JwtHelper();
                                    String newJwt = jwtHelper.createToken(userId, userName, departId, userLevel, jwtExpireTime);
                                    logger.debug("重新换发token:" + jwt);
                                    response.getHeaders().set(tokenName, newJwt);
                                }
                            }
                    )
            );
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    public static class Config {
        private String tokenName;

        public Config() {

        }

        public String getTokenName() {
            return tokenName;
        }

        public void setTokenName(String tokenName) {
            this.tokenName = tokenName;
        }
    }

    /**
     * 请求类型
     */
    public enum RequestType {
        GET(0, "GET"),
        POST(1, "POST"),
        PUT(2, "PUT"),
        DELETE(3, "DELETE");

        private static final Map<Integer, RequestType> typeMap;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            typeMap = new HashMap();
            for (RequestType enum1 : values()) {
                typeMap.put(enum1.code, enum1);
            }
        }

        private int code;
        private String description;

        RequestType(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static RequestType getTypeByCode(Integer code) {
            return typeMap.get(code);
        }

        public static RequestType getCodeByType(HttpMethod method) {
            switch (method) {
                case GET: return RequestType.GET;
                case PUT: return RequestType.PUT;
                case POST: return RequestType.POST;
                case DELETE: return RequestType.DELETE;
                default: return null;
            }
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

    }
}
