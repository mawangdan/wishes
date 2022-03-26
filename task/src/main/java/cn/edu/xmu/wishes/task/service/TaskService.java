package cn.edu.xmu.wishes.task.service;

import cn.edu.xmu.wishes.core.util.ReturnObject;
import cn.edu.xmu.wishes.task.model.po.Task;
import com.baomidou.mybatisplus.extension.service.IService;

public interface TaskService extends IService<Task> {
    public ReturnObject listTask(Long initiatorId, Byte type, Integer page, Integer pageSize);
}
