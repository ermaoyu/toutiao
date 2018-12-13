package com.maomaoyu.toutiao.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * maomaoyu    2018/12/10_14:42
 **/
//@Controller
public class SendUtilTest {

//    @Autowired
    JavaMailSender jms;

//    @GetMapping("/send")
    public String send(){
        //建立邮件消息
        SimpleMailMessage mainMessage = new SimpleMailMessage();
        //发送者
        mainMessage.setFrom("928432997@qq.com");
        //接收者
        mainMessage.setTo("871365987@qq.com");
        //发送的标题
        mainMessage.setSubject("嗨喽");
        //发送的内容
        mainMessage.setText("hello world");
        jms.send(mainMessage);
        return "1";

    }

}
