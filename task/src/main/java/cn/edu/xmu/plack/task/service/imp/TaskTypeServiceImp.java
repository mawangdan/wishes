package cn.edu.xmu.plack.task.service.imp;

import cn.edu.xmu.plack.task.service.TaskTypeService;
import cn.edu.xmu.plack.task.mapper.TaskTypeMapper;
import cn.edu.xmu.plack.task.model.po.TaskType;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service("taskTypeService")
public class TaskTypeServiceImp extends ServiceImpl<TaskTypeMapper, TaskType> implements TaskTypeService {
    @Override
    @Cacheable("task.type.name")
    public String getTypeName(Long typeId) {
        TaskType taskType = this.getById(typeId);
        if (taskType == null) {
            return "未知";
        } else {
            return taskType.getName();
        }
    }
}
