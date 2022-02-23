package cn.edu.xmu.wishes.gateway.util;

import cn.edu.xmu.wishes.core.util.JacksonUtil;
import cn.edu.xmu.wishes.core.util.ResponseUtil;
import cn.edu.xmu.wishes.core.util.ReturnNo;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;


public class ResponseUtils {

    public static Mono<Void> writeErrorInfo(ServerHttpResponse response, ReturnNo returnNo) {
        switch (returnNo) {
            case AUTH_INVALID_JWT:
            case AUTH_JWT_EXPIRED:
            case AUTH_NEED_LOGIN:
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                break;
            case AUTH_NO_RIGHT:
                response.setStatusCode(HttpStatus.FORBIDDEN);
                break;
            default:
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                break;
        }
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getHeaders().set("Access-Control-Allow-Origin", "*");
        response.getHeaders().set("Cache-Control", "no-cache");
        String body = JacksonUtil.toJson(ResponseUtil.fail(returnNo));
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer))
                .doOnError(error -> DataBufferUtils.release(buffer));
    }

}
