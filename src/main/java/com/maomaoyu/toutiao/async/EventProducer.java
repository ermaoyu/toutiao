package com.maomaoyu.toutiao.async;

import com.alibaba.fastjson.JSONObject;
import com.maomaoyu.toutiao.util.JedisAdapter;
import com.maomaoyu.toutiao.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * maomaoyu    2018/12/9_20:28
 **/
@Service
public class EventProducer {

    @Autowired
    private JedisAdapter jedisAdapter;


    /**
     *  将页面传过来的操作添加到redis队列中,以json字符串存入,取出赋值时要用json解析
     * */
    public boolean fireEvent(EventModel eventModel){
        try{
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();

            jedisAdapter.lpush(key,json);//Redis单向/优先队列,实现异步功能
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
