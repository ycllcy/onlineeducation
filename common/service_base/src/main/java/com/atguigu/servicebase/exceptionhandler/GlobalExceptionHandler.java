package com.atguigu.servicebase.exceptionhandler;


import com.atguigu.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author LcY
 * @create 2022-08-29 16:11
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public R error(Exception exception) {
//        exception.printStackTrace();
//        return R.error().message("执行了全局异常处理。。。");
//    }

    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public R error(ArithmeticException exception) {
        exception.printStackTrace();
        return R.error().message("执行了ArithmeticException异常处理。。。");
    }

    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public R error(GuliException exception) {
        log.error(exception.getMessage());
        exception.printStackTrace();
        return R.error().code(exception.getCode()).message(exception.getMsg());
    }
}
