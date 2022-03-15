package cn.edu.xmu.wishes.task.service;

import cn.edu.xmu.wishes.core.util.ReturnObject;
import cn.edu.xmu.wishes.task.model.po.TaskDraft;
import cn.edu.xmu.wishes.task.model.vo.TaskDraftVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskDraftService extends IService<TaskDraft> {
    ReturnObject insertTaskDraft(TaskDraftVo taskDraftVo);

    ReturnObject getTaskDraftById(Long id);

    ReturnObject uploadTaskImage(Long id, List<MultipartFile> files);
}
