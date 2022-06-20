package cn.edu.xmu.wishes.user.security.userdetails;

import cn.edu.xmu.wishes.user.mapper.AuthPrivilegeMapper;
import cn.edu.xmu.wishes.user.mapper.AuthRoleMapper;
import cn.edu.xmu.wishes.user.mapper.UserMapper;
import cn.edu.xmu.wishes.user.model.po.AuthRole;
import cn.edu.xmu.wishes.user.model.po.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthPrivilegeMapper authPrivilegeMapper;

    @Autowired
    private AuthRoleMapper authRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        if (!StringUtils.hasLength(name)) {
            throw new UsernameNotFoundException("用户名不能为空");
        }

        User user = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getUserName, name));
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户名不存在");
        }

        List<AuthRole> authRoleList = authRoleMapper.getRolesByUserId(user.getId());
        List<Long> roleIdList = authRoleList.stream().map(AuthRole::getId).collect(Collectors.toList());
        //Todo 当roleIdList is empty 时 要判断 不然下面的查询会出现in () 出现sql语法错误
//        List<AuthPrivilege> privilegeList = authPrivilegeMapper.getPrivilegesByRoleId(new QueryWrapper<AuthPrivilege>().in("role_id", roleIdList));
        SecurityUser securityUser = new SecurityUser(user, authRoleList);
        return securityUser;
    }
}
