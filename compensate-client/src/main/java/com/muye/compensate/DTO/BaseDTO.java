package com.muye.compensate.DTO;

import java.io.Serializable;

/**
 *
 * @author qinshuangping
 */
public class BaseDTO implements Serializable {
    private static final long serialVersionUID = -7779110672848186411L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 扩展属性
     * 存储为json串
     */
    private String extAtt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExtAtt() {
        return extAtt;
    }

    public void setExtAtt(String extAtt) {
        this.extAtt = extAtt;
    }
}
