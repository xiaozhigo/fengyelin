package com.example.fengcommon.exception;

import com.example.fengcommon.enums.EnumStatus;
import com.example.fengcommon.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 统一异常处理
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-10-02 21:26
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 统一 json 异常处理
     *
     * @param exception JsonException
     * @return 统一返回 json 格式
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response Exception(Exception exception){
        log.error("【Exception】:{}", exception.getMessage());
        return Response.error(EnumStatus.FAIL.getCode(), exception.getMessage());
    }


}
