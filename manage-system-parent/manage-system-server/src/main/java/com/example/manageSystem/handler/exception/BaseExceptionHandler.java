package com.example.manageSystem.handler.exception;

import com.example.common.entity.response.Result;
import com.example.common.entity.response.StatusCode;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@ControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        if(e instanceof UnauthorizedException){
            throw new RuntimeException("权限不足！");
        }
        return new Result(false, StatusCode.ERROR, e.getMessage());
    }
}
