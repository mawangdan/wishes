package cn.edu.xmu.wishes.user.mapper;

import cn.edu.xmu.wishes.user.model.po.AuthPrivilege;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AuthPrivilegeMapper extends BaseMapper<AuthPrivilege> {
    @Select("SELECT b.* from wishes_auth_role_privilege a join wishes_auth_privilege b on a.privilege_id = b.id ${ew.customSqlSegment}")
    List<AuthPrivilege> getPrivilegesByRoleId(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("SELECT a.*, role_id " +
            "from wishes_auth_privilege a left join wishes_auth_role_privilege b on a.id = b.privilege_id " +
            "left join wishes_auth_role c on c.id = b.role_id ")
    @Results(value = {
            @Result(property = "roleList", javaType = List.class, column = "role_id",
                    many = @Many(select = "cn.edu.xmu.wishes.user.mapper.AuthRoleMapper.selectById"))
    })
    List<AuthPrivilege> listPermRoles();
}
