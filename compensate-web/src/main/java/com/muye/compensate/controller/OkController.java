package com.muye.compensate.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * created by dahan at 2018/7/30
 */
@RestController
public class OkController {

    @RequestMapping("/ok")
    public String status(){
        return "ok";
    }
}
