package com.zlh.he_ma_master.service;

import com.zlh.he_ma_master.api.mall.param.MallUserUpdateParam;
import com.zlh.he_ma_master.entity.MallUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* 用户服务模块
* @author lh
* @createDate 2022-04-05 19:46:19
*/
public interface MallUserService extends IService<MallUser> {

    /**
     * 登录
     * @param loginName 用户名
     * @param passwordMd5 加密密码
     * @return 返回登录结果
     */
    String login(String loginName, String passwordMd5);

    /**
     * 修改用户信息
     * @param userUpdateParam 用户参数
     * @param userId 用户ID
     * @return true 修改成功
     */
    boolean updateInfo(MallUserUpdateParam userUpdateParam, Long userId);
}
