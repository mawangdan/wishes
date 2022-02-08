package cn.edu.xmu.wishes.user.service.impl;

import cn.edu.xmu.wishes.core.util.JwtHelper;
import cn.edu.xmu.wishes.core.util.RedisUtil;
import cn.edu.xmu.wishes.core.util.ReturnNo;
import cn.edu.xmu.wishes.core.util.ReturnObject;
import cn.edu.xmu.wishes.user.model.po.User;
import cn.edu.xmu.wishes.user.mapper.UserMapper;
import cn.edu.xmu.wishes.user.model.vo.LoginVo;
import cn.edu.xmu.wishes.user.model.vo.UserRetVo;
import cn.edu.xmu.wishes.user.model.vo.UserVo;
import cn.edu.xmu.wishes.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static cn.edu.xmu.wishes.core.util.Common.cloneVo;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bwly
 * @since 2022-02-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final static String USER_KEY="user_%d";

    @Value("${user.login.jwt.expire}")
    private Integer USER_EXPIRE_TIME;

    private static JwtHelper jwtHelper = new JwtHelper();

    @Autowired
    private RedisUtil redisUtil;

    @Transactional(rollbackFor = Exception.class)
    public ReturnObject registerUser(UserVo userVo) {
        User user = cloneVo(userVo, User.class);
        try {
            //判断是否可以注册
            List<User> userList = lambdaQuery().eq(User::getMobile, user.getMobile())
                    .or().eq(User::getEmail, user.getEmail())
                    .or().eq(User::getUserName, user.getUserName()).list();

            if (userList.size() > 0) {
                for(User user1 : userList) {
                    if (user1.getUserName().equals(user.getUserName())) {
                        return new ReturnObject(ReturnNo.CUSTOMER_NAMEEXIST);
                    }
                    else if (user1.getMobile().equals(user.getMobile())) {
                        return new ReturnObject(ReturnNo.CUSTOMER_MOBILEEXIST);
                    }
                    else if (user1.getEmail().equals(user.getEmail())) {
                        return new ReturnObject(ReturnNo.CUSTOMER_EMAILEXIST);
                    }
                }
            }

            // 保存到数据库
            save(user);
            return new ReturnObject();
        }
        catch (Exception e) {
            log.error("注册失败" + e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ReturnObject login(LoginVo loginVo) {
        User user = lambdaQuery().eq(User::getUserName, loginVo.getUserName()).one();

        if (user == null || !user.getPassword().equals(loginVo.getPassword())) {
            return new ReturnObject(ReturnNo.CUSTOMER_INVALID_ACCOUNT);
        }
        else if (user.getState() == User.Type.BANNED) {
            return new ReturnObject(ReturnNo.CUSTOMER_FORBIDDEN);
        }

        return createToken(user);
    }

    private ReturnObject createToken(User user) {
        try {
            String token = jwtHelper.createToken(user.getId(), user.getUserName(), 1L, 1, USER_EXPIRE_TIME);
            String key = String.format(USER_KEY, user.getId());

            redisUtil.addSet(key, token, USER_EXPIRE_TIME);
            return new ReturnObject(token);
        }
        catch (Exception e) {
            log.error("token " + e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR);
        }
    }

    private void banJwt(String jwt) {
        String[] banSetName = {"BanJwt_0", "BanJwt_1"};
        String banIndexKey = "banIndex";
        String scriptPath = "scripts/ban-jwt.lua";

        DefaultRedisScript<Void> script = new DefaultRedisScript<>();

        script.setScriptSource(new ResourceScriptSource(new ClassPathResource(scriptPath)));
        script.setResultType(Void.class);

        List<String> keyList = new ArrayList<>(List.of(banSetName));
        keyList.add(banIndexKey);
        redisUtil.executeScript(script, keyList, banSetName.length, jwt, USER_EXPIRE_TIME);
    }

    private void banToken(Long id)
    {
        String key = String.format(USER_KEY, id);
        Set<Serializable> set = redisUtil.getSet(key);
        String jwt = null;
        for (Serializable str : set) {
            /* 找出JWT */
            if ((str.toString()).length() > 8) {
                jwt = str.toString();
                banJwt(jwt);
            }
        }
        redisUtil.del(key);
    }

    public ReturnObject logout(Long userId)
    {
        try {
            banToken(userId);
            return new ReturnObject(ReturnNo.OK);
        } catch (Exception e) {
            log.error("logout " + e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR);
        }
    }

    @Transactional(readOnly = true)
    public ReturnObject getUserInfo(Long userId) {
        try {
            User user = getById(userId);
            return new ReturnObject(cloneVo(user, UserRetVo.class));
        } catch (Exception e) {
            log.error("logout " + e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR);
        }
    }
}
