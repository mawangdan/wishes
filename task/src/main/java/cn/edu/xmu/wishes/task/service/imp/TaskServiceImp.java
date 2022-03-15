package cn.edu.xmu.wishes.task.service.imp;

import cn.edu.xmu.wishes.core.util.ReturnObject;
import cn.edu.xmu.wishes.task.mapper.TaskMapper;
import cn.edu.xmu.wishes.task.model.po.Task;
import cn.edu.xmu.wishes.task.service.TaskService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("taskService")
public class TaskServiceImp extends ServiceImpl<TaskMapper, Task> implements TaskService {

    public ReturnObject listTask(Integer page, Integer pageSize) {
        Page<Task> taskPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
        Page<Task> tasks = this.baseMapper.selectPage(taskPage, queryWrapper);
        return new ReturnObject(tasks);
    }
}
