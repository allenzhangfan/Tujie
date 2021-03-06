package com.netposa.common.log;

public interface Parser<T> {

    String LINE_SEPARATOR = Constant.BR;

    Class<T> parseClassType();

    String parseString(T t);
}
