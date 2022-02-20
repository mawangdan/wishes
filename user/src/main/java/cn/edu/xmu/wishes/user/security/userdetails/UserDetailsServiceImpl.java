package cn.edu.xmu.wishes.user.security.userdetails;

import cn.edu.xmu.wishes.user.mapper.UserMapper;
import cn.edu.xmu.wishes.user.model.po.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        if (!StringUtils.hasLength(name)) {
            throw new UsernameNotFoundException("用户名不能为空");
        }

        User user = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getUserName, name));
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        return user;
    }
}
