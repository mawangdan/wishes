package cn.edu.xmu.wishes.task.controller;

import cn.edu.xmu.wishes.core.util.Common;
import cn.edu.xmu.wishes.core.util.ReturnNo;
import cn.edu.xmu.wishes.core.util.ReturnObject;
import cn.edu.xmu.wishes.core.util.UserInfoUtil;
import cn.edu.xmu.wishes.core.util.storage.StorageUtil;
import cn.edu.xmu.wishes.task.model.po.Task;
import cn.edu.xmu.wishes.task.model.vo.TaskDraftVo;
import cn.edu.xmu.wishes.task.model.vo.TaskVo;
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

    /**
     * 新增草稿任务
     * @param taskDraftVo
     * @return
     */
    @ApiOperation("新增草稿任务")
    @PostMapping("/taskdraft/add")
    public Object createTaskDraft(@Validated @RequestBody TaskDraftVo taskDraftVo) {
        return Common.decorateReturnObject(taskDraftService.insertTaskDraft(taskDraftVo));
    }

    /**
     * 通过id获取草稿任务
     * @param id
     * @return
     */
    @ApiOperation("通过id获取草稿任务")
    @GetMapping("/taskdraft/{id}")
    public Object getTaskDraftById(@PathVariable("id") Long id) {
        return Common.decorateReturnObject(taskDraftService.getTaskDraftById(id));
    }

    /**
     * 为草稿任务上传图片
     * @param id
     * @param request
     * @return
     */
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

    /**
     * 修改草稿任务
     * @param id
     * @param taskDraftVo
     * @return
     */
    @ApiOperation("修改草稿任务")
    @PutMapping("/taskdraft/{id}")
    public Object updateTaskDraft(@PathVariable Long id, @RequestBody @Validated TaskDraftVo taskDraftVo) {
        Long userId = UserInfoUtil.getUserId();
        if (!userId.equals(taskDraftVo.getInitiatorId())) {
            return Common.decorateReturnObject(new ReturnObject(ReturnNo.AUTH_NO_RIGHT));
        }
        return Common.decorateReturnObject(taskDraftService.updateTaskDraft(id, taskDraftVo));
    }

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
                return Common.decorateReturnObject(new ReturnObject(ReturnNo.FILE_NOT_EXIST));
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
        Task task = taskService.getById(taskId);
        if (task == null) {
            return Common.decorateReturnObject(ReturnObject.RESOURCE_ID_NOTEXIST_RET);
        }
        else if (!task.getInitiatorId().equals(userId)) {
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
}
