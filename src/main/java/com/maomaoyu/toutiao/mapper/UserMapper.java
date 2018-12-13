package com.maomaoyu.toutiao.mapper;

import com.maomaoyu.toutiao.bean.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    String TABLE_NAME = "user";
    String INSERT_FIELDS = "name,password,salt,head_url,state,email";
    String SELECT_FIELDS = "id,name,password,salt,head_url,state,email";

    @Insert({"insert into  ",TABLE_NAME," (",INSERT_FIELDS,") values(#{name},#{password},#{salt},#{headUrl},#{state},#{email}) "})
    void addUser(User user);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where id=#{id} and state=0"})
    User selectById(int id);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where name=#{name}"})
    User selectByName(String name);

    @Update({"update ",TABLE_NAME," set password=#{password} where id=#{id}"})
    void updatePassword(User user);

    @Update({"update ",TABLE_NAME," set state=0 where id=#{id}"})
    void deleteById(int id);

}
