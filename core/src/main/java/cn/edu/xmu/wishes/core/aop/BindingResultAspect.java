package cn.edu.xmu.wishes.core.aop;

import cn.edu.xmu.wishes.core.util.Common;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

@Aspect
@Component
public class BindingResultAspect {
//    @Pointcut("execution(public * cn.edu.xmu.wishes.*..*Controller(..)) && args(.., bindingResult)")
//    public void bindingResultAspect() {
//    }
    //Todo 未测试
    @Around("execution(public * cn.edu.xmu.wishes.*..*Controller(..)) && args(.., bindingResult)")
    public Object around(ProceedingJoinPoint joinPoint, BindingResult bindingResult) throws Throwable {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        Object o= Common.processFieldErrors(bindingResult, response);
        if(o!=null) {
            return o;
        }
        else {
            joinPoint.proceed();
            return null;
        }
    }
}
