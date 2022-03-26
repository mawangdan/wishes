package cn.edu.xmu.wishes.task.service.imp;

import cn.edu.xmu.wishes.task.mapper.TaskTypeMapper;
import cn.edu.xmu.wishes.task.model.po.TaskType;
import cn.edu.xmu.wishes.task.service.TaskTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service("taskTypeService")
public class TaskTypeServiceImp extends ServiceImpl<TaskTypeMapper, TaskType> implements TaskTypeService {
    @Override
    @Cacheable("task.type.name")
    public String getTypeName(Byte type) {
        return this.getById(type).getName();
    }
}
