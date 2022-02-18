package cn.edu.xmu.wishes.task.service.imp;

import cn.edu.xmu.wishes.core.util.Common;
import cn.edu.xmu.wishes.core.util.JacksonUtil;
import cn.edu.xmu.wishes.core.util.ReturnNo;
import cn.edu.xmu.wishes.core.util.ReturnObject;
import cn.edu.xmu.wishes.core.util.storage.StorageUtil;
import cn.edu.xmu.wishes.task.mapper.TaskDraftMapper;
import cn.edu.xmu.wishes.task.model.po.TaskDraft;
import cn.edu.xmu.wishes.task.model.vo.TaskDraftVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskDraftServiceImp extends ServiceImpl<TaskDraftMapper, TaskDraft> {
    @Autowired
    private StorageUtil storageUtil;

    @Transactional(rollbackFor = Exception.class)
    public ReturnObject createTaskDraft(TaskDraftVo taskDraftVo) {
        try {
            TaskDraft taskDraft = Common.cloneVo(taskDraftVo, TaskDraft.class);
            save(taskDraft);
            return new ReturnObject();
        }
        catch (Exception e) {
            log.error("createTaskDraft" + e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReturnObject getTaskDraftById(Long id) {
        try {
            TaskDraft taskDraft = getById(id);
            if (taskDraft == null) {
                return new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST);
            }
            return new ReturnObject(taskDraft);
        }
        catch (Exception e) {
            log.error("getTaskDraftById" + e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ReturnObject uploadTaskImage(Long id, List<MultipartFile> files) {
        try {
            TaskDraft taskDraft = getById(id);
            if (taskDraft == null) {
                return new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST);
            }

            String imageUrls = taskDraft.getImageUrl();
            List<String> fileUrlList;
            if (imageUrls != null) {
                fileUrlList = JacksonUtil.parseObjectList(imageUrls, String.class);
            }
            else {
                fileUrlList = new ArrayList<>(files.size());
            }

            // 存储图片并将url放入imaUrls中
            String url;
            for (MultipartFile file : files) {
                url = storageUtil.storeImg(file.getInputStream(), file.getOriginalFilename());
                if (url == null) {
                    return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR);
                }
                else {
                    fileUrlList.add(url);
                }
            }
            taskDraft.setImageUrl(JacksonUtil.toJson(fileUrlList));
            updateById(taskDraft);
            return new ReturnObject();
        }
        catch (Exception e) {
            log.error("uploadTaskImage" + e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR);
        }
    }



}
