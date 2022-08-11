package com.zlh.he_ma_master.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lh
 */
@Component
public class NumberUtil {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 开始时间戳
     */
    public static final long BEGIN_TIMESTAMP = 1640995200L;

    /**
     * 序列号位数
     */
    public static final int COUNT_BITS = 32;

    private static final Pattern PHONE_PATTERN = Pattern.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[0-8])|(18[0-9]))\\d{8}$");

    public static boolean isPhone(String loginName){
        Matcher matcher = PHONE_PATTERN.matcher(loginName);
        return matcher.matches();
    }


    public String getOrderNo(){
        // 1.生成时间戳
        long nowSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;
        // 2.生成序列号
        // 2.1获取当前日期
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        Long increment = stringRedisTemplate.opsForValue().increment(RedisConstants.MALL_ORDER_KEY + date);
        stringRedisTemplate.expire(RedisConstants.MALL_ORDER_KEY + date, RedisConstants.INCR_ORDER_TTL, TimeUnit.HOURS);
        // 3.拼接返回
        return String.valueOf(timestamp << COUNT_BITS | increment);
    }


}
