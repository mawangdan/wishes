package cn.edu.xmu.wishes.task.service.imp;

import cn.edu.xmu.wishes.core.util.Common;
import cn.edu.xmu.wishes.core.util.ReturnNo;
import cn.edu.xmu.wishes.core.util.ReturnObject;
import cn.edu.xmu.wishes.task.mapper.TaskMapper;
import cn.edu.xmu.wishes.task.model.po.Task;
import cn.edu.xmu.wishes.task.model.vo.TaskRetVo;
import cn.edu.xmu.wishes.task.service.TaskService;
import cn.edu.xmu.wishes.task.service.TaskTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("taskService")
public class TaskServiceImp extends ServiceImpl<TaskMapper, Task> implements TaskService {

    @Autowired
    private TaskTypeService taskTypeService;

    private final String taskCacheKey = "task#3600";

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReturnObject listTask(Long initiatorId, Byte type, Integer page, Integer pageSize) {
        Page<Task> taskPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
        Optional.ofNullable(initiatorId).ifPresent(x -> queryWrapper.eq(Task::getInitiatorId, x));
        Optional.ofNullable(type).ifPresent(x -> queryWrapper.eq(Task::getType, x));

        Page<Task> tasks = this.baseMapper.selectPage(taskPage, queryWrapper);
        ReturnObject returnObject = Common.getPageRetVo(tasks, TaskRetVo.class);
        List<TaskRetVo> taskRetVos = (List<TaskRetVo>) ((Map<String, Object>) returnObject.getData()).get("list");
        TaskRetVo taskRetVo;
        for(int i=0;i<taskRetVos.size();i++) {
            taskRetVo = taskRetVos.get(i);
            taskRetVo.setType(taskTypeService.getTypeName(tasks.getRecords().get(i).getType()));
        }
        return returnObject;
    }

    @Override
    @Cacheable(cacheNames = taskCacheKey, key = "#id")
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReturnObject getTaskById(Long id) {
        Task task = this.getById(id);
        if (task == null) {
            return new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST);
        } else {
            return new ReturnObject(task);
        }
    }

}
