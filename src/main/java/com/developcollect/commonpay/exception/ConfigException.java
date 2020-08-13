package com.developcollect.commonpay.exception;

/**
 * 配置异常
 * @author zak
 * @since 1.0.0
 */
public class ConfigException extends AbstractSimpleParameterRuntimeException {

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigException(Throwable cause) {
        super(cause);
    }

    public ConfigException(String format, Object... params) {
        super(format, params);
    }


}
