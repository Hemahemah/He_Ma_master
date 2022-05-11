package com.zlh.he_ma_master.config.resolver;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zlh.he_ma_master.common.HeMaException;
import com.zlh.he_ma_master.common.ServiceResultEnum;
import com.zlh.he_ma_master.config.annotation.TokenToMallUser;
import com.zlh.he_ma_master.dao.MallUserMapper;
import com.zlh.he_ma_master.dao.MallUserTokenMapper;
import com.zlh.he_ma_master.entity.MallUser;
import com.zlh.he_ma_master.entity.MallUserToken;
import com.zlh.he_ma_master.utils.Constants;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import javax.annotation.Resource;

/**
 * @author lh
 */
@Component
public class TokenToMallUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Resource
    private MallUserTokenMapper mallUserTokenMapper;

    @Resource
    private MallUserMapper mallUserMapper;

    public TokenToMallUserMethodArgumentResolver() {
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TokenToMallUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String token = webRequest.getHeader("token");
        // 1. 是否有token参数
        if (StringUtils.hasText(token) && token.length() == Constants.TOKEN_LENGTH){
            QueryWrapper<MallUserToken> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("token",token);
            MallUserToken userToken = mallUserTokenMapper.selectOne(queryWrapper);
            if (userToken == null || userToken.getExpireTime().getTime() < System.currentTimeMillis()){
                throw new HeMaException(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
            }
            // 2. 查询用户
            MallUser mallUser = mallUserMapper.selectById(userToken.getUserId());
            // 3. 校验用户状态
            if (mallUser.getLockedFlag() == 1){
                throw new HeMaException(ServiceResultEnum.LOGIN_USER_LOCKED_ERROR.getResult());
            }
            return mallUser;
        }else {
            throw new HeMaException(ServiceResultEnum.MALL_USER_NOT_LOGIN_ERROR.getResult());
        }
    }
}
