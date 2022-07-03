package cn.edu.xmu.plack.admin.controller;

import cn.edu.xmu.plack.admin.model.po.Admin;
import cn.edu.xmu.plack.admin.model.vo.AdminVo;
import cn.edu.xmu.plack.admin.service.AdminService;
import cn.edu.xmu.plack.admin.util.JwtUtil;
import cn.edu.xmu.plack.core.util.Common;
import cn.edu.xmu.plack.core.util.ReturnNo;
import cn.edu.xmu.plack.core.util.ReturnObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin", produces = "application/json;charset=UTF-8")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @ApiOperation("管理员登录")
    @PostMapping("/login")
    public Object login(@RequestBody AdminVo adminVo) {
        LambdaQueryWrapper<Admin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Admin::getUserName, adminVo.getUserName());
        lambdaQueryWrapper.eq(Admin::getUserName, adminVo.getUserName());
        Admin admin = adminService.getOne(lambdaQueryWrapper);
        ReturnObject returnObject;
        if (admin == null) {
            returnObject = new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST, "用户名不存在或密码错误");
        } else {
            String token = JwtUtil.creatToken(admin);
            returnObject = new ReturnObject(token);
        }
        return Common.decorateReturnObject(returnObject);
    }
}
