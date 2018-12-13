package com.maomaoyu.toutiao.controller;

import com.maomaoyu.toutiao.async.EventModel;
import com.maomaoyu.toutiao.async.EventProducer;
import com.maomaoyu.toutiao.async.EventType;
import com.maomaoyu.toutiao.service.UserService;
import com.maomaoyu.toutiao.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * maomaoyu    2018/12/5_14:12
 **/
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);


    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;


    @RequestMapping(path = {"/login/"},method ={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model,@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rememberme",defaultValue ="0") int rememberme,HttpServletResponse response){
        try {
            Map<String,Object> map = userService.login(username,password);
            if (map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme > 0){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
//                eventProducer.fireEvent(new
//                        EventModel(EventType.LOGIN).setActorId((int) map.get("userId"))
//                        .setExt("username", "牛客").setExt("to", String.valueOf(map.get("email"))));
                return ToutiaoUtil.getJSONString(0,"登录成功");
            }else {
                return ToutiaoUtil.getJSONString(1,map);
            }
        } catch (Exception e) {
            logger.error("登录异常 " + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "登录异常");
        }
    }
    @RequestMapping(path = {"/logout/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/index";
    }

//    @GetMapping("/send")
//    public String send(){
//        //建立邮箱消息
//        return mailSender.send();
//    }
}
