package com.zlh.he_ma_master.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlh.he_ma_master.api.admin.param.AdminUpdateNameParam;
import com.zlh.he_ma_master.api.admin.param.AdminUpdatePasswordParam;
import com.zlh.he_ma_master.common.ServiceResultEnum;
import com.zlh.he_ma_master.dao.AdminUserTokenMapper;
import com.zlh.he_ma_master.entity.AdminUser;
import com.zlh.he_ma_master.entity.AdminUserToken;
import com.zlh.he_ma_master.service.AdminUserService;
import com.zlh.he_ma_master.dao.AdminUserMapper;
import com.zlh.he_ma_master.utils.EncryptUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;

/**
* 管理员服务模块
* @author lh
* @createDate 2022-03-20 22:58:22
*/
@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser>
    implements AdminUserService{

    @Resource
    private AdminUserTokenMapper adminUserTokenMapper;

    @Override
    public String login(String userName, String password) {
        // 1. 查询管理员信息
        QueryWrapper<AdminUser> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_account",userName).eq("user_password",password);
        AdminUser adminUser = getOne(userQueryWrapper);
        if (adminUser != null){
            // 2. 查询token
            QueryWrapper<AdminUserToken> tokenQueryWrapper = new QueryWrapper<>();
            tokenQueryWrapper.eq("user_id",adminUser.getUserId());
            AdminUserToken adminUserToken = adminUserTokenMapper.selectOne(tokenQueryWrapper);
            Date updateTime = new Date();
            Date expireTime = new Date(updateTime.getTime()+ 3 * 24 * 3600 * 1000);
            // 3. 如果有更新token,没有则生成token
            if (adminUserToken != null){
                adminUserToken.setUpdateTime(updateTime);
                adminUserToken.setExpireTime(expireTime);//72小时过期
                adminUserTokenMapper.updateById(adminUserToken);
                return adminUserToken.getToken();
            }else {
                String token = EncryptUtil.getToken(String.valueOf(adminUser.getUserId()));
                adminUserToken = new AdminUserToken();
                adminUserToken.setUserId(adminUser.getUserId());
                adminUserToken.setToken(token);
                adminUserToken.setUpdateTime(updateTime);
                adminUserToken.setExpireTime(expireTime);
                adminUserTokenMapper.insert(adminUserToken);
                return token;
            }
        }else {
            //返回失败信息
            return ServiceResultEnum.LOGIN_ERROR.getResult();
        }
    }

    @Override
    public boolean updateName(AdminUserToken adminUserToken, AdminUpdateNameParam adminUpdateNameParam) {
        AdminUser adminUser = getById(adminUserToken.getUserId());
        // 用户不为空
        if (adminUser != null){
            adminUser.setUserNickName(adminUpdateNameParam.getNickName());
            adminUser.setUserAccount(adminUpdateNameParam.getLoginName());
            adminUser.setUpdateTime(new Date());
            return updateById(adminUser);
        }
        return false;
    }

    @Override
    public boolean updatePassword(AdminUserToken adminUserToken, AdminUpdatePasswordParam passwordParam) {
        AdminUser adminUser = getById(adminUserToken.getUserId());
        // 用户不为空
        if (adminUser != null){
            // 校验密码
            if (passwordParam.getOriginalPassword().equals(adminUser.getUserPassword())){
                adminUser.setUserPassword(passwordParam.getNewPassword());
                adminUser.setUpdateTime(new Date());
                //修改密码并删除token数据
                return updateById(adminUser) && adminUserTokenMapper.deleteById(adminUserToken.getTokenId()) > 0;
            }
        }
        return false;
    }

    @Override
    public boolean logout(AdminUserToken adminUserToken) {
        return adminUserTokenMapper.deleteById(adminUserToken.getTokenId()) > 0;
    }

}




