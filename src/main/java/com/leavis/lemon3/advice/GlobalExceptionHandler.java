package com.leavis.lemon3.advice;

import com.leavis.lemon3.rsp.Result;
import com.leavis.lemon3.rsp.ResultCodeEnum;
import com.leavis.lemon3.exception.BizException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: paynejlli
 * @Description: 全局异常处理
 * @Date: 2024/8/20 15:12
 */
@RestControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler<ServiceException extends BizException> {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public Result<String> handleValidException(MethodArgumentNotValidException ex,
            HttpServletResponse httpServletResponse) {
        log.error("[GlobalExceptionHandler][handleValidException] 参数校验exception", ex);
        return wrapperBindingResult(ex.getBindingResult(), httpServletResponse);
    }

    private Result<String> wrapperBindingResult(BindingResult bindingResult,
            HttpServletResponse httpServletResponse) {
        StringBuilder errorMsg = new StringBuilder();
        for (ObjectError error : bindingResult.getAllErrors()) {
            if (error instanceof FieldError) {
                errorMsg.append(((FieldError) error).getField()).append(": ");
            }
            errorMsg.append(error.getDefaultMessage() == null ? "" : error.getDefaultMessage());

        }
        httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return Result.failed(ResultCodeEnum.FAILED.getCode(), errorMsg.toString());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BizException.class)
    public Result bizExceptionHandler(HttpServletRequest request, ServiceException exception) {
        // BizException属于可以预见的业务异常，使用warn进行打印。如果需要针对BizException建立告警，建议通过Code编码来进行
        log.error("服务出现异常  method={} requestURI={} code={} msg={}",
                request.getMethod(), request.getRequestURI(),
                exception.getCode(), exception.getMessage(), exception);
        return Result.failed(exception.getCode(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public Result unknownHandler(HttpServletRequest request, Exception exception) {
        if (exception instanceof ClientAbortException) {
            // ClientAbortException 没必要记录为ERROR日志
            log.error("get client abort exception, no need to be alert,", exception);
        } else {
            log.error("==>未知异常", exception);
            log.error("[exception:GlobalExceptionHandler]  method={} requestURI={} code={} msg={} ",
                    request.getMethod(), request.getRequestURI(),
                    ResultCodeEnum.FAILED.getCode(), ResultCodeEnum.FAILED.getMsg(), exception);
        }
        return Result.failed(ResultCodeEnum.FAILED.getCode(), ResultCodeEnum.FAILED.getMsg());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BindException.class)
    public Result bindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        return Result.failed(ResultCodeEnum.PARAMETER_ERROR.getCode(),
                Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }
}
