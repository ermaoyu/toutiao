package com.maomaoyu.toutiao.service;

import com.maomaoyu.toutiao.bean.LoginTicket;
import com.maomaoyu.toutiao.bean.User;
import com.maomaoyu.toutiao.mapper.LoginTicketMapper;
import com.maomaoyu.toutiao.mapper.UserMapper;
import com.maomaoyu.toutiao.util.ToutiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper ticketMapper;

    public User getUser(int id){
        return userMapper.selectById(id);
    }

    public Map<String,Object> register(String username,String password,String email){
        Map<String,Object> map = new HashMap<>();
        if (StringUtils.isBlank(username)){
            map.put("msgname","用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("msgpwd","密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("msgemail","email不能为空");
            return map;
        }
        User user = userMapper.selectByName(username);
        if (user != null){
            map.put("msgname","用户名已经被注册");
            return map;
        }

        //密码强度
        user = new User();
        user.setName(username);
        user.setEmail(email);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        userMapper.addUser(user);

        //登录
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public Map<String,Object> login(String username,String password){
        Map<String ,Object> map = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            map.put("msgname", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msgpwd", "密码不能为空");
            return map;
        }

        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("msgname", "用户名不存在");
            return map;
        }

        if (!ToutiaoUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msgpwd","密码不正确");
            return map;
        }

        map.put("userId",user.getId());
        map.put("email",user.getEmail());

        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }
    private String addLoginTicket(int userId){
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24);
        ticket.setExpired(date);//过期时间
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        ticketMapper.addTicket(ticket);
        return ticket.getTicket();
    }

    public void logout(String ticket){
        ticketMapper.updateStatus(ticket,1);
    }
}
