package com.zlh.he_ma_master.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlh.he_ma_master.api.mall.param.MallUserRegisterParam;
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

    /**
     * 注册
     * @param registerParam 用户注册参数
     * @return true 注册成功
     */
    boolean register(MallUserRegisterParam registerParam);

    /**
     * 后台获取用户信息
     * @param pageNumber 页码
     * @param pageSize 分页大小
     * @return 用户列表
     */
    Page<MallUser> getUsersList(Integer pageNumber, Integer pageSize);

    /**
     * 修改用户状态
     * @param lockStatus 用户状态
     * @param ids 用户编号
     * @return
     */
    boolean updateUserStatus(Integer lockStatus, Long[] ids);
}
