package com.netposa.common.entity;

import static com.netposa.common.net.HttpConstant.IS_INVALID;
import static com.netposa.common.net.HttpConstant.IS_KICK_OFF;
import static com.netposa.common.net.HttpConstant.IS_OUT_OF_DATE;
import static com.netposa.common.net.HttpConstant.IS_PERMISSION_DENIED;
import static com.netposa.common.net.HttpConstant.IS_SUCCESS;

@SuppressWarnings({"WeakerAccess", "unused"})
public class HttpResponseEntity<T> {

    public int code;
    public String message;
    public T data;

    public boolean isSuccess() {
        return code == IS_SUCCESS;
    }

    public boolean isPermissionDenied() {
        return code == IS_PERMISSION_DENIED;
    }

    public boolean isInvalid() {
        return code == IS_INVALID;
    }

    public boolean isOutOfDate() {
        return code == IS_OUT_OF_DATE;
    }

    public boolean isKickOff() {
        return code == IS_KICK_OFF;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HttpResponseEntity{");
        sb.append("code=").append(code);
        sb.append(", message='").append(message).append('\'');
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
