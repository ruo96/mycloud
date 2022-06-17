package org.wrh.cloud.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.wrh.cloud.common.dto.ReturnResult;
import org.wrh.cloud.common.enums.ResultEnum;

/**
 * @author wuruohong
 * @date 2022-06-17 9:33
 * 这个用来处理程序内部的统一的错误异常
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    public ReturnResult hanldeException(Exception e) {
        log.error("[exceptionHandler]>>> e: {}",e.getMessage());
        return ReturnResult.fail(ResultEnum.SERVER_ERROR);
    }
}
