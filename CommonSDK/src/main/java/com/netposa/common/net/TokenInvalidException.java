/*
 * Copyright (C) 2017 LingDaNet.Co.Ltd. All Rights Reserved.
 */

package com.netposa.common.net;

/**
 * 描述：token过期异常
 */

public class TokenInvalidException extends Exception {
    public TokenInvalidException(String msg) {
        super(msg);
    }
}
