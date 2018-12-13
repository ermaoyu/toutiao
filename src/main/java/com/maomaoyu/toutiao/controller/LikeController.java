package com.maomaoyu.toutiao.controller;

import com.maomaoyu.toutiao.async.EventModel;
import com.maomaoyu.toutiao.async.EventProducer;
import com.maomaoyu.toutiao.async.EventType;
import com.maomaoyu.toutiao.bean.EntityType;
import com.maomaoyu.toutiao.bean.HostHolder;
import com.maomaoyu.toutiao.bean.News;
import com.maomaoyu.toutiao.service.LikeService;
import com.maomaoyu.toutiao.service.NewsService;
import com.maomaoyu.toutiao.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * maomaoyu    2018/12/8_23:45
 **/
@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private NewsService newsService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/like"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("newsId") int newsId){
        long likeCount = likeService.like(hostHolder.getUser().getId(),EntityType.ENTITY_NEWS,newsId);
        //更新喜欢数
        News news = newsService.getById(newsId);
        newsService.updateLikeCount(newsId,(int) likeCount);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setEntityOwnerId(news.getUserId())
                .setActorId(hostHolder.getUser().getId()).setEntityId(newsId));
        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String disLike(@RequestParam("newsId") int newsId){
        long likeCount = likeService.disLike(hostHolder.getUser().getId(),EntityType.ENTITY_NEWS,newsId);
        //更新喜欢数
        newsService.updateLikeCount(newsId,(int)likeCount);
        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }
}
