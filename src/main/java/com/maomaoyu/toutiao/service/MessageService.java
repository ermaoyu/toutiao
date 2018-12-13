package com.maomaoyu.toutiao.service;

import com.maomaoyu.toutiao.bean.Message;
import com.maomaoyu.toutiao.mapper.MessageMapper;
import com.maomaoyu.toutiao.util.PageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * maomaoyu    2018/12/8_13:39
 **/
@Service
public class MessageService {
    //private static Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private MessageMapper messageMapper;

    public int addMessage(Message message){
        return messageMapper.addMessage(message);
    }

    public List<Message> getConversationList(int userId, int offset, int limit){
        // conversation的总条数存在id里
        return messageMapper.getConversationList(userId,(offset - 1) * limit,limit);
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit){
        return messageMapper.getConversationDetail(conversationId,(offset - 1) * limit ,limit);
    }

    public int getUnreadCount(int userId,String conversation){
        return messageMapper.getConversationUnReadCount(userId,conversation);
    }

    public int  getConversationDetailCount(String conversationId){
        return messageMapper.getConversationDetailCount(conversationId);
    }

    public int getConversationCount(int userId){
        return messageMapper.getConversationCount(userId);
    }

    public int readMessage(int id){
        return messageMapper.updataMessage(id);
    }



}
