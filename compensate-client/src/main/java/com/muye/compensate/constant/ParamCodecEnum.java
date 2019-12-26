package com.muye.compensate.constant;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * created by dahan at 2018/8/2
 */
public enum ParamCodecEnum {

    JAVA(0),
    ;

    private Integer code;

    ParamCodecEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static ParamCodecEnum getByCode(Integer code){
        Optional<ParamCodecEnum> optional = Stream.of(ParamCodecEnum.values())
                .filter(e -> e.getCode().equals(code))
                .findFirst();

        if (optional.isPresent()) return optional.get();

        return null;
    }
}
