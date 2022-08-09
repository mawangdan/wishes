package cn.edu.xmu.plack.news.controller;

import cn.edu.xmu.plack.core.util.Common;
import cn.edu.xmu.plack.core.util.ReturnObject;
import cn.edu.xmu.plack.core.util.storage.StorageUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/news/file", produces = "application/json;charset=UTF-8")
@Slf4j
public class FileController {
    @PostMapping("")
    @ApiOperation(value = "文件上传")
    public Object uploadImage(@RequestParam(value = "file") MultipartFile file) throws Exception {
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
}
