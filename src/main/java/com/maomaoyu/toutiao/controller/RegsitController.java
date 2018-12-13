package com.maomaoyu.toutiao.controller;

import com.maomaoyu.toutiao.async.EventModel;
import com.maomaoyu.toutiao.async.EventProducer;
import com.maomaoyu.toutiao.async.EventType;
import com.maomaoyu.toutiao.service.MessageService;
import com.maomaoyu.toutiao.service.RegService;
import com.maomaoyu.toutiao.service.UserService;
import com.maomaoyu.toutiao.util.MailSenderUtil;
import com.maomaoyu.toutiao.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * maomaoyu    2018/12/10_15:19
 **/
@Controller
public class RegsitController {
    private static  final Logger LOGGER = LoggerFactory.getLogger(RegsitController.class);

    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    RegService regService;

    @RequestMapping(path = {"/reg/"},method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reg(Model model,@RequestParam("email") String email,@RequestParam("userkap")String userkap, @RequestParam("username") String username, @RequestParam("password")String password, @RequestParam(value = "rememberme",defaultValue = "0")int rememberme, HttpServletResponse response){
        System.out.println(email + "  " + userkap);
        try {
            if (!regService.checkUserKap(email,userkap)){
                return ToutiaoUtil.getJSONString(0,"验证码错误");
            }
            Map<String,Object> map = userService.register(username,password,email);
            if (map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/index");
                if (rememberme > 0){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                eventProducer.fireEvent(new
                        EventModel(EventType.REGSUCCESS)
                        .setExt("username", username).setExt("to", email));
                return ToutiaoUtil.getJSONString(0,"注册成功");
            }else {
                return ToutiaoUtil.getJSONString(1,map);
            }
        } catch (Exception e) {
            LOGGER.error("注册异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1,"注册异常");
        }
    }

    @RequestMapping(path = {"/reg/kapther"},method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String kaptcher(@RequestParam("email") String email,@RequestParam("username") String username){
        String kap = verifyCode();//生成随机的kap并发送邮件给注册用户邮箱
        System.out.println(kap + "  " + email + "  " + username);
        if (email == null){
            return ToutiaoUtil.getJSONString(1,"用户邮箱不能为空!");
        }

        regService.saveRegKap(email,kap);
        //发送验证码到邮箱
        regService.sendUserKap(email,kap,username);
        return ToutiaoUtil.getJSONString(0,"发送成功!");
    }

    private static String verifyCode(){
        String str = "";
        char[] ch = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
                'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        Random random = new Random();
        for (int i = 0; i <6; i++){
            char num = ch[random.nextInt(ch.length)];
            str += num;
        }
        return str;
    }
}
