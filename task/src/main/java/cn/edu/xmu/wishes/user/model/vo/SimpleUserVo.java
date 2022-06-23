package cn.edu.xmu.wishes.user.model.vo;

import lombok.Data;

@Data
public class SimpleUserVo {
    private String password;

    private String sign;

    private String address;

    private String preferTaskType;
}
