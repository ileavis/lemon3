package com.leavis.lemon3.rsp;

import com.leavis.lemon3.exception.BaseError;
import com.leavis.lemon3.mdc.TraceIdUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.slf4j.MDC;

/**
 * @Author: paynejlli
 * @Description: 结果反回通用类
 * @Date: 2024/8/8 16:58
 */
@Schema(description = "结果反回通用类")
@Data
public class Result<T> implements BaseError {

    @Schema(description = "traceId")
    private String traceId;

    @Schema(description = "错误码")
    private String code;

    @Schema(description = "错误信息")
    private String msg;

    @Schema(description = "业务数据")
    private T data;

    @Schema(description = "分页数据时，返回总条数")
    private Long total;

    public Result() {
        this.traceId = MDC.get(TraceIdUtil.TRACE_ID);
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setMsg(ResultCodeEnum.SUCCESS.getMsg());
        result.setData(data);
        return result;
    }

    public static Result successNobody() {
        Result result = new Result<>();
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setMsg(ResultCodeEnum.SUCCESS.getMsg());
        return result;
    }

    public static <T> Result<T> successPage(T data, Long total) {
        Result<T> result = new Result<>();
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setMsg(ResultCodeEnum.SUCCESS.getMsg());
        result.setData(data);
        result.setTotal(total);
        return result;
    }

    public static <T> Result failed(String code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(message);
        return result;
    }

    public static boolean isSucceed(Result result) {
        return result.getCode().equals(ResultCodeEnum.SUCCESS.getCode());
    }
}