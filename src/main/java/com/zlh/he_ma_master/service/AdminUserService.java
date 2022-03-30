package com.zlh.he_ma_master.service;

import com.zlh.he_ma_master.api.admin.param.AdminUpdateNameParam;
import com.zlh.he_ma_master.api.admin.param.AdminUpdatePasswordParam;
import com.zlh.he_ma_master.entity.AdminUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zlh.he_ma_master.entity.AdminUserToken;

/**
 * 管理员服务模块
 * @author lh
 * @createDate 2022-03-20 22:58:22
 */
public interface AdminUserService extends IService<AdminUser> {

    /**
     * 登录
     * @param userName 管理员账号
     * @param password 密码(加密)
     * @return 返回token
     */
    String login(String userName, String password);

    /**
     * 修改名称
     * @param adminUserToken 用户token
     * @param adminUpdateNameParam 用户名称修改参数
     * @return true 修改成功
     */
    boolean updateName(AdminUserToken adminUserToken, AdminUpdateNameParam adminUpdateNameParam);

    /**
     * 修改密码
     * @param adminUserToken 用户token
     * @param passwordParam 用户名称修改参数
     * @return true 修改成功
     */
    boolean updatePassword(AdminUserToken adminUserToken, AdminUpdatePasswordParam passwordParam);

    /**
     * 注销
     * @param adminUserToken 用户token
     * @return true 注销成功
     */
    boolean logout(AdminUserToken adminUserToken);
}
