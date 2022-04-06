package com.zlh.he_ma_master.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlh.he_ma_master.entity.MallUserToken;
import com.zlh.he_ma_master.service.MallUserTokenService;
import com.zlh.he_ma_master.dao.MallUserTokenMapper;
import org.springframework.stereotype.Service;

/**
* @author lh
* @createDate 2022-04-05 19:46:19
*/
@Service
public class MallUserTokenServiceImpl extends ServiceImpl<MallUserTokenMapper, MallUserToken>
    implements MallUserTokenService{

    @Override
    public boolean logout(Long userId) {
        UpdateWrapper<MallUserToken> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id",userId);
        return remove(updateWrapper);
    }
}




