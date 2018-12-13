package com.maomaoyu.toutiao.service;

import com.maomaoyu.toutiao.bean.User;
import com.maomaoyu.toutiao.util.JedisAdapter;
import com.maomaoyu.toutiao.util.MailSenderUtil;
import com.maomaoyu.toutiao.util.RedisKeyUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * maomaoyu    2018/12/10_15:34
 **/
@Service
public class RegService {


    @Autowired
    MailSenderUtil mailSenderUtil;

    @Autowired
    private JedisAdapter jedisAdapter;

    public String saveRegKap(String email,String kap){
        //在喜欢集合里添加
        String kapKey = RedisKeyUtil.getKapKey(email);
        //存储到redis中并设置过期时间为3分钟
        return jedisAdapter.setex(kapKey,kap);
    }

    public boolean checkUserKap(String email,String kap){
        Map<String,Object> map = new HashMap<>();
            if (StringUtils.isBlank(email)){
                map.put("msgemail","用户邮箱不能为空");
                return false;
            }
            if (StringUtils.isBlank(kap)){
                map.put("msgkap","用户验证码不能为空");
                return false;
            }
            String kepKey = RedisKeyUtil.getKapKey(email);
            String checkKapKey = jedisAdapter.get(kepKey);
            if (!kap.equals(checkKapKey)){
                map.put("msgkap","验证码错误");
                return false;
            }
        return true;
    }

    public boolean sendUserKap(String email,String kap,String username){
        Map<String,Object> model = new HashMap<>();
        model.put("email",email);
        model.put("kap",kap);
        model.put("username",username);
        model.put("date",new Date());
        String subject = "Hello,您注册的验证为: " + kap;
        return mailSenderUtil.sendWithHTMLTemplate(email,subject,"mails/regkap.html",model);
    }
}
