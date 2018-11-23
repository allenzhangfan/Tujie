package com.netposa.common.exception;

/**
 * Created by yexiaokang on 2018/10/10.
 */
@SuppressWarnings("unused")
public class NullMessageException extends Exception {

    public NullMessageException() {
    }

    public NullMessageException(String message) {
        super(message);
    }

    public NullMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullMessageException(Throwable cause) {
        super(cause);
    }
}
