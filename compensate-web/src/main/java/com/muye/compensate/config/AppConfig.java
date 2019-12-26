package com.muye.compensate.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * created by dahan at 2018/7/30
 */
@SpringBootApplication(scanBasePackages = "com.muye.compensate.*")
public class AppConfig {

    public static void main(String[] args){
        new SpringApplication(AppConfig.class).run(args);
    }
}
