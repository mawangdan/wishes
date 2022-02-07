package cn.edu.xmu.wishes.task.controller;

import cn.edu.xmu.wishes.core.util.Common;
import cn.edu.xmu.wishes.core.util.ReturnObject;
import cn.edu.xmu.wishes.task.model.po.Task;
import cn.edu.xmu.wishes.task.service.TaskService;
import cn.edu.xmu.wishes.task.service.TaskServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class TaskController {
    @Autowired
    private TaskServiceImp taskService;

    @GetMapping("/task/add")
    public Object createTask() {
        Task task = new Task();
        task.setInitiatorId(1L);
        task.setDescription("测试一下");
        taskService.save(task);
        return Common.decorateReturnObject(new ReturnObject());
    }

    @GetMapping("/task/{id}")
    public Object getTaskById(@PathVariable("id") Long id) {
        return Common.decorateReturnObject(new ReturnObject(taskService.lambdaQuery().list()));
    }
}
