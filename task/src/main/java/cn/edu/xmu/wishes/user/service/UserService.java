package cn.edu.xmu.wishes.user.service;

import cn.edu.xmu.wishes.core.util.ReturnObject;
import cn.edu.xmu.wishes.user.model.po.User;
import cn.edu.xmu.wishes.user.model.vo.NewPasswordVo;
import cn.edu.xmu.wishes.user.model.vo.UserVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author bwly
 * @since 2022-02-07
 */
public interface UserService extends IService<User> {
    User getUserByName(String userName);
    ReturnObject changeUserPassword(String userEmail, NewPasswordVo vo);
    ReturnObject isValid(UserVo vo);
}
