package com.leavis.lemon3.utils;


import com.leavis.lemon3.exception.BaseError;

/**
 * @Author: paynejlli
 * @Description: 错误码验证器
 * @Date: 2024/8/20 11:17
 */
public class ErrorCodeValidator {

    /**
     * 判断给定的错误码是否表示成功。
     * 成功的标准是错误码为 8 位字符串，并且最后五位都是 "0"。
     *
     * @param errorCode 输入的错误码字符串
     * @return 如果错误码表示成功则返回 true，否则返回 false
     */
    public static boolean isSuccess(String errorCode) {
        // 检查错误码是否为 8 位字符串
        if (errorCode == null || errorCode.length() != 8) {
            return false;
        }

        // 检查最后五位是否都是 "0"
        String lastFiveDigits = errorCode.substring(3);
        return lastFiveDigits.equals("00000");
    }

    /**
     * 判断给定的错误基类是否表示成功。
     * 成功的标准是错误码为 8 位字符串，并且最后五位都是 "0"。
     *
     * @param baseError 错误基类
     * @return 如果错误码表示成功则返回 true，否则返回 false
     */
    public static boolean isSuccess(BaseError baseError) {
        return isSuccess(baseError.getCode());
    }

    /**
     * 判断给定的错误码是否表示失败。
     * 成功的标准是错误码为 8 位字符串，并且最后五位都是 "0"。
     *
     * @param errorCode 输入的错误码字符串
     * @return 如果错误码表示失败则返回 true，否则返回 false
     */
    public static boolean isFail(String errorCode) {
        return !isSuccess(errorCode);
    }

    /**
     * 判断给定的错误码是否表示失败。
     * 成功的标准是错误码为 8 位字符串，并且最后五位都是 "0"。
     *
     * @param baseError 错误基类
     * @return 如果错误码表示失败则返回 true，否则返回 false
     */
    public static boolean isFail(BaseError baseError) {
        return isFail(baseError.getCode());
    }
}