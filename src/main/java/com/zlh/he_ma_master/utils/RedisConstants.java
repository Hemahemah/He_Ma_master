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
}
