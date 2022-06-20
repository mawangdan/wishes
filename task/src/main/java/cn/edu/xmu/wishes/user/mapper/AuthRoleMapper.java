package cn.edu.xmu.wishes.user.mapper;

import cn.edu.xmu.wishes.user.model.po.AuthRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AuthRoleMapper extends BaseMapper<AuthRole> {
    @Select("SELECT b.* from wishes_auth_user_role a, wishes_auth_role b  where a.user_id = #{userId} and a.role_id = b.id")
    List<AuthRole> getRolesByUserId(Long userId);
}
