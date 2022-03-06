package com.atguigu.servicebase.exception;

import com.atguigu.commonutils.ResultEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理
 */
@RestControllerAdvice
//@Slf4j
public class GlobalExceptionHandler {

    // 统一异常
    @ExceptionHandler(Exception.class)
    public ResultEntity error(Exception exception) {
        exception.printStackTrace();
        return ResultEntity.error().message("执行了全局异常处理");
    }

    // 特定异常
    @ExceptionHandler(ArithmeticException.class)
    public ResultEntity error(ArithmeticException arithmeticException) {
        arithmeticException.printStackTrace();
        return ResultEntity.error().message("执行了ArithmeticException异常处理");
    }

    // 自定义异常
    @ExceptionHandler(GuliException.class)
    public ResultEntity error(GuliException guliException) {
        //log.error(guliException.getMessage());
        guliException.printStackTrace();
        // 将异常信息设置到结果集中
        return ResultEntity.error().code(guliException.getCode()).message(guliException.getMsg());
    }

}
