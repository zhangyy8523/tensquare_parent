package com.bwie.error;

import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @描述：
 * @作者：zhangyuyang
 * @日期：2020/5/8 14:32
 */
@ControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR,"系统异常，请联系管理员");
    }
}
