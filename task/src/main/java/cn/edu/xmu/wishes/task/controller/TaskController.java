package cn.edu.xmu.wishes.task.controller;

import cn.edu.xmu.wishes.core.util.Common;
import cn.edu.xmu.wishes.core.util.ReturnNo;
import cn.edu.xmu.wishes.core.util.ReturnObject;
import cn.edu.xmu.wishes.core.util.UserInfoUtil;
import cn.edu.xmu.wishes.core.util.storage.StorageUtil;
import cn.edu.xmu.wishes.task.model.po.Task;
import cn.edu.xmu.wishes.task.model.vo.TaskVo;
import cn.edu.xmu.wishes.task.service.TaskService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class TaskController {
    @Autowired
    private TaskService taskService;

    /**
     * 上传文件
     * @param file 文件
     * @return
     * @throws Exception
     */
    @ApiOperation("上传文件")
    @PostMapping("/task/file")
    public Object uploadFile(@RequestParam(value = "file") MultipartFile file) throws Exception {
        try {
            if (file == null) {
                return Common.decorateReturnObject(ReturnObject.FILE_NOT_EXIST_RET);
            }

            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            String path = StorageUtil.store(file.getInputStream(), suffix);
            return Common.decorateReturnObject(new ReturnObject(path));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @ApiOperation("新增任务")
    @PostMapping("/task")
    public Object createTask(@RequestBody @Validated TaskVo taskVo) {
        Long userId = UserInfoUtil.getUserId();
        return Common.decorateReturnObject(taskService.saveOrUpdateTask(userId, null, taskVo));
    }

    @ApiOperation("修改任务")
    @PutMapping("/task/{id}")
    public Object updateTask(@PathVariable("id") Long taskId, @RequestBody @Validated TaskVo taskVo) {
        Long userId = UserInfoUtil.getUserId();

        ReturnObject<Task> retObj = taskService.getTaskById(taskId);
        if (retObj.getCode() != ReturnNo.OK) {
            return Common.decorateReturnObject(ReturnObject.RESOURCE_ID_NOTEXIST_RET);
        }
        else if (!retObj.getData().getInitiatorId().equals(userId)) {
            return Common.decorateReturnObject(ReturnObject.RESOURCE_ID_OUTSCOPE_RET);
        }
        return Common.decorateReturnObject(taskService.saveOrUpdateTask(userId, taskId, taskVo));
    }

//    public static void main(String[] args) {
//        EnumSet<ReturnNo> enumSet = EnumSet.allOf(ReturnNo.class);
//        enumSet.forEach(x -> System.out.printf("public static ReturnObject %s_RET = new ReturnObject(ReturnNo.%s);\n", x.name(), x.name()));
//    }

    /**
     * 通过id获取任务
     * @param id
     * @return
     */
    @ApiOperation("通过id获取任务")
    @GetMapping("/task/{id}")
    public Object getTaskById(@PathVariable("id") Long id) {
        return Common.decorateReturnObject(taskService.getTaskById(id));
    }

    /**
     * 通过筛选条件获取分页后的任务
     * @param initiatorId 发起者id
     * @param typeId 类型id
     * @param page 页号
     * @param pageSize 页大小
     * @return
     */
    @ApiOperation("通过筛选条件获取分页后的任务")
    @GetMapping("/task")
    public Object listTask(@RequestParam(required = false) Long initiatorId,  @RequestParam(required = false) Long typeId,
                           @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        return Common.decorateReturnObject(taskService.listTask(initiatorId, typeId, page, pageSize));
    }

    @ApiOperation("用户接取任务")
    @PostMapping("/task/{id}:accept")
    public Object acceptTask(@PathVariable("id") Long taskId) {
        Long userId = UserInfoUtil.getUserId();
        return Common.decorateReturnObject(taskService.acceptTask(taskId, userId));
    }
}
