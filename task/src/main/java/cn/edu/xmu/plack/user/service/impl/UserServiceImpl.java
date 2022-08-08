package cn.edu.xmu.plack.user.service.impl;

import cn.edu.xmu.plack.core.util.ReturnNo;
import cn.edu.xmu.plack.core.util.ReturnObject;
import cn.edu.xmu.plack.user.mapper.UserMapper;
import cn.edu.xmu.plack.user.model.po.User;
import cn.edu.xmu.plack.user.model.vo.NewPasswordVo;
import cn.edu.xmu.plack.user.model.vo.SimpleUserVo;
import cn.edu.xmu.plack.user.model.vo.UserRetVo;
import cn.edu.xmu.plack.user.model.vo.UserVo;
import cn.edu.xmu.plack.user.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cn.edu.xmu.plack.core.util.Common.cloneVo;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    public ReturnObject registerUser(UserVo userVo) {
        try {
            //判断是否可以注册
            List<User> userList = lambdaQuery().eq(User::getMobile, userVo.getMobile())
                    .or().eq(User::getEmail, userVo.getEmail())
                    .or().eq(User::getUserName, userVo.getUserName()).list();

            if (userList.size() > 0) {
                for(User user1 : userList) {
                    if (user1.getUserName() != null && user1.getUserName().equals(userVo.getUserName())) {
                        return new ReturnObject(ReturnNo.CUSTOMER_NAMEEXIST);
                    }
                    else if (user1.getMobile() != null && user1.getMobile().equals(userVo.getMobile())) {
                        return new ReturnObject(ReturnNo.CUSTOMER_MOBILEEXIST);
                    }
                    else if (user1.getEmail() != null && user1.getEmail().equals(userVo.getEmail())) {
                        return new ReturnObject(ReturnNo.CUSTOMER_EMAILEXIST);
                    }
                }
            }
            User user = cloneVo(userVo, User.class);
            save(user);
            return new ReturnObject();
        }
        catch (Exception e) {
            log.error("注册失败" + e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR);
        }
    }

    public ReturnObject getUserInfo(Long userId) {
        try {
            User user = getById(userId);
            return new ReturnObject(cloneVo(user, UserRetVo.class));
        } catch (Exception e) {
            log.error("logout " + e.getMessage());
            throw e;
        }
    }

    public ReturnObject changeUserInfo(Long userId, SimpleUserVo vo) {
        try {
            User user = new User();
            BeanUtils.copyProperties(vo, user);
            user.setId(userId);
            this.updateById(user);
            return new ReturnObject();
        } catch (Exception e) {
            log.error("changeUserInfo " + e.getMessage());
            throw e;
        }
    }

    @Override
    public User getUserByName(String username) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName, username);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public ReturnObject changeUserPassword(String username, NewPasswordVo vo) {
        try {
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getUserName, username);
            User user = this.getOne(lambdaQueryWrapper);
            if (user == null) {
                return new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST, "用户不存在");
            }
            if (!user.getPassword().equals(vo.getOldpassword())) {
                return new ReturnObject(ReturnNo.CUSTOMER_PASSWORDWRONG);
            }
            user.setPassword(vo.getNewpassword());
            this.updateById(user);
            return new ReturnObject();
        } catch (Exception e) {
            log.error("changeUserPassword " + e.getMessage());
            throw e;
        }
    }
    @Override
    public ReturnObject isValid(UserVo vo) {
        try {
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getUserName, vo.getUserName());
            User user = this.getOne(lambdaQueryWrapper);
            if (user == null) {
                return new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST, "用户不存在");
            }
            if (!user.getPassword().equals(vo.getPassword())) {
                return new ReturnObject(ReturnNo.CUSTOMER_PASSWORDWRONG);
            }
            return new ReturnObject(vo.getUserName());
        } catch (Exception e) {
            log.error("changeUserPassword " + e.getMessage());
            throw e;
        }
    }
}
