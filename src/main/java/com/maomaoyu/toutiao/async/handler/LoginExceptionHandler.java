package com.maomaoyu.toutiao.async.handler;

import com.maomaoyu.toutiao.async.EventHandler;
import com.maomaoyu.toutiao.async.EventModel;
import com.maomaoyu.toutiao.async.EventType;
import com.maomaoyu.toutiao.bean.Message;
import com.maomaoyu.toutiao.service.MessageService;
import com.maomaoyu.toutiao.util.MailSenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * maomaoyu    2018/12/9_20:41
 **/
@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    MailSenderUtil mailSenderUtil;
    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setToId(model.getActorId());
        message.setContent("你上次的登录IP异常");

        message.setFromId(3);
        message.setCreatedDate(new Date());
        messageService.addMessage(message);
        Map<String, Object> map = new HashMap();
        map.put("username", model.getExt("username"));
        mailSenderUtil.sendWithHTMLTemplate(model.getExt("to"), "登陆异常",
                "mails/welcome.html", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
