package cn.edu.xmu.wishes.user.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
    @NotBlank(message = "用户名不能为空")
    private String userName;
    @NotBlank(message = "密码不能为空")
    private String password;

    private String sign;

    private String address;

    @NotBlank(message = "手机号不能为空")
    private String mobile;

//    @NotBlank(message = "邮箱不能为空")
    private String email;

//    @NotBlank(message = "真实姓名不能为空")
//    private String name;
}
