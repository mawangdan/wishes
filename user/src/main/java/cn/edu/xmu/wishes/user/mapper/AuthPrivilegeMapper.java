package cn.edu.xmu.wishes.user.mapper;

import cn.edu.xmu.wishes.user.model.po.AuthPrivilege;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AuthPrivilegeMapper extends BaseMapper<AuthPrivilege> {
    @Select("SELECT b.* from wishes_auth_role_privilege a, wishes_auth_privilege b  where a.role_id = #{roleId} and a.privilege_id = b.id")
    List<AuthPrivilege> getPrivilegesByRoleId(Long roleId);
}
