package cn.edu.xmu.wishes.user.security;

import cn.edu.xmu.wishes.core.util.ResponseUtil;
import cn.edu.xmu.wishes.core.util.ReturnNo;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType("text/javascript;charset=utf-8");
//        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.getWriter().print(ResponseUtil.fail(ReturnNo.AUTH_NEED_LOGIN));
    }
}
