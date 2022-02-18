package cn.edu.xmu.wishes.task.controller;

import cn.edu.xmu.wishes.core.util.Common;
import cn.edu.xmu.wishes.core.util.ReturnNo;
import cn.edu.xmu.wishes.core.util.ReturnObject;
import cn.edu.xmu.wishes.task.model.po.Task;
import cn.edu.xmu.wishes.task.service.imp.TaskServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class TaskController {
    @Autowired
    private TaskServiceImp taskService;

    private static final Integer IMAGE_MAX_SIZE=1000000;

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

    @PostMapping("task/{id}/uploadImg")
    public Object uploadTaskImage(@PathVariable Long id, HttpServletRequest request) {

        //对输入数据进行合法性判断
        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
                .getFiles("file");
        if(files.size()<=0){
            return Common.decorateReturnObject(new ReturnObject<>(ReturnNo.FIELD_NOTVALID));
        }
        //图片超限
        if (files.stream().anyMatch((multipartFile -> multipartFile.getSize() > IMAGE_MAX_SIZE))) {
            return Common.decorateReturnObject(new ReturnObject<>(ReturnNo.IMG_SIZE_EXCEED));
        }
        return Common.decorateReturnObject(taskService.uploadTaskImage(id, files));

    }
}
