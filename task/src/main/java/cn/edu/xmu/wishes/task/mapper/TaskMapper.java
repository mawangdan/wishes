package cn.edu.xmu.wishes.task.mapper;

import cn.edu.xmu.wishes.core.util.ReturnObject;
import cn.edu.xmu.wishes.task.model.po.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface TaskMapper extends BaseMapper<Task> {
    public ReturnObject listTask(Integer page, Integer pageSize);
}
