package com.maomaoyu.toutiao.controller;

import com.maomaoyu.toutiao.bean.*;
import com.maomaoyu.toutiao.mapper.CommentMapper;
import com.maomaoyu.toutiao.service.CommentService;
import com.maomaoyu.toutiao.service.LikeService;
import com.maomaoyu.toutiao.service.NewsService;
import com.maomaoyu.toutiao.service.UserService;
import com.maomaoyu.toutiao.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * maomaoyu    2018/12/6_23:26
 **/

@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @Autowired
    LikeService likeService;

    @RequestMapping(path = {"/news/{newsId}"},method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model){
        try {
            News news = newsService.getById(newsId);
            if (news != null) {
                int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
                if (localUserId != 0) {
                    model.addAttribute("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
                } else {
                    model.addAttribute("like", 0);
                }
                //添加评论
                List<Comment> comments = commentService.getCommentsByEntity(news.getId(), EntityType.ENTITY_NEWS);
                List<ViewObject> commentVOs = new ArrayList<>();
                for (Comment comment : comments) {
                    ViewObject commentVO = new ViewObject();
                    commentVO.set("comment", comment);
                    commentVO.set("user", userService.getUser(comment.getUserId()));
                    commentVOs.add(commentVO);
                }
                model.addAttribute("comments",commentVOs);
            }
            //返回质询详情
                model.addAttribute("news",news);
                model.addAttribute("owner",userService.getUser(news.getUserId()));

        } catch (Exception e) {
            logger.error("获取资讯详情失败 " + e.getMessage());
        }
        return "detail";
    }

    @RequestMapping(path = {"/image"},method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName, HttpServletResponse response){
        response.setContentType("image/jpeg");
        try {
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR + imageName)),response.getOutputStream());
        } catch (IOException e) {
            logger.error("读取图片错误" + imageName + e.getMessage());
        }
    }

    @RequestMapping(path = {"/uploadImage/"},method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file){
        try {
            String fileUrl = newsService.saveImage(file);
            if (fileUrl == null){
                return ToutiaoUtil.getJSONString(1,"上传图片失败!!!");
            }
            return ToutiaoUtil.getJSONString(0,fileUrl);
        } catch (IOException e) {
           logger.error("上传图片失败  " + e.getMessage());
           return  ToutiaoUtil.getJSONString(1,"上传失败");
        }
    }

    @RequestMapping(path = {"/user/addNews/"},method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title")String title,
                          @RequestParam("link")String link){

        try {
            News news = new News();
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setImage(image);
            news.setLink(link);
            if (hostHolder.getUser() != null){
                news.setUserId(hostHolder.getUser().getId());
            }else {
                //设置一个匿名用户
                news.setUserId(11);
            }
            newsService.addNews(news);
            return ToutiaoUtil.getJSONString(0);
        } catch (Exception e) {
            logger.error("添加资讯失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1,"发布失败");
        }
    }

    @RequestMapping(path = {"/addComment"},method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,@RequestParam("content") String content){
        try {
            Comment comment = new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            commentService.addComment(comment);

            //更新评论数量,以后用异步实现
            int count = commentService.getCommentCount(comment.getEntityId(),EntityType.ENTITY_NEWS);
//            System.out.println(count);
            newsService.updateCommentCount(comment.getEntityId(),count);
//            System.out.println(11111);
        } catch (Exception e) {
//            System.out.println(2222);
            logger.error("提交评论错误" + e.getMessage());
        }
        return "redirect:/news/"+String.valueOf(newsId);
    }
}
