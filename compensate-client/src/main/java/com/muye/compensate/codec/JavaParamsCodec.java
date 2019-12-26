package com.muye.compensate.codec;

import com.muye.compensate.constant.ParamCodecEnum;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * created by dahan at 2018/8/1
 */
public class JavaParamsCodec implements ParamsCodec {

    @Override
    public Object[] decode(String binary) throws Exception{
        ObjectInputStream ois = null;
        ByteArrayInputStream bis = null;
        try {
            bis = new ByteArrayInputStream(Base64.decodeBase64(binary));
            ois = new ObjectInputStream(bis);
            return (Object[]) ois.readObject();
        } finally {
            try {
                if (ois != null) ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (bis != null) bis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String encode(Object[] params) throws Exception{
        ObjectOutputStream oos = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(params);
            byte[] bytes = bos.toByteArray();

            return Base64.encodeBase64String(bytes);
        } finally {
            try {
                if (oos != null) oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (bos != null) bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Integer getCode() {
        return ParamCodecEnum.JAVA.getCode();
    }
}
