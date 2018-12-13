package com.maomaoyu.toutiao.controller;

import com.maomaoyu.toutiao.async.EventModel;
import com.maomaoyu.toutiao.async.EventProducer;
import com.maomaoyu.toutiao.async.EventType;
import com.maomaoyu.toutiao.bean.*;
import com.maomaoyu.toutiao.service.LikeService;
import com.maomaoyu.toutiao.service.MessageService;
import com.maomaoyu.toutiao.service.UserService;
import com.maomaoyu.toutiao.util.PageBean;
import com.maomaoyu.toutiao.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * maomaoyu    2018/12/8_13:44
 **/
@Controller
public class MessageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    MessageService messageService;

    @RequestMapping(path = {"/msg/detail"},method = {RequestMethod.GET})
    public String conversationDetail(Model model,@RequestParam("conversationId") String conversationId,@RequestParam(value = "page" ,defaultValue = "1") int page,HttpServletRequest request){
        try {
            List<ViewObject> messages = new ArrayList<>();
            PageBean<ViewObject> pageBeanDetail = (PageBean<ViewObject>) request.getAttribute("pageBeanDetail");
            if (pageBeanDetail == null){
                pageBeanDetail = new PageBean<ViewObject>();
                pageBeanDetail.setTotalCount(messageService.getConversationDetailCount(conversationId));
            }
            if (page > 0 && page <= pageBeanDetail.getPageCount()){
                pageBeanDetail.setCurPage(page);
            }
            pageBeanDetail.setStrId(conversationId);
            List<Message> conversationList = messageService.getConversationDetail(pageBeanDetail.getStrId(), pageBeanDetail.getCurPage(), pageBeanDetail.getPageSize());
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("message", msg);
                User user = userService.getUser(msg.getFromId());
                if (user == null) {
                    continue;
                }
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userName", user.getName());
                eventProducer.fireEvent(new EventModel(EventType.READMESSAGE).setActorId(msg.getId()));
                messages.add(vo);
            }
            model.addAttribute("pageBeanDetail",pageBeanDetail);
            model.addAttribute("messages",messages);
            return "letterDetail";
        }catch (Exception e) {
            LOGGER.error("获取站内信失败" + e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path = {"/msg/list"},method = {RequestMethod.GET})
    public String conversationList(Model model,HttpServletRequest request,@RequestParam(value = "page",defaultValue = "1") int page){

        try {
            int localUserId = hostHolder.getUser().getId();
            PageBean<ViewObject> pageBeanList = (PageBean<ViewObject>) request.getAttribute("pageBeanList");
            if (pageBeanList == null){
                pageBeanList = new PageBean<ViewObject>();
                pageBeanList.setTotalCount(messageService.getConversationCount(localUserId));//总条数
            }
            if (page > 0 && page <= pageBeanList.getPageCount()){
                System.out.println(111111);
                pageBeanList.setCurPage(page);
            }
            pageBeanList.setId(localUserId);
            List<ViewObject> conversations = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationList(localUserId, pageBeanList.getCurPage(),pageBeanList.getPageSize());
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                User user = userService.getUser(targetId);
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userName", user.getName());
                vo.set("id", targetId);
                vo.set("totalCount", msg.getId());
                vo.set("unreadCount", messageService.getUnreadCount(localUserId, msg.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("pageBeanList",pageBeanList);
            model.addAttribute("conversations", conversations);
            return "letter";
        }catch (Exception e){
            LOGGER.error("获取站内信列表失败" + e.getMessage());
        }
        return "letter";
    }

//    @RequestMapping(path = {"/msg/list"},method = {RequestMethod.GET})
//    public String conversationList(Model model,@RequestParam("curPage") int curPage,  HttpServletRequest request){
//
//        try {
//            int localUserId = hostHolder.getUser().getId();
//            List<ViewObject> conversations = new ArrayList<>();
//            PageBean<ViewObject> pageBean = (PageBean<ViewObject>) request.getAttribute("pageBean");
//            pageBean.setCurPage(curPage > pageBean.getPageCount() ? pageBean.getCurPage() : curPage);
//            List<Message> conversationList = messageService.getConversationList(localUserId, curPage,pageBean.getPageSize());
//            for (Message msg : conversationList) {
//                ViewObject vo = new ViewObject();
//                vo.set("conversation", msg);
//                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
//                User user = userService.getUser(targetId);
//                vo.set("headUrl", user.getHeadUrl());
//                vo.set("userName", user.getName());
//                vo.set("id", targetId);
//                vo.set("totalCount", msg.getId());
//                vo.set("unreadCount", messageService.getUnreadCount(localUserId, msg.getConversationId()));
//                conversations.add(vo);
//            }
//            pageBean.setPageDate(conversations);
//            model.addAttribute("pageBean",pageBean);
//            model.addAttribute("conversations", conversations);
//            return "letter";
//        }catch (Exception e){
//            LOGGER.error("获取站内信列表失败" + e.getMessage());
//        }
//        return "letter";
//    }

    @RequestMapping(path = {"/msg/addMessage"},method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,@RequestParam("toId")int toId,@RequestParam("content")String content){
        Message msg = new Message();
        msg.setContent(content);
        msg.setCreatedDate(new Date());
        msg.setFromId(fromId);
        msg.setToId(toId);
        //msg.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) :
        //        String.format("%d_%d", toId, fromId));
        messageService.addMessage(msg);
        return ToutiaoUtil.getJSONString(msg.getId());
    }
}
