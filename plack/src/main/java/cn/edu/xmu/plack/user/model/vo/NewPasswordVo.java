package cn.edu.xmu.plack.user.model.vo;

import lombok.Data;

@Data
public class NewPasswordVo {
    private String username;
    private String oldpassword;
    private String newpassword;
}
