package com.muye.compensate.exception;

/**
 * created by dahan at 2018/7/5
 */
public class UnknownException extends CompensateException {

    public UnknownException(Throwable e){
        super("9999", "未知异常", e);
    }
}
