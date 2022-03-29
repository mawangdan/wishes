package cn.edu.xmu.wishes.task.controller;

import cn.edu.xmu.wishes.core.util.Common;
import cn.edu.xmu.wishes.core.util.ReturnNo;
import cn.edu.xmu.wishes.core.util.ReturnObject;
import cn.edu.xmu.wishes.core.util.UserInfoUtil;
import cn.edu.xmu.wishes.task.model.vo.TaskDraftVo;
import cn.edu.xmu.wishes.task.service.TaskDraftService;
import cn.edu.xmu.wishes.task.service.TaskService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskDraftService taskDraftService;

    private static final Integer IMAGE_MAX_SIZE=1000000;

    @ApiOperation("新增草稿任务")
    @PostMapping("/taskdraft/add")
    public Object createTaskDraft(@Validated @RequestBody TaskDraftVo taskDraftVo) {
        return Common.decorateReturnObject(taskDraftService.insertTaskDraft(taskDraftVo));
    }

    @ApiOperation("通过id获取草稿任务")
    @GetMapping("/taskdraft/{id}")
    public Object getTaskDraftById(@PathVariable("id") Long id) {
        return Common.decorateReturnObject(taskDraftService.getTaskDraftById(id));
    }

    @ApiOperation("为草稿任务上传图片")
    @PostMapping("/taskdraft/{id}/uploadImg")
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

    @ApiOperation("修改草稿任务")
    @PutMapping("/taskdraft/{id}")
    public Object updateTaskDraft(@PathVariable Long id, @RequestBody @Validated TaskDraftVo taskDraftVo) {
        Long userId = UserInfoUtil.getUserId();
        if (!userId.equals(taskDraftVo.getInitiatorId())) {
            return Common.decorateReturnObject(new ReturnObject(ReturnNo.AUTH_NO_RIGHT));
        }
        return Common.decorateReturnObject(taskDraftService.updateTaskDraft(id, taskDraftVo));
    }

    @ApiOperation("通过id获取任务")
    @GetMapping("/tasks/{id}")
    public Object getTaskById(@PathVariable("id") Long id) {
        return Common.decorateReturnObject(taskService.getTaskById(id));
    }

    @ApiOperation("通过筛选条件获取分页后任务")
    @GetMapping("/tasks")
    public Object listTask(@RequestParam(required = false) Long initiatorId,  @RequestParam(required = false) Byte type,
                           @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        return Common.decorateReturnObject(taskService.listTask(initiatorId, type, page, pageSize));
    }
}
