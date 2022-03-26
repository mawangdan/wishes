package cn.edu.xmu.wishes.task.service;

import cn.edu.xmu.wishes.task.model.po.TaskType;
import com.baomidou.mybatisplus.extension.service.IService;

public interface TaskTypeService extends IService<TaskType> {
    String getTypeName(Byte type);
}
