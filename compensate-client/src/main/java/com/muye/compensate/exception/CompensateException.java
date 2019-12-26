package com.muye.compensate.exception;

public class CompensateException extends RuntimeException {

    private String msg;
    private String code;

    public CompensateException(){
        super();
    }

    public CompensateException(String msg){
        super(msg);
        this.msg = msg;
    }

    public CompensateException(String code, String msg){
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public CompensateException(String code, String msg, Throwable e){
        super(msg, e);
        this.code = code;
        this.msg = msg;
    }

    public CompensateException(String msg, Throwable e){
        super(msg, e);
        this.msg = msg;
    }

    public CompensateException(Throwable e){
        super(e);
    }

    public String getMsg() {
        return msg;
    }

    public String getCode() {
        return code;
    }
}
