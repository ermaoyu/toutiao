package com.maomaoyu.toutiao.bean;

import org.springframework.stereotype.Component;

/**
 * maomaoyu    2018/12/5_17:18
 **/
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<User>();


    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}
