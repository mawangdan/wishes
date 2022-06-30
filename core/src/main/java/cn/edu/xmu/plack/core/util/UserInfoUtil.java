package cn.edu.xmu.plack.core.util;

import cn.edu.xmu.plack.core.constants.SecurityConstants;
import cn.edu.xmu.plack.core.exception.UnAuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Slf4j
public class UserInfoUtil {
    public static Long getUserId() {
        try {
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String payload = httpServletRequest.getHeader(SecurityConstants.JWT_PAYLOAD_KEY);

            String decodePayload = URLDecoder.decode(payload, "UTF-8");
            Long userId = JacksonUtil.parseObject(decodePayload, "userId", Long.class);
            return userId;
        } catch (UnsupportedEncodingException | NullPointerException e) {
            log.error("UserInfoUtil getUserId:" + e.getMessage());
            throw new UnAuthException();
        }
    }
}
