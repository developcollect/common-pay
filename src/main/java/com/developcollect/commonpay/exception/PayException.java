package com.developcollect.commonpay.exception;

/**
 * 支付异常
 * @author zak
 * @since 1.0.0
 */
public class PayException extends AbstractSimpleParameterRuntimeException {

    public PayException() {
        super();
    }

    public PayException(String message) {
        super(message);
    }

    public PayException(String message, Throwable cause) {
        super(message, cause);
    }

    public PayException(Throwable cause) {
        super(cause);
    }

    public PayException(String format, Object... params) {
        super(format, params);
    }

}
