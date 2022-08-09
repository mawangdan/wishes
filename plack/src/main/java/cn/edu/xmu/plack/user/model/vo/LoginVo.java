package cn.edu.xmu.plack.user.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginVo {
    @NotBlank
    private String userName;

    @NotBlank
    private String password;
}
