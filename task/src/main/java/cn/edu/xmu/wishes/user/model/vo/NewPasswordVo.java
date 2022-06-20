package cn.edu.xmu.wishes.user.model.vo;

import lombok.Data;

@Data
public class NewPasswordVo {
    private String oldPassword;
    private String newPassword;
}
