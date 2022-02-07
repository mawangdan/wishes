package cn.edu.xmu.wishes.user.controller;


import cn.edu.xmu.wishes.core.aop.Audit;
import cn.edu.xmu.wishes.core.aop.LoginUser;
import cn.edu.xmu.wishes.core.util.Common;
import cn.edu.xmu.wishes.core.util.ResponseUtil;
import cn.edu.xmu.wishes.core.util.ReturnNo;
import cn.edu.xmu.wishes.core.util.ReturnObject;
import cn.edu.xmu.wishes.user.model.vo.LoginVo;
import cn.edu.xmu.wishes.user.model.vo.UserVo;
import cn.edu.xmu.wishes.user.service.impl.UserServiceImpl;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
     * 注册用户
     * @param vo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "注册用户")
    @ApiImplicitParams(value={
            @ApiImplicitParam(paramType = "body", dataType = "UserVo", name = "vo", value = "用户信息", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "成功")
    })
    @PostMapping("/customers")
    public Object registerUser(@Validated @RequestBody UserVo vo,
                               BindingResult bindingResult)
    {
        Object o= Common.processFieldErrors(bindingResult,httpServletResponse);
        if(o!=null)
        {
            return o;
        }
        ReturnObject returnObject = userService.registerUser(vo);
        if(returnObject.getData()==null)
            return Common.decorateReturnObject(returnObject);
        return new ResponseEntity(ResponseUtil.ok(returnObject.getData()), HttpStatus.CREATED);
    }


    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public Object userLogin(@Validated @RequestBody LoginVo loginVo,
                            BindingResult bindingResult)
    {
        Object o= Common.processFieldErrors(bindingResult,httpServletResponse);
        if(o!=null)
        {
            return o;
        }
        ReturnObject returnObject = userService.login(loginVo);
        if (returnObject.getCode().equals(ReturnNo.CUSTOMER_INVALID_ACCOUNT))
            return new ResponseEntity(ResponseUtil.fail(returnObject.getCode(), returnObject.getErrmsg()), HttpStatus.UNAUTHORIZED);
        if (returnObject.getCode().equals(ReturnNo.OK))
            return new ResponseEntity(ResponseUtil.ok(returnObject.getData()), HttpStatus.CREATED);
        if(returnObject.getCode().equals(ReturnNo.CUSTOMER_FORBIDDEN))
            return new ResponseEntity(ResponseUtil.fail(returnObject.getCode(), returnObject.getErrmsg()), HttpStatus.FORBIDDEN);
        return Common.decorateReturnObject(returnObject);
    }
    @ApiOperation(value = "用户登出")
    @ApiImplicitParams(value={
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "成功")
    })

    @Audit
    @GetMapping("/logout")
    public Object logout(@LoginUser Long userid)
    {

        return Common.decorateReturnObject(userService.logout(userid));
    }
    /**
     * 用户查询信息
     * @param userid
     * @return
     */
    @ApiOperation(value = "用户查询自己信息")
    @ApiImplicitParams(value={
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @GetMapping("/self")
    public Object getCustomerInfo(@LoginUser Long userid)
    {
        ReturnObject returnObject = userService.getUserInfo(userid);
        return Common.decorateReturnObject(returnObject);
    }


    /**
     * 买家修改自己的信息
     * @param userId
     * @param vo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "买家修改自己的信息")
    @ApiImplicitParams(value={
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "SimpleUserVo", name = "vo", value = "可修改的用户信息", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @PutMapping("/self")
    public Object changeUserInfo(@LoginUser Long userId,
                                 @Validated @RequestBody SimpleUserVo vo,
                                 BindingResult bindingResult)
    {
        Object o= Common.processFieldErrors(bindingResult,httpServletResponse);
        if(o!=null)
        {
            return o;
        }
        return Common.decorateReturnObject(userService.changeUserInfo(userId,vo));
    }


    @ApiOperation(value = "用户重置密码")
    @ApiImplicitParams(value={
            @ApiImplicitParam(paramType = "body", dataType = "PasswordResetVo", name = "vo", value = "用户名", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "成功")
    })
    @PutMapping("/password/reset")
    public Object ResetPassword(@Validated @RequestBody PasswordResetVo vo,
                                BindingResult bindingResult)
    {
        Object o = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(null!=o){
            return o;
        }
        return Common.decorateReturnObject(userService.ResetUserPassword(vo));
    }


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
    @PutMapping("/password")
    public Object changePassword(@Validated @RequestBody NewPasswordVo vo,
                                 BindingResult bindingResult)
    {
        Object o= Common.processFieldErrors(bindingResult,httpServletResponse);
        if(o!=null)
        {
            return o;
        }
        return Common.decorateReturnObject(userService.changeUserPassword(vo));
    }
}

