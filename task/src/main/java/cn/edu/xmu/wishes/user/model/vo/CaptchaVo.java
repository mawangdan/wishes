package cn.edu.xmu.wishes.user.model.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CaptchaVo {
    @NotBlank
    String captcha;

    @NotBlank
    String email;
}
