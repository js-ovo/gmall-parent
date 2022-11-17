package com.jing.gmall.common.config.exception.handler;

import com.jing.gmall.common.execption.GmallException;
import com.jing.gmall.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *  全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    // 自定义异常处理器
    @ExceptionHandler(GmallException.class)
    public Result gmallExceptionHandler(GmallException e){
        Result<Object> fail = Result.fail();
        fail.setCode(e.getCode());
        fail.setMessage(e.getMessage());
        log.error("错误:{}",e);
        return fail;
    }


    @ExceptionHandler(Throwable.class)
    public Result exceptionHandler(Throwable e){
        Result<Object> fail = Result.fail();
        fail.setMessage(e.getMessage());
        log.error("服务器内部错误:{}",e);
        return fail;
    }
}
