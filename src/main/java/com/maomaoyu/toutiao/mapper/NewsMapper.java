package com.maomaoyu.toutiao.mapper;

import com.maomaoyu.toutiao.bean.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NewsMapper {

    String TABLE_NAME = "news";
    String INSERT_FIELDS = "title,link,image,like_count,comment_count,created_date,user_id,state";
    String SELECT_FIELDS = "id,"+INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME," (",INSERT_FIELDS,") values(#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId},#{state})"})
    int addNews(News news);

    List<News> selectByUserIdAndOffset(@Param("userId") int userId,@Param("offset")int offset,@Param("limit")int limit);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where id=#{newsId}"})
    News getById(int newsId);

    @Update({"update ",TABLE_NAME," set title=#{title},link=#{link},image=#{image},like_count=#{likeCount},comment_count=#{commentCount},created_date=#{createdDate},user_id=#{userId},state=#{state} where id=#{id}"})
    int updateNews(News news);

    @Update({"update ",TABLE_NAME," set comment_count =#{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id,@Param("commentCount") int commentCount);

    @Update({"update ",TABLE_NAME," set like_count=#{newLikeCount} where id=#{newsId}"})
    int updateLikeCount(@Param("newsId") int newsId,@Param("newLikeCount") int newLikeCount);
}
