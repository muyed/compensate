package com.muye.compensate.annotation;

import com.muye.compensate.codec.JavaParamsCodec;
import com.muye.compensate.codec.ParamsCodec;

import java.lang.annotation.*;

/**
 * created by dahan at 2018/7/31
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Compensate {
    /**
     * 最多重试次数 默认无限制
     */
    int maxNum() default Integer.MAX_VALUE;

    /**
     * 参数序列化协议
     */
    Class<? extends ParamsCodec> codec() default JavaParamsCodec.class;
}
