package cn.edu.xmu.wishes.user.service.impl;

import cn.edu.xmu.wishes.core.util.JwtHelper;
import cn.edu.xmu.wishes.core.util.RedisUtil;
import cn.edu.xmu.wishes.core.util.ReturnNo;
import cn.edu.xmu.wishes.core.util.ReturnObject;
import cn.edu.xmu.wishes.user.mapper.UserMapper;
import cn.edu.xmu.wishes.user.model.po.User;
import cn.edu.xmu.wishes.user.model.vo.*;
import cn.edu.xmu.wishes.user.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

import static cn.edu.xmu.wishes.core.util.Common.cloneVo;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bwly
 * @since 2022-02-07
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final static String USER_KEY="user_%d";
    private static final String CAPTCHA_KEY = "cap_%s";


    private static JwtHelper jwtHelper = new JwtHelper();

    @Autowired
    private RedisUtil redisUtil;



    @Transactional(rollbackFor = Exception.class)
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
//            String userEmail = userVo.getEmail();
//            String captcha = createCaptcha(userVo);
//            sendEmail(captcha, userEmail);
            return new ReturnObject();
        }
        catch (Exception e) {
            log.error("注册失败" + e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR);
        }
    }




    private String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }






    @Transactional(readOnly = true)
    public ReturnObject getUserInfo(Long userId) {
        try {
            User user = getById(userId);
            return new ReturnObject(cloneVo(user, UserRetVo.class));
        } catch (Exception e) {
            log.error("logout " + e.getMessage());
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
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
        User user = this.getOne(lambdaQueryWrapper);
        return user;
    }

    @Override
    public ReturnObject changeUserPassword(String username, NewPasswordVo vo) {
        try {
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getUserName, username);
            User user = this.getOne(lambdaQueryWrapper);
            if (user == null) {
                return new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST, "该用户未注册");
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
                return new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST, "该用户未注册");
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
