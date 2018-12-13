package com.maomaoyu.toutiao.service;

import com.maomaoyu.toutiao.bean.News;
import com.maomaoyu.toutiao.mapper.NewsMapper;
import com.maomaoyu.toutiao.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {

    @Autowired
    private NewsMapper newsMapper;

    public List<News> getLatestNews(int userId,int offset,int limit){
        return newsMapper.selectByUserIdAndOffset(userId,offset,limit);
    }

    public int addNews(News news){
        return newsMapper.addNews(news);
    }

    public News getById(int newsId){
        return newsMapper.getById(newsId);
    }

    public String saveImage(MultipartFile file) throws IOException {
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if (dotPos < 0){
            return null;
        }
        String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
        if (!ToutiaoUtil.isFileAllowed(fileExt)){
            return null;
        }
        String fileName = UUID.randomUUID().toString().replaceAll("-","") + "." + fileExt;
        //String fileName = new Date().getTime();
        Files.copy(file.getInputStream(),new File(ToutiaoUtil.IMAGE_DIR + fileName).toPath(),StandardCopyOption.REPLACE_EXISTING);
        return ToutiaoUtil.TOUTIAO_DOMAIN + "image?name=" + fileName;
    }

    public int updateCommentCount(int id,int count){
        return newsMapper.updateCommentCount(id,count);
    }

    public int updateLikeCount(int newsId,int count){
        return newsMapper.updateLikeCount(newsId,count);
    }
}
