package cn.edu.xmu.wishes.user.model;

import java.security.Principal;

public class WebsocketUserVo implements Principal {
    private  String id;
    public WebsocketUserVo(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return id;
    }
}
