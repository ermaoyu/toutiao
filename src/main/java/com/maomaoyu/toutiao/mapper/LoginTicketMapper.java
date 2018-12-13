package com.maomaoyu.toutiao.mapper;

import com.maomaoyu.toutiao.bean.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * maomaoyu    2018/12/5_16:17
 **/
@Mapper
public interface LoginTicketMapper {
    String TABLE_NAME = "login_ticket";
    String INSERT_FIELDS = "user_id,expired,status,ticket";
    String SELECT_FIELDS = "id,"+INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME," (",INSERT_FIELDS,") values(#{userId},#{expired},#{status},#{ticket})"})
    int addTicket(LoginTicket ticket);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where status=0 and ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Update({"update ",TABLE_NAME," set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket") String ticket, @Param("status") int status);


}
