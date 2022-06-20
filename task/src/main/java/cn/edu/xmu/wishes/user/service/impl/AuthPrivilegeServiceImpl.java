package cn.edu.xmu.wishes.user.service.impl;

import cn.edu.xmu.wishes.core.constants.SecurityConstants;
import cn.edu.xmu.wishes.user.mapper.AuthPrivilegeMapper;
import cn.edu.xmu.wishes.user.mapper.AuthRoleMapper;
import cn.edu.xmu.wishes.user.model.po.AuthPrivilege;
import cn.edu.xmu.wishes.user.service.AuthPrivilegeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("authPrivilegeService")
public class AuthPrivilegeServiceImpl extends ServiceImpl<AuthPrivilegeMapper, AuthPrivilege> implements AuthPrivilegeService {
    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Autowired
    private AuthRoleMapper authRoleMapper;

    @Override
    @Transactional(readOnly = true)
    public void refreshPermRoles() {
        redisTemplate.delete(SecurityConstants.URL_PERM_ROLES_KEY);
        List<AuthPrivilege> permissions = this.baseMapper.listPermRoles();
        if (!permissions.isEmpty()) {
            // 初始化URL【权限->角色(集合)】规则
            List<AuthPrivilege> urlPermList = permissions.stream()
                    .filter(item -> StringUtils.hasLength(item.getPrivilege()))
                    .collect(Collectors.toList());
            if (!urlPermList.isEmpty()) {
                Map<String, List<String>> urlPermRoles = new HashMap<>();
                urlPermList.stream().forEach(item -> {
                    String perm = item.getPrivilege();
                    List<String> roles;
                    if (item.getRoleList() != null) {
                        roles = item.getRoleList().stream()
                                .map((authRole -> SecurityConstants.AUTHORITY_PREFIX + authRole.getName()))
                                .collect(Collectors.toList());
                    }
                    else {
                        roles = new ArrayList<>();
                    }

                    urlPermRoles.put(perm, roles);
                });
                redisTemplate.opsForHash().putAll(SecurityConstants.URL_PERM_ROLES_KEY, urlPermRoles);
            }
        }
    }
}
