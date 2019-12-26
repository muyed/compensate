package com.muye.compensate.constant;

/**
 * created by dahan at 2018/7/31
 */
public enum CompensateStatusEnum {

    WAIT(0), COMPENSATING(1), SUCCESSFUL(2), FAILED(3)
    ;

    private int status;

    CompensateStatusEnum(int status){
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
