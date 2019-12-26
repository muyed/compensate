package com.muye.compensate.codec;

import com.muye.compensate.constant.ParamCodecEnum;

/**
 * created by dahan at 2018/8/1
 *
 * 参数序列化接口
 */
public interface ParamsCodec {

    /**
     *
     * @param binary
     * @return
     */
    Object[] decode(String binary) throws Exception;

    /**
     *
     * @param params
     * @return
     */
    String encode(Object[] params) throws Exception;

    /**
     * @see ParamsCodec#getByCode(Integer)
     * @return
     */
    Integer getCode();

    /**
     *
     * @param code {@link com.muye.compensate.constant.ParamCodecEnum}
     * @return
     */
    static ParamsCodec getByCode(Integer code){

        ParamCodecEnum codecEnum = ParamCodecEnum.getByCode(code);

        switch (codecEnum) {
            case JAVA:
                return new JavaParamsCodec();
            default:
                return null;
        }
    }
}
