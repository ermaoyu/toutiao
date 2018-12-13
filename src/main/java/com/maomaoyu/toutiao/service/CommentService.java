package com.maomaoyu.toutiao.service;

import com.maomaoyu.toutiao.bean.Comment;
import com.maomaoyu.toutiao.mapper.CommentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * maomaoyu    2018/12/7_23:17
 **/
@Service
public class CommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    CommentMapper commentMapper;

    public List<Comment> getCommentsByEntity(int entityId,int entityType){
        return commentMapper.selectByEntity(entityId,entityType);
    }

    public int addComment(Comment comment){
        return commentMapper.addComment(comment);
    }

    public int getCommentCount(int entityId,int entityType){
        return commentMapper.getCommentCount(entityId,entityType);
    }
}
