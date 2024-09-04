package com.leavis.lemon3.dto;

import com.leavis.lemon3.enums.ErrorCodeEnum;
import com.leavis.lemon3.exception.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author: paynejlli
 * @Description: 结果反回通用类
 * @Date: 2024/8/8 16:58
 */
@Schema(description = "结果反回通用类")
@Data
public class GenericRspDTO<T> implements BaseError {

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

    public static <T> GenericRspDTO<T> success(T data) {
        GenericRspDTO<T> genericRspDTO = new GenericRspDTO<>();
        genericRspDTO.setCode(ErrorCodeEnum.SUCCESS.getCode());
        genericRspDTO.setMsg(ErrorCodeEnum.SUCCESS.getMsg());
        genericRspDTO.setData(data);
        return genericRspDTO;
    }

    public static GenericRspDTO successNobody() {
        GenericRspDTO genericRspDTO = new GenericRspDTO<>();
        genericRspDTO.setCode(ErrorCodeEnum.SUCCESS.getCode());
        genericRspDTO.setMsg(ErrorCodeEnum.SUCCESS.getMsg());
        return genericRspDTO;
    }

    public static <T> GenericRspDTO<T> successPage(T data, Long total) {
        GenericRspDTO<T> genericRspDTO = new GenericRspDTO<>();
        genericRspDTO.setCode(ErrorCodeEnum.SUCCESS.getCode());
        genericRspDTO.setMsg(ErrorCodeEnum.SUCCESS.getMsg());
        genericRspDTO.setData(data);
        genericRspDTO.setTotal(total);
        return genericRspDTO;
    }

    public static <T> GenericRspDTO failed(String code, String message) {
        GenericRspDTO<T> genericRspDTO = new GenericRspDTO<>();
        genericRspDTO.setCode(code);
        genericRspDTO.setMsg(message);
        return genericRspDTO;
    }

    public static <T> GenericRspDTO failed(BaseError baseError) {
        GenericRspDTO<T> genericRspDTO = new GenericRspDTO<>();
        genericRspDTO.setCode(baseError.getCode());
        genericRspDTO.setMsg(baseError.getMsg());
        return genericRspDTO;
    }
}