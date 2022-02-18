package cn.edu.xmu.wishes.task.service.imp;

import cn.edu.xmu.wishes.core.util.JacksonUtil;
import cn.edu.xmu.wishes.core.util.ReturnNo;
import cn.edu.xmu.wishes.core.util.ReturnObject;
import cn.edu.xmu.wishes.core.util.storage.StorageUtil;
import cn.edu.xmu.wishes.task.mapper.TaskMapper;
import cn.edu.xmu.wishes.task.model.po.Task;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImp extends ServiceImpl<TaskMapper, Task> {
    @Autowired
    private StorageUtil storageUtil;

    public ReturnObject uploadTaskImage(Long id, List<MultipartFile> files) {
        try {
            Task task = getById(id);
            if (task == null) {
                return new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST);
            }

            String imageUrls = task.getImageUrl();
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
            task.setImageUrl(JacksonUtil.toJson(fileUrlList));
            updateById(task);
            return new ReturnObject();
        }
        catch (Exception e) {
            log.error("uploadTaskImage" + e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR);
        }
    }
}
