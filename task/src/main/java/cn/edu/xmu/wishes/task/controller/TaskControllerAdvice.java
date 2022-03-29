package cn.edu.xmu.wishes.task.controller;

import cn.edu.xmu.wishes.core.util.Common;
import cn.edu.xmu.wishes.core.util.ReturnNo;
import cn.edu.xmu.wishes.core.util.ReturnObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class TaskControllerAdvice {
    @ExceptionHandler(value = Exception.class)
    public Object handleException(Exception e) {
        log.error(e.getStackTrace()[0] + "\n" + e.getMessage());
        return Common.decorateReturnObject(new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR));
    }
}
