package cn.edu.xmu.plack.admin.service.imp;

import cn.edu.xmu.plack.admin.mapper.AdminMapper;
import cn.edu.xmu.plack.admin.model.po.Admin;
import cn.edu.xmu.plack.admin.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("adminService")
@Slf4j
public class AdminServiceImp extends ServiceImpl<AdminMapper, Admin> implements AdminService {
}
