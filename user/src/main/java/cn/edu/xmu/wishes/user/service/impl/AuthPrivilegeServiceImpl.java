package cn.edu.xmu.wishes.user.service.impl;

import cn.edu.xmu.wishes.user.mapper.AuthPrivilegeMapper;
import cn.edu.xmu.wishes.user.model.po.AuthPrivilege;
import cn.edu.xmu.wishes.user.service.AuthPrivilegeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("authPrivilegeService")
public class AuthPrivilegeServiceImpl extends ServiceImpl<AuthPrivilegeMapper, AuthPrivilege> implements AuthPrivilegeService {
}
