package cn.edu.xmu.wishes.user.config;

import cn.edu.xmu.wishes.user.service.AuthPrivilegeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InitPermissionRolesCache implements CommandLineRunner {

    @Autowired
    private AuthPrivilegeService authPrivilegeService;

    @Override
    public void run(String... args) {
        authPrivilegeService.refreshPermRoles();
    }
}
