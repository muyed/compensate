package com.muye.compensate.util;

import com.muye.compensate.DTO.CompensateDTO;

/**
 * created by dahan at 2018/8/1
 */
public class ThreadLocalUtil {

    private static final ThreadLocal<CompensateDTO> THREAD_LOCAL = new ThreadLocal<>();

    public static void set(CompensateDTO compensateDTO){
        THREAD_LOCAL.set(compensateDTO);
    }

    public static CompensateDTO get(){
        return THREAD_LOCAL.get();
    }

    public static void remove(){
        THREAD_LOCAL.remove();
    }
}
