package com.maomaoyu.toutiao.async.handler;

import com.maomaoyu.toutiao.async.EventHandler;
import com.maomaoyu.toutiao.async.EventModel;
import com.maomaoyu.toutiao.async.EventType;
import com.maomaoyu.toutiao.bean.Message;
import com.maomaoyu.toutiao.bean.User;
import com.maomaoyu.toutiao.service.MessageService;
import com.maomaoyu.toutiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * maomaoyu    2018/12/9_20:41
 **/
@Component
public class LikeHandler implements EventHandler {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        User user = userService.getUser(model.getActorId());
        message.setToId(model.getEntityOwnerId());
        message.setContent("用户" + model.getActorId() + "赞了你的资讯,http://127.0.0.1:8080/news/"
         + String.valueOf(model.getEntityId()));
        message.setFromId(3);
        message.setCreatedDate(new Date());
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
