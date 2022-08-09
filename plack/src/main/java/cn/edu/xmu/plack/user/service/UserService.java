package cn.edu.xmu.plack.user.service;

import cn.edu.xmu.plack.core.util.ReturnObject;
import cn.edu.xmu.plack.user.model.po.User;
import cn.edu.xmu.plack.user.model.vo.NewPasswordVo;
import cn.edu.xmu.plack.user.model.vo.UserVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {
    User getUserByName(String userName);
    ReturnObject changeUserPassword(String userEmail, NewPasswordVo vo);
    ReturnObject isValid(UserVo vo);
}
