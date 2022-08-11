package com.zlh.he_ma_master.service.impl;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlh.he_ma_master.api.mall.param.MallUserRegisterParam;
import com.zlh.he_ma_master.api.mall.param.MallUserUpdateParam;
import com.zlh.he_ma_master.common.HeMaException;
import com.zlh.he_ma_master.common.ServiceResultEnum;
import com.zlh.he_ma_master.entity.MallUser;
import com.zlh.he_ma_master.service.MallUserService;
import com.zlh.he_ma_master.dao.MallUserMapper;
import com.zlh.he_ma_master.service.MallUserTokenService;
import com.zlh.he_ma_master.utils.EncryptUtil;
import com.zlh.he_ma_master.utils.RedisConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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

    @Resource
    private StringRedisTemplate stringRedisTemplate;

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
           // 3. 生成token
           String token = EncryptUtil.getToken(String.valueOf(user.getUserId()));
           String userStr = JSONUtil.toJsonStr(user);
           stringRedisTemplate.opsForValue().set(RedisConstants.LOGIN_USER_KEY + token, userStr, RedisConstants.LOGIN_USER_TTL, TimeUnit.HOURS);
           return token;
       }else {
           return ServiceResultEnum.LOGIN_ERROR.getResult();
       }
   }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateInfo(MallUserUpdateParam userUpdateParam, Long userId, String token) {
        // 1. 校验用户是否存在
        MallUser user = getById(userId);
        if (user == null){
            throw new HeMaException(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        // 2. 密码为空,说明用户不修改密码
        if (StringUtils.hasText(userUpdateParam.getPasswordMd5())){
            user.setPasswordMd5(userUpdateParam.getPasswordMd5());
            mallUserTokenService.logout(userId, token);
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

    @Override
    public Page<MallUser> getUsersList(Integer pageNumber, Integer pageSize) {
        return page(new Page<>(pageNumber, pageSize));
    }

    @Override
    public boolean updateUserStatus(Integer lockStatus, Long[] ids) {
        QueryWrapper<MallUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", Arrays.asList(ids));
        List<MallUser> mallUsers = list(queryWrapper);
        if (mallUsers.size() != ids.length){
            throw new HeMaException(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        mallUsers.forEach(mallUser -> mallUser.setLockedFlag(lockStatus));
        return updateBatchById(mallUsers);
    }
}




