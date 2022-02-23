package cn.edu.xmu.wishes.user.model.vo;

import lombok.Data;

@Data
public class SimpleUserVo {
    private static final long serialVersionUID = 4021571921740200108L;

    private String password;

    private String sign;

    private String address;
}
