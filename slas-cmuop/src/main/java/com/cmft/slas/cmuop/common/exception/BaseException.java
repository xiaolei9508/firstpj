package com.cmft.slas.cmuop.common.exception;

/**
 * Created by chenxm on 2018/1/25.
 */
public class BaseException extends  RuntimeException {


    private static final long serialVersionUID = 3212950966960496693L;


    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(Throwable t) {
        super(t);
    }
}
