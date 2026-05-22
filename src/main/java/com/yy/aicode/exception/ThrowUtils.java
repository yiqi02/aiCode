package com.yy.aicode.exception;

/*
自动抛异常的工具类
 */
public class ThrowUtils {

    //根据条件抛出异常 条件成立则抛出异常
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if(condition) {
            throw runtimeException;
        }
    }

    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition,new BusinessException(errorCode));
    }

    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition,new BusinessException(errorCode,message));

    }
}
