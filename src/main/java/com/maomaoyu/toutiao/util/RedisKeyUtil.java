package com.maomaoyu.toutiao.util;

/**
 * maomaoyu    2018/12/8_23:21
 **/
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENT = "EVENT";
    private static String BIZ_KAP = "KAP";

    public static String getEventQueueKey(){
        return BIZ_EVENT;
    }

    public static String getKapKey(String email){
        return BIZ_KAP + SPLIT + email;
    }

    /**
     *  当用户通过页面点赞时,
     *  将被点赞的资讯id和资讯类型包装成一个Key字段
     *  传给controller层,方便处理,存入Redis
     * */
    public static String getLikeKey(int entityId,int entityType){
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityId,int entityType){
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
}
