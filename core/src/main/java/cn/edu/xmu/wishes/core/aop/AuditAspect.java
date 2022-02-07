package cn.edu.xmu.wishes.core.aop;

import cn.edu.xmu.privilegegateway.annotation.util.JwtHelper;
import cn.edu.xmu.privilegegateway.annotation.util.ResponseUtil;
import cn.edu.xmu.privilegegateway.annotation.util.ReturnNo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @auther mingqiu
 * @date 2020/6/26 下午2:16
 *      modifiedBy Ming Qiu 2020/11/3 22:59
 *
 */
@Aspect
@Component
public class AuditAspect {

    //注入Service用于把日志保存数据库

    private  static  final Logger logger = LoggerFactory.getLogger(AuditAspect. class);
    private static final String LOG = "%s: %s";

    //Controller层切点
    @Pointcut("@annotation(cn.edu.xmu.privilegegateway.annotation.aop.Audit)")
    public void auditAspect() {
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @Before("auditAspect()")
    public void doBefore(JoinPoint joinPoint) {
    }

    //配置controller环绕通知,使用在方法aspect()上注册的切入点
    @Around("auditAspect()")
    public Object around(JoinPoint joinPoint){
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        String token = request.getHeader(JwtHelper.LOGIN_TOKEN_KEY);
        if (token == null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ResponseUtil.fail(ReturnNo.AUTH_NEED_LOGIN);
        }

        JwtHelper.UserAndDepart userAndDepart = new JwtHelper().verifyTokenAndGetClaims(token);
        Long userId = null;
        Long departId = null;
        String userName=null;
        Integer userLevel=null;
        if (null != userAndDepart){
            userId = userAndDepart.getUserId();
            departId = userAndDepart.getDepartId();
            userName=userAndDepart.getUserName();
            userLevel=userAndDepart.getUserLevel();
        }

        //检验/shop的api中传入token是否和departId一致
        String pathInfo = userAndDepart == null ? null : request.getRequestURI();
        String departName=null;
        Audit AuditAnnotation = method.getAnnotation(Audit.class);
        if(AuditAnnotation!=null){
            departName = ((Audit) AuditAnnotation).departName();
        }

        boolean flag=false;
        if(null!=pathInfo) {
            if(!"".equals(departName)) {
                logger.debug(String.format(LOG,"around","getPathInfo = " + pathInfo));
                String paths[] = pathInfo.split("/");
                for (int i = 0; i < paths.length; i++) {
                    //如果departId为0,可以操作所有的depart
                    if (departId == 0) {
                        flag = true;
                        break;
                    }
                    if (paths[i].equals(departName)) {
                        if (i + 1 < paths.length) {
                            //找到路径上对应id 将其与string类型的departId比较
                            String pathId = paths[i + 1];
                            logger.debug(String.format(LOG,"around","did =" + pathId));
                            if (!pathId.equals(departId.toString())) {
                                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                logger.info(String.format(LOG,"around", "不匹配departId = "+departId));
                                return ResponseUtil.fail(ReturnNo.RESOURCE_ID_OUTSCOPE);
                            } else {
                                flag = true;
                                logger.debug(String.format(LOG,"around","success match Id!"));
                            }
                        }
                        else {
                            flag = true;//这是没有departId的情况
                            break;
                        }
                    }
                }
                if (flag == false) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    logger.info(String.format(LOG,"around", "不匹配departId = "+departId));
                    return ResponseUtil.fail(ReturnNo.RESOURCE_ID_OUTSCOPE);
                }
            }
            else {
                departId=null;
            }
        }
        else{
            logger.error(String.format(LOG,"around","the api path is null"));
        }

        logger.debug("around: userId ="+userId+" departId="+departId);
        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            logger.info(String.format(LOG,"around", "userId is null"));
            return ResponseUtil.fail(ReturnNo.AUTH_NEED_LOGIN);
        }

        Object[] args = joinPoint.getArgs();
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            Object param = args[i];
            Annotation[] paramAnn = annotations[i];
            if (paramAnn.length == 0){
                continue;
            }

            for (Annotation annotation : paramAnn) {
                //这里判断当前注解是否为LoginUser.class
                if (annotation.annotationType().equals(LoginUser.class)) {
                    //校验该参数，验证一次退出该注解
                    args[i] = userId;
                }
                if (annotation.annotationType().equals(Depart.class)) {
                    //校验该参数，验证一次退出该注解
                    args[i] = departId;
                }
                if (annotation.annotationType().equals(LoginName.class)) {
                    //校验该参数，验证一次退出该注解
                    args[i] = userName;
                }
                if (annotation.annotationType().equals(UserLevel.class)) {
                    //校验该参数，验证一次退出该注解
                    args[i] = userLevel;
                }
            }
        }

        Object obj = null;
        try {
            obj = ((ProceedingJoinPoint) joinPoint).proceed(args);
        } catch (Throwable e) {

        }
        return obj;
    }
}
