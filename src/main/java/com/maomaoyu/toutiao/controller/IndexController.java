package com.maomaoyu.toutiao.controller;

import com.maomaoyu.toutiao.bean.User;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class IndexController {

    @RequestMapping("/hello")
    @ResponseBody
    public String index(HttpSession session){
        return "Hello maomaoyu," + session.getAttribute("msg");
    }

    @RequestMapping("/profile/{groupId}/{userId}")
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId , @PathVariable("userId") int userId, @RequestParam(value = "type",defaultValue = "1") int type,@RequestParam(value = "key",defaultValue = "maomaoyu") String key){
        return String.format("GID{%s},UID{%d},TYPE{%d},KEY{%s}", groupId, userId, type, key);
    }

    @RequestMapping("/vm")
    public String index(Model model){
        model.addAttribute("value1","maomaoyu v1");
        List<String> colors = Arrays.asList(new String[]{"红","黄","蓝","绿"});

        Map<Integer,Integer> map = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            map.put(i,i*i);
        }
        model.addAttribute("colors",colors);
        model.addAttribute("map",map);
        model.addAttribute("user",new User("maomaoyu"));
        return "index";
    }

    @RequestMapping("/requset")
    @ResponseBody
    public String requset(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session){
        StringBuilder sb = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "</br>");
        }
        for (Cookie cookie: request.getCookies()) {
            sb.append("Cookie:");
            sb.append(cookie.getName());
            sb.append(":");
            sb.append(cookie.getValue());
            sb.append("</br>");
        }
        sb.append("getMethod:" + request.getMethod() + "<br>");
        sb.append("getPathInfo:" + request.getPathInfo() + "<br>");
        sb.append("getQueryString:" + request.getQueryString() + "<br>");
        sb.append("getRequestURI:" + request.getRequestURI() + "<br>");
        return sb.toString();
    }

    @RequestMapping("/response")
    @ResponseBody
    public String response(@CookieValue(value = "Id",defaultValue = "a") String Id,
                         @RequestParam(value = "key",defaultValue = "key") String key,
                         @RequestParam(value = "value",defaultValue = "value") String value,
                         HttpServletResponse response){
        response.addCookie(new Cookie(key,value));
        response.addHeader(key,value);
        return  "maomaoyu From Cookie:" + Id;
    }

    @RequestMapping("/redirect/{code}")
    public String redirect(@PathVariable("code") int code,HttpSession session){
        session.setAttribute("msg","Jump From redirect.");
        return "redirect:/";
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value = "key",required = false) String key){
        if ("admin".equals(key)){
            return "hello world";
        }
        throw new IllegalArgumentException("Key 错误");
    }
}
