package com.zlh.he_ma_master.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlh.he_ma_master.api.mall.param.MallUserRegisterParam;
import com.zlh.he_ma_master.api.mall.param.MallUserUpdateParam;
import com.zlh.he_ma_master.common.HeMaException;
import com.zlh.he_ma_master.common.ServiceResultEnum;
import com.zlh.he_ma_master.entity.MallUser;
import com.zlh.he_ma_master.entity.MallUserToken;
import com.zlh.he_ma_master.service.MallUserService;
import com.zlh.he_ma_master.dao.MallUserMapper;
import com.zlh.he_ma_master.service.MallUserTokenService;
import com.zlh.he_ma_master.utils.EncryptUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
* @author lh
* @createDate 2022-04-05 19:46:19
*/
@Service
public class MallUserServiceImpl extends ServiceImpl<MallUserMapper, MallUser>
    implements MallUserService{

    @Resource
    private MallUserTokenService mallUserTokenService;

    @Override
    public String login(String loginName, String passwordMd5) {
        // 1. 查询用户信息
        QueryWrapper<MallUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_name",loginName).eq("password_md5",passwordMd5);
        MallUser user = getOne(queryWrapper);
        if (user != null){
            // 2. 判断用户状态
            if (user.getLockedFlag() == 1){
                return ServiceResultEnum.LOGIN_USER_LOCKED_ERROR.getResult();
            }
            // 3. 获取用户token
            QueryWrapper<MallUserToken> tokenQueryWrapper = new QueryWrapper<>();
            tokenQueryWrapper.eq("user_id",user.getUserId());
            MallUserToken userToken = mallUserTokenService.getOne(tokenQueryWrapper);
            Date updateTime = new Date();
            //72小时过期
            Date expireTime = new Date(updateTime.getTime()+ 3 * 24 * 3600 * 1000);
            //如果token为空,创建token
            if (userToken == null){
                String newToken = EncryptUtil.getToken(String.valueOf(user.getUserId()));
                userToken = new MallUserToken();
                userToken.setToken(newToken);
                userToken.setUserId(user.getUserId());
            }
            userToken.setUpdateTime(updateTime);
            userToken.setExpireTime(expireTime);
            mallUserTokenService.saveOrUpdate(userToken);
            return userToken.getToken();
        }else {
            return ServiceResultEnum.LOGIN_ERROR.getResult();
        }
    }

    @Override
    public boolean updateInfo(MallUserUpdateParam userUpdateParam, Long userId) {
        // 1. 校验用户是否存在
        MallUser user = getById(userId);
        if (user == null){
            throw new HeMaException(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        // 2. 密码为空,说明用户不修改密码
        if (userUpdateParam.getPasswordMd5() != null){
            user.setPasswordMd5(userUpdateParam.getPasswordMd5());
        }
        user.setIntroduceSign(userUpdateParam.getIntroduceSign());
        user.setNickName(userUpdateParam.getNickName());
        return updateById(user);
    }

    @Override
    public boolean register(MallUserRegisterParam registerParam) {
        // 1. 查询用户名是否重复
        MallUser mallUser = getOne(new QueryWrapper<MallUser>().eq("login_name", registerParam.getLoginName()));
        if (mallUser != null){
            throw new HeMaException(ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult());
        }
        // 2. 设置默认简介与昵称
        mallUser = new MallUser();
        BeanUtils.copyProperties(registerParam, mallUser);
        mallUser.setNickName(registerParam.getLoginName());
        mallUser.setIntroduceSign(ServiceResultEnum.USER_INTRO.getResult());
        return save(mallUser);
    }


}




