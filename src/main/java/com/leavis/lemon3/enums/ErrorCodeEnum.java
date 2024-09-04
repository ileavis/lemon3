package com.leavis.lemon3.enums;


import com.leavis.lemon3.exception.BaseError;

/**
 * @Author: paynejlli
 * @Description: 错误码类demo，业务应该自己定义自己的错误吗
 * @Date: 2024/8/8 17:01
 */
public enum ErrorCodeEnum implements BaseError {
    // 错误码定义规则，前三位为系统模块，后 5 位为数字错误码
    SUCCESS("SYS00000", "交易成功"),
    FAILED("SYS00001", "交易失败"),
    PARAMETER_ERROR("SYS00002", "交易失败，输入参数错误"),
    ;

    /**
     * 错误码
     */
    private final String code;

    /**
     * 错误描述信息
     */
    private final String msg;


    ErrorCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
