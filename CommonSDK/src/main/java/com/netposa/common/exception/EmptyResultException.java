package com.netposa.common.exception;


@SuppressWarnings("unused")
public class EmptyResultException extends Exception {

    public EmptyResultException() {
    }

    public EmptyResultException(String message) {
        super(message);
    }

}
