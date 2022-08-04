package com.zlh.he_ma_master.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlh.he_ma_master.entity.MallUserToken;
import com.zlh.he_ma_master.service.MallUserTokenService;
import com.zlh.he_ma_master.dao.MallUserTokenMapper;
import com.zlh.he_ma_master.utils.RedisConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
* @author lh
* @createDate 2022-04-05 19:46:19
*/
@Service
public class MallUserTokenServiceImpl extends ServiceImpl<MallUserTokenMapper, MallUserToken>
    implements MallUserTokenService{

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean logout(Long userId, String token) {
        return Optional.ofNullable(stringRedisTemplate.delete(RedisConstants.LOGIN_USER_KEY + token)).orElse(false);
    }
}




