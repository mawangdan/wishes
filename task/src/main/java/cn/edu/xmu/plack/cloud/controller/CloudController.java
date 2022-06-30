package cn.edu.xmu.plack.cloud.controller;

import cn.edu.xmu.plack.cloud.model.OCRVo;
import cn.edu.xmu.plack.core.util.Common;
import cn.edu.xmu.plack.core.util.ReturnObject;
import cn.edu.xmu.plack.util.Transfer;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class CloudController {
    @Autowired
    Transfer transfer;

    @ApiOperation(value = "base64图片转文字")
    @PostMapping("/ocr")
    public Object ocr(@RequestBody OCRVo ocrVo
    ){
        return Common.decorateReturnObject(new ReturnObject(transfer.OCR(ocrVo.getBase64())));
    }
}
