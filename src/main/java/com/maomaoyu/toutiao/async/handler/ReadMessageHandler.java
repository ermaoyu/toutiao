package com.maomaoyu.toutiao.async.handler;

import com.maomaoyu.toutiao.async.EventHandler;
import com.maomaoyu.toutiao.async.EventModel;
import com.maomaoyu.toutiao.async.EventType;
import com.maomaoyu.toutiao.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * maomaoyu    2018/12/12_22:35
 **/
@Component
public class ReadMessageHandler implements EventHandler {

    @Autowired
    private MessageService messageService;

    @Override
    public void doHandle(EventModel model) {
        int id = model.getActorId();
        messageService.readMessage(id);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.READMESSAGE);
    }
}
