package com.maomaoyu.toutiao.util;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Random;

/**
 * maomaoyu    2018/12/9_21:20
 **/
@Component
public class MailSenderUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailSenderUtil.class);
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    public boolean sendWithHTMLTemplate(String to,String subject,String template,Map<String,Object> model){
        try {
            String nick = MimeUtility.encodeText("毛毛雨");
            InternetAddress form = new InternetAddress(nick + "<928432997@qq.com>");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            String res = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,template,"UTF-8",model);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom(form);
            mimeMessageHelper.setText(res,true);
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            LOGGER.error("发送邮件失败" + e.getMessage());
            return false;
        }
    }

//    @Override
//    public void afterPropertiesSet() throws Exception{
//        mailSender = new JavaMailSenderImpl();
//        //输入自己的邮箱和密码,用于发送邮件
//        mailSender.setUsername("928432997@qq.com");
//        mailSender.setPassword("wqfiwvhkcjirbbje");
//        mailSender.setHost("smtp.qq.com");
//        mailSender.setPort(465);
//        mailSender.setProtocol("smtps");
//        mailSender.setDefaultEncoding("utf8");
//        Properties javaMailProperties = new Properties();
//        javaMailProperties.put("mail.smtps.auth",true);
//        mailSender.setJavaMailProperties(javaMailProperties);
//    }
}
