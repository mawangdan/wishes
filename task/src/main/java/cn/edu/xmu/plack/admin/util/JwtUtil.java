package cn.edu.xmu.plack.admin.util;

import cn.edu.xmu.plack.admin.model.po.Admin;
import cn.edu.xmu.plack.core.util.JwtHelper;

public class JwtUtil {
    private static JwtHelper jwtHelper = new JwtHelper();
    private static final int expireTime = 3 * 24 * 60 * 60;

    public static String creatToken(Admin admin) {
        return jwtHelper.createToken(admin.getId(), admin.getUserName(), 0L, 0, expireTime);
    }
}
