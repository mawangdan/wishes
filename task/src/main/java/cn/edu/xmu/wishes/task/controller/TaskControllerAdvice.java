package cn.edu.xmu.wishes.task.controller;

import cn.edu.xmu.wishes.core.util.Common;
import cn.edu.xmu.wishes.core.util.ReturnNo;
import cn.edu.xmu.wishes.core.util.ReturnObject;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TaskControllerAdvice {
    @ExceptionHandler(value = Exception.class)
    public Object handleException() {
        return Common.decorateReturnObject(new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR));
    }
}
