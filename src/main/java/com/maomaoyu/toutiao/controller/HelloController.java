package com.maomaoyu.toutiao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

    @RequestMapping(value = "/show")
    public String hello(){
        return "login";
    }

    @RequestMapping(value = "/reg")
    public String reg(){
        return "reg";
    }
}
