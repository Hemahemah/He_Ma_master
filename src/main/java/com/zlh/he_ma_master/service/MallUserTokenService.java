package com.zlh.he_ma_master.service;

import com.zlh.he_ma_master.entity.MallUserToken;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author lh
* @createDate 2022-04-05 19:46:19
*/
public interface MallUserTokenService extends IService<MallUserToken> {

    /**
     * 登出
     * @param userId 用户编号
     * @param token token
     * @return true 删除成功
     */
    boolean logout(Long userId, String token);
}
