package com.zlh.he_ma_master;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import javax.annotation.Resource;

@Slf4j
@SpringBootTest
class HeMaMasterApplicationTests {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void contextLoads() {
        stringRedisTemplate.opsForValue().set("test", "connect");
        String test = stringRedisTemplate.opsForValue().get("test");
        System.out.println(test);
    }

}
