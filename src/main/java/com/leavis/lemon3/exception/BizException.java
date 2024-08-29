package com.leavis.lemon3.exception;

import java.io.Serial;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: paynejlli
 * @Description: 统一业务异常
 * @Date: 2024/8/20 15:03
 */
@Getter
@Setter
public class BizException extends RuntimeException implements BaseError {

    @Serial
    private static final long serialVersionUID = 3866151654570261446L;
    /**
     * 错误码
     */
    protected String code;

    /**
     * 错误信息
     */
    protected String msg;

    public BizException() {
        super();
    }

    public BizException(String code, String msg) {
        super(String.format("Business exception occurred, error code: %s, error message: %s", code, msg));
        this.code = code;
        this.msg = msg;
    }

    public BizException(String code, String message, Throwable cause) {
        super(String.format("Business exception occurred, error code: %s, error message: %s", code, message), cause);
        this.code = code;
        this.msg = msg;
    }


    public BizException(BaseError baseError) {
        this(baseError.getCode(), baseError.getMsg());
    }

    public BizException(BaseError baseError, Throwable cause) {
        this(baseError.getCode(), baseError.getMsg(), cause);
    }


    public static void throwException(String code, String msg) {
        throw new BizException(code, msg);
    }

    public static void throwException(String code, String msg, Throwable cause) {
        throw new BizException(code, msg, cause);
    }

    public static void throwException(BaseError baseError) {
        throw new BizException(baseError);
    }

    public static void throwException(BaseError baseError, Throwable cause) {
        throw new BizException(baseError, cause);
    }
}
