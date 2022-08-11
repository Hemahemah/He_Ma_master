package com.zlh.he_ma_master.job;

import cn.hutool.json.JSONUtil;
import com.zlh.he_ma_master.api.mall.vo.MallIndexInfoVO;
import com.zlh.he_ma_master.service.IndexConfigService;
import com.zlh.he_ma_master.utils.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author lh
 */
@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private IndexConfigService indexConfigService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Scheduled(cron = "0 59 23 * * *")
    public void doCache(){
        MallIndexInfoVO index = indexConfigService.getIndex();
        stringRedisTemplate.opsForValue().set(RedisConstants.MALL_INDEX_KEY, JSONUtil.toJsonStr(index), RedisConstants.MALL_INDEX_TTL, TimeUnit.HOURS);
        log.info("PreCacheJob:doCache");
    }
}
