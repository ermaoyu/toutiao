package com.maomaoyu.toutiao.mapper;

import com.maomaoyu.toutiao.bean.Message;
import javafx.scene.control.Tab;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * maomaoyu    2018/12/8_11:32
 **/
@Mapper
public interface MessageMapper {
    String TABLE_NAME = "message";
    String INSERT_FIELDS = "from_id,to_id,content,created_date,has_read,conversation_id";
    String SELECT_FIELDS = "id," + INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME," (",INSERT_FIELDS,") values(#{fromId},#{toId},#{content},#{createdDate},#{hasRead},#{conversationId})"})
    int addMessage(Message message);

    @Select({"select ", INSERT_FIELDS, " ,count(id) as id from ( select * from ", TABLE_NAME, " where from_id=#{userId} or to_id=#{userId} order by id desc) tt group by conversation_id order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,@Param("offset")int offset,@Param("limit") int limit);

    @Select({"select count(id) from (select count(id) as id from ",TABLE_NAME," where from_id =#{userId} or to_id=#{userId} group by conversation_id )ff"})
    int getConversationCount(@Param("userId") int userId);

    //未读信息
    @Select({"select count(id) from ",TABLE_NAME," where has_read = 0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnReadCount(@Param("userId") int userId,@Param("conversationId") String conversationId);

    @Select({"select count(id) from ",TABLE_NAME," where has_read = 0 and to_id=#{userId}"})
    int getConversationTotalCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where conversation_id=#{conversationId} order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId, @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select count(id) from ",TABLE_NAME," where conversation_id=#{conversationId} "})
    int getConversationDetailCount(String conversationId);

    @Update({"update ",TABLE_NAME," set has_read = 1 where id=#{id}"})
    int updataMessage(int id);
}
