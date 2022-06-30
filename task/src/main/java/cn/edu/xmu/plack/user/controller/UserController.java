package cn.edu.xmu.plack.user.controller;


import cn.edu.xmu.plack.core.util.Common;
import cn.edu.xmu.plack.core.util.ReturnNo;
import cn.edu.xmu.plack.core.util.ReturnObject;
import cn.edu.xmu.plack.core.util.UserInfoUtil;
import cn.edu.xmu.plack.user.model.vo.NewPasswordVo;
import cn.edu.xmu.plack.user.model.vo.SimpleUserVo;
import cn.edu.xmu.plack.user.model.vo.UserVo;
import cn.edu.xmu.plack.user.service.impl.UserServiceImpl;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author bwly
 * @since 2022-02-07
 */
@RestController
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class UserController {
    @Autowired
    private HttpServletResponse httpServletResponse;
    
    @Autowired
    private UserServiceImpl userService;


    /**
     * 用户登录
     * @return
     * @throws HttpRequestMethodNotSupportedException
     */
    @ApiOperation(value = "用户登录")
    @PostMapping("/oauth/token")
    public Object postAccessToken(@RequestBody UserVo vo
    ){
        return Common.decorateReturnObject(userService.isValid(vo));
    }
    /**
     * 用户注册
     * @param vo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "用户注册")
    @PostMapping("/users")
    public Object registerUser(@Validated @RequestBody UserVo vo,
                               BindingResult bindingResult)
    {
        Object o= Common.processFieldErrors(bindingResult,httpServletResponse);
        if(o!=null) {
            return o;
        }

        ReturnObject returnObject = userService.registerUser(vo);
        if(returnObject.getCode() != ReturnNo.OK) {
            return Common.decorateReturnObject(returnObject);
        }
        return Common.decorateReturnObject(returnObject);
    }


    /**
     * 用户登出
     * @return
     */
    @ApiOperation(value = "用户登出")
    @PostMapping("/user/logout")
    public Object logout() {
        return Common.decorateReturnObject(new ReturnObject());
    }

    /**
     * 用户查询信息
     * @return
     */
    @ApiOperation(value = "用户查询自己信息")
    @ApiImplicitParams(value={
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping("/self")
    public Object getCustomerInfo(@RequestHeader(value = "Authorization") String username)
    {
        ReturnObject returnObject = new ReturnObject(userService.getUserByName(username));
        return Common.decorateReturnObject(returnObject);
    }


    @ApiOperation(value = "用户修改自己的信息")
    @PutMapping("/self")
    public Object changeUserInfo(@Validated @RequestBody SimpleUserVo vo,
                                 BindingResult bindingResult)
    {
        Object o= Common.processFieldErrors(bindingResult,httpServletResponse);
        if(o!=null) {
            return o;
        }
        Long userId = UserInfoUtil.getUserId();
        return Common.decorateReturnObject(userService.changeUserInfo(userId,vo));
    }
//
//
//    @ApiOperation(value = "用户重置密码")
//    @ApiImplicitParams(value={
//            @ApiImplicitParam(paramType = "body", dataType = "PasswordResetVo", name = "vo", value = "用户名", required = true)
//    })
//    @ApiResponses(value = {
//            @ApiResponse(code = 0, message = "成功")
//    })
//    @PutMapping("/password/reset")
//    public Object ResetPassword(@Validated @RequestBody PasswordResetVo vo,
//                                BindingResult bindingResult)
//    {
//        Object o = Common.processFieldErrors(bindingResult, httpServletResponse);
//        if(null!=o){
//            return o;
//        }
//        return Common.decorateReturnObject(userService.ResetUserPassword(vo));
//    }
//
//
    /**
     * 修改用户密码
     * @param vo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "用户修改密码")
    @ApiImplicitParams(value={
            @ApiImplicitParam(paramType = "body", dataType = "NewPasswordVo", name = "vo", value ="修改的密码", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "成功")
    })
    @PostMapping("/user/update")
    public Object changePassword(@Validated @RequestBody NewPasswordVo vo,
                                 BindingResult bindingResult)
    {
        Object o= Common.processFieldErrors(bindingResult,httpServletResponse);
        if(o!=null)
        {
            return o;
        }
        return Common.decorateReturnObject(userService.changeUserPassword(vo.getUsername(), vo));
    }

}

