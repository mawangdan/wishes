package cn.edu.xmu.wishes.task.service.imp;

import cn.edu.xmu.wishes.core.util.Common;
import cn.edu.xmu.wishes.core.util.ReturnNo;
import cn.edu.xmu.wishes.core.util.ReturnObject;
import cn.edu.xmu.wishes.task.mapper.TaskMapper;
import cn.edu.xmu.wishes.task.model.po.Task;
import cn.edu.xmu.wishes.task.model.vo.TaskRetVo;
import cn.edu.xmu.wishes.task.model.vo.TaskVo;
import cn.edu.xmu.wishes.task.service.TaskService;
import cn.edu.xmu.wishes.task.service.TaskTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("taskService")
public class TaskServiceImp extends ServiceImpl<TaskMapper, Task> implements TaskService {

    @Autowired
    private TaskTypeService taskTypeService;

    private final String taskCacheKey = "task#3600";

    @Override
    public ReturnObject listTask(Long initiatorId, Long typeId, Integer page, Integer pageSize) {
        Page<Task> taskPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
        Optional.ofNullable(initiatorId).ifPresent(x -> queryWrapper.eq(Task::getInitiatorId, x));
        Optional.ofNullable(typeId).ifPresent(x -> queryWrapper.eq(Task::getTypeId, x));

        Page<Task> tasks = this.baseMapper.selectPage(taskPage, queryWrapper);

        Page<TaskRetVo> taskRetVoPage = Common.transformPageVo(tasks, TaskRetVo.class);
        ReturnObject returnObject = Common.getPageRetVo(tasks, TaskRetVo.class);
        List<TaskRetVo> taskRetVos = taskRetVoPage.getRecords();
        TaskRetVo taskRetVo;
        for(int i=0;i<taskRetVos.size();i++) {
            taskRetVo = taskRetVos.get(i);
            taskRetVo.setType(taskTypeService.getTypeName(tasks.getRecords().get(i).getTypeId()));
        }
        return returnObject;
    }

    @Override
    @Cacheable(cacheNames = taskCacheKey, key = "#id")
    public ReturnObject<Task> getTaskById(Long id) {
        Task task = this.getById(id);
        if (task == null) {
            return new ReturnObject<>(ReturnNo.RESOURCE_ID_NOTEXIST);
        } else {
            return new ReturnObject<>(task);
        }
    }

    @Override
    public ReturnObject saveOrUpdateTask(Long userId, Long taskId, TaskVo taskVo) {
        Task task = new Task();
        BeanUtils.copyProperties(taskVo, task);
        task.setId(taskId);
        task.setInitiatorId(userId);
        if (this.saveOrUpdate(task)) {
            return new ReturnObject();
        } else {
            return ReturnObject.INTERNAL_SERVER_ERR_RET;
        }
    }

    @Override
    public ReturnObject acceptTask(Long taskId, Long userId) {
        LambdaUpdateWrapper<Task> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(Task::getId, userId)
                .eq(Task::getState, Task.StateType.NOT_ACCEPTED)
                .set(Task::getState, Task.StateType.ACCEPTED)
                .set(Task::getReceiverId, userId);
        boolean isUpdated = this.update(updateWrapper);
        if (isUpdated) {
            return ReturnObject.OK_RET;
        } else {
            return new ReturnObject(ReturnNo.AUTH_NO_RIGHT, "任务不存在或已被接取");
        }
    }

}
