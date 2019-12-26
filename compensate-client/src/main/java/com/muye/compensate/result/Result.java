package com.muye.compensate.result;

import com.muye.compensate.exception.CompensateException;
import com.muye.compensate.query.BaseQuery;
import com.muye.compensate.util.DateUtil;
import org.apache.commons.beanutils.PropertyUtilsBean;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 *
 *
 * @author shushuangshi
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 2994494589314183496L;

    private static final Pattern MOBILE_PATTERN = Pattern.compile("^1[3-9][0-9]{9}$");

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 查询信息
     */
    private BaseQuery query;

    /**
     * 错误编码
     */
    private String errCode;

    /**
     * 错误信息
     */
    private String errMsg;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BaseQuery getQuery() {
        return query;
    }

    public void setQuery(BaseQuery query) {
        this.query = query;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public static <T> Result ok(T data, BaseQuery query){
        Result<T> result = new Result<>();
        result.setData(data);
        result.setQuery(query);
        result.setSuccess(true);
        return result;
    }

    public static Result ok(){
        return ok(null);
    }

    public static <T> Result ok(T data){
        return ok(data, null);
    }

    public static <T> Result okOfFormat(T data, BaseQuery query){
        return ok(format(data, false), query);
    }

    public static <T> Result okOfFormat(T data){
        return okOfFormat(data, null);
    }

    public static <T> Result okOfFormat(T data, boolean desensitization, BaseQuery query){
        return ok(format(data, desensitization), query);
    }

    public static <T> Result okOfFormat(T data, boolean desensitization){
        return okOfFormat(data, desensitization, null);
    }

    public static Result fail(String errCode, String errMsg){
        Result result = new Result();
        result.setSuccess(false);
        result.setErrCode(errCode);
        result.setErrMsg(errMsg);
        return result;
    }

    public static Result fail(CompensateException e){
        return fail(e.getCode(), e.getMsg());
    }

    private static <T> Object format(T data, boolean desensitization) {
        if (data == null){
            return null;
        }

        //base dto
        if (data instanceof Result) {
            return dto2MapAndFormat((Result) data, desensitization);
        }

        //list
        if (data instanceof List && ((List) data).size() > 0) {
            List list = new ArrayList(((List) data).size());
            ((List) data).forEach(d -> {
                if (null == d){
                    list.add(d);
                }
                else if (d instanceof Result) {
                    list.add(dto2MapAndFormat((Result) d, desensitization));
                }
                else if (d instanceof List){
                    list.add(format(d, desensitization));
                }else {
                    list.add(d);
                }
            });
            return list;
        }

        return data;
    }

    private static Map dto2MapAndFormat(Result dto, boolean desensitization) {
        if (null == dto){
            return null;
        }

        Map<String, Object> map = new HashMap<>();
        PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
        PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(dto);
        Stream.of(descriptors)
                .map(descriptor -> descriptor.getName())
                .filter(name -> !"class".equals(name))
                .forEach(name -> {
                    try {
                        Object v = propertyUtilsBean.getNestedProperty(dto, name);
                        if (null == v){
                        }
                        //js中 数值大于9007199254740992会丢失精度
                        else if (v instanceof Long && (Long)v >= 9007199254740992l) {
                            v = v.toString();
                        }

                        else if (v instanceof BigDecimal) {
                            v = v.toString();
                        }

                        //手机号脱敏
                        else if("loginId".equals(name) || "mobile".equals(name)){
                            try {
                                String mobile = v.toString();
                                if (desensitization && MOBILE_PATTERN.matcher(mobile).matches()) {
                                    v = mobile.substring(0, 3) + "****" + mobile.substring(7);
                                }
                            } catch (Exception e) {
                            }
                        }

                        else if (v instanceof Date) {
                            v = DateUtil.format(v, DateUtil.DATE_PATTERN.YYYY_MM_DD_HH_MM_SS);
                        }
                        else if (v instanceof Result) {
                            v = dto2MapAndFormat((Result) v, desensitization);
                        }
                        else if (v instanceof List){
                            List tempList = new ArrayList();
                            Iterator iterator = ((List) v).iterator();
                            while (iterator.hasNext()){
                                Object item = iterator.next();
                                if (null != item && item instanceof Result) {
                                    iterator.remove();
                                    tempList.add(dto2MapAndFormat((Result)item, desensitization));
                                }
                            }
                            ((List) v).addAll(tempList);
                        }
                        map.put(name, v);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                });

        return map;
    }
}
