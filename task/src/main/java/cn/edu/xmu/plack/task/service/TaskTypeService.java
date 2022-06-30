package cn.edu.xmu.plack.task.service;

import cn.edu.xmu.plack.task.model.po.TaskType;
import com.baomidou.mybatisplus.extension.service.IService;

public interface TaskTypeService extends IService<TaskType> {
    String getTypeName(Long typeId);
}
