package cn.edu.xmu.wishes.task.service.imp;

import cn.edu.xmu.wishes.core.util.ReturnObject;
import cn.edu.xmu.wishes.task.mapper.TaskMapper;
import cn.edu.xmu.wishes.task.model.po.Task;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class TaskServiceImp extends ServiceImpl<TaskMapper, Task> {
    public ReturnObject uploadTaskImage(Long id, List<MultipartFile> files) {


    }
}
