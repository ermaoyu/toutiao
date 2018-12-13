package com.maomaoyu.toutiao.async.handler;

import com.maomaoyu.toutiao.async.EventHandler;
import com.maomaoyu.toutiao.async.EventModel;
import com.maomaoyu.toutiao.async.EventType;
import com.maomaoyu.toutiao.util.MailSenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * maomaoyu    2018/12/10_16:46
 **/
@Component
public class RegSucHandler implements EventHandler {
    @Autowired
    MailSenderUtil mailSenderUtil;
    @Override
    public void doHandle(EventModel model) {
        Map<String, Object> map = new HashMap();
        map.put("username", model.getExt("username"));
        mailSenderUtil.sendWithHTMLTemplate(model.getExt("to"), "注册成功",
                "mails/welcome.html", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.REGSUCCESS);
    }
}
