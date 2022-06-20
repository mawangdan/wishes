package cn.edu.xmu.wishes.task.service;

import cn.edu.xmu.wishes.core.util.ReturnObject;
import cn.edu.xmu.wishes.task.model.po.Task;
import cn.edu.xmu.wishes.task.model.vo.TaskVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface TaskService extends IService<Task> {
    ReturnObject listTask(Long initiatorId, Long typeId, Integer page, Integer pageSize);

    ReturnObject<Task> getTaskById(Long id);

    ReturnObject saveOrUpdateTask(Long userId, Long taskId, TaskVo taskVo);

    ReturnObject acceptTask(Long taskId, Long userId);
}
