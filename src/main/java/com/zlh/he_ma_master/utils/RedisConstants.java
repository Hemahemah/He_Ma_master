package com.zlh.he_ma_master.utils;

/**
 * redis配置
 * @author lh
 */
public class RedisConstants {

    /**
     * 用户登录前缀
     */
    public static final String LOGIN_USER_KEY = "he_ma:login:token:";

    /**
     * token过期时间 72小时
     */
    public static final Long LOGIN_USER_TTL = 72L;

    /**
     * 订单Id前缀
     */
    public static final String MALL_ORDER_KEY = "he_ma:icr:order:";

    /**
     * 订单Id过期时间 30天
     */
    public static final Long INCR_ORDER_TTL = 24 * 30L;

    /**
     * 商品主页信息过期时间 36小时
     */
    public static final Long MALL_INDEX_TTL = 36L;

    /**
     * 商城主页信息key
     */
    public static final String MALL_INDEX_KEY = "he_ma:index:info";



}
