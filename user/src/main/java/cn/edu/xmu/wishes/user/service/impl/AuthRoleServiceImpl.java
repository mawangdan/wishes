package cn.edu.xmu.wishes.user.service.impl;

import cn.edu.xmu.wishes.user.mapper.AuthRoleMapper;
import cn.edu.xmu.wishes.user.model.po.AuthRole;
import cn.edu.xmu.wishes.user.service.AuthRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("authRoleService")
public class AuthRoleServiceImpl extends ServiceImpl<AuthRoleMapper, AuthRole> implements AuthRoleService {
}
