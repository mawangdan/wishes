package cn.edu.xmu.wishes.core.mybatisplus;

import cn.edu.xmu.wishes.core.util.JwtHelper;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class FillHandler implements MetaObjectHandler {
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    class FillInfo {
        Long userId;
        String userName;
    }
    private FillInfo getFillInfo() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String token = request.getHeader(JwtHelper.LOGIN_TOKEN_KEY);
        if (token == null){
            return new FillInfo();
        }
        JwtHelper.UserAndDepart userAndDepart = new JwtHelper().verifyTokenAndGetClaims(token);
        Long userId;
        String userName;
        if (null != userAndDepart){
            userId = userAndDepart.getUserId();
            userName=userAndDepart.getUserName();
            return new FillInfo(userId, userName);
        }
        return new FillInfo();
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        FillInfo fillInfo = getFillInfo();
        log.info("start insert fill ....");
        this.strictInsertFill(metaObject, "creatorId", () -> fillInfo.getUserId(), Long.class);
        this.strictInsertFill(metaObject, "creatorName", () -> fillInfo.getUserName(), String.class);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        FillInfo fillInfo = getFillInfo();
        log.info("start update fill ....");
        this.strictUpdateFill(metaObject, "modifierId", () -> fillInfo.getUserId(), Long.class);
        this.strictUpdateFill(metaObject, "modifierName", () -> fillInfo.getUserName(), String.class);
    }
}
