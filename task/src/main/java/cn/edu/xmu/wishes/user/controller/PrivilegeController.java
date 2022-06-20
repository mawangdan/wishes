package cn.edu.xmu.wishes.user.controller;

import cn.edu.xmu.wishes.core.util.InternalReturnObject;
import cn.edu.xmu.wishes.user.service.AuthPrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class PrivilegeController {
    @Autowired
    private AuthPrivilegeService authPrivilegeService;

    @PutMapping("/internal/privilege/load")
    public Object refreshPermRoles() {
        authPrivilegeService.refreshPermRoles();
        return new InternalReturnObject<>();
    }
}
