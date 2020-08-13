package com.developcollect.commonpay.exception;

import cn.hutool.core.util.StrUtil;

/**
 * 异常基类
 * @author zak
 * @since 1.0.0
 */
public abstract class AbstractSimpleParameterRuntimeException extends RuntimeException {

    public AbstractSimpleParameterRuntimeException() {
        super();
    }

    public AbstractSimpleParameterRuntimeException(String message) {
        super(message);
    }

    public AbstractSimpleParameterRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractSimpleParameterRuntimeException(Throwable cause) {
        super(cause);
    }

    protected AbstractSimpleParameterRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public AbstractSimpleParameterRuntimeException(String format, Object... params) {
        super(StrUtil.format(format, params), mayThrowable(params));
    }

    private static Throwable mayThrowable(Object... params) {
        if (params.length > 0 && params[params.length - 1] instanceof Throwable) {
            return (Throwable) params[params.length - 1];
        }
        return null;
    }
}
