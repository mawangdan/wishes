package cn.edu.xmu.wishes.task.controller;

import cn.edu.xmu.wishes.core.util.Common;
import cn.edu.xmu.wishes.core.util.ReturnNo;
import cn.edu.xmu.wishes.core.util.ReturnObject;
import cn.edu.xmu.wishes.task.model.vo.TaskDraftVo;
import cn.edu.xmu.wishes.task.service.imp.TaskDraftServiceImp;
import cn.edu.xmu.wishes.task.service.imp.TaskServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class TaskController {
    @Autowired
    private TaskServiceImp taskService;

    @Autowired
    private TaskDraftServiceImp taskDraftService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    private static final Integer IMAGE_MAX_SIZE=1000000;

    @PostMapping("/taskdraft/add")
    public Object createTaskDraft(@Validated @RequestBody TaskDraftVo taskDraftVo, BindingResult bindingResult) {
        Object o= Common.processFieldErrors(bindingResult,httpServletResponse);
        if(o!=null) {
            return o;
        }

        return Common.decorateReturnObject(taskDraftService.createTaskDraft(taskDraftVo));
    }

    @GetMapping("/taskdraft/{id}")
    public Object getTaskDraftById(@PathVariable("id") Long id) {
        return Common.decorateReturnObject(taskDraftService.getTaskDraftById(id));
    }

    @PostMapping("taskdraft/{id}/uploadImg")
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
        return Common.decorateReturnObject(taskDraftService.uploadTaskImage(id, files));

    }

    @GetMapping("/task/{id}")
    public Object getTaskById(@PathVariable("id") Long id) {
        return Common.decorateReturnObject(new ReturnObject(taskService.lambdaQuery().list()));
    }
}
