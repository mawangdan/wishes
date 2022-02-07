package cn.edu.xmu.wishes.task.service;

import cn.edu.xmu.wishes.task.mapper.TaskMapper;
import cn.edu.xmu.wishes.task.model.po.Task;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImp extends ServiceImpl<TaskMapper, Task> {
}
