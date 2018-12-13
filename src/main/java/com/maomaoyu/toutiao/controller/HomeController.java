package com.maomaoyu.toutiao.controller;

import com.maomaoyu.toutiao.bean.EntityType;
import com.maomaoyu.toutiao.bean.HostHolder;
import com.maomaoyu.toutiao.bean.News;
import com.maomaoyu.toutiao.bean.ViewObject;
import com.maomaoyu.toutiao.service.LikeService;
import com.maomaoyu.toutiao.service.NewsService;
import com.maomaoyu.toutiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private NewsService newsService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;


    private List<ViewObject> getNews(int usetId,int offset,int limit){
        List<News> newsList = newsService.getLatestNews(usetId,offset,limit);
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
//        System.out.println(localUserId);
        List<ViewObject> vos = new ArrayList<>();
        for (News news: newsList) {
            ViewObject vo = new ViewObject();
            vo.set("news",news);
            vo.set("user",userService.getUser(news.getUserId()));
            if (localUserId != 0){
                vo.set("like",likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS,news.getId()));
//                System.out.println(vo.get("like"));
            }else {
                vo.set("like",0);
            }
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path = {"/index","/"},method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model,@RequestParam(value = "page",defaultValue = "1") int page){
        model.addAttribute("vos",getNews(0,page,10));
        return "home";
    }

    @RequestMapping(path = {"/user/{userId}/"} ,method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model,@PathVariable int userId){
        model.addAttribute("vos",getNews(userId,0,10));
        return "home";
    }
}
