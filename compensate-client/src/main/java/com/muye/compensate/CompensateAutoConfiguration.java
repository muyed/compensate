package com.muye.compensate;

import com.muye.compensate.aop.CompensateInterceptor;
import com.muye.compensate.work.CompensateWork;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * created by dahan at 2018/8/2
 */
@Configuration
@ConditionalOnClass({CompensateInterceptor.class, CompensateWork.class})
public class CompensateAutoConfiguration {

    @Bean
    public CompensateInterceptor compensateInterceptor(){
        return new CompensateInterceptor();
    }

    @Bean
    public CompensateWork compensateWork(){
        return new CompensateWork();
    }
}
