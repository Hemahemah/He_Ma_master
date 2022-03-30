package com.zlh.he_ma_master.config.resolver;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zlh.he_ma_master.common.HeMaException;
import com.zlh.he_ma_master.common.ServiceResultEnum;
import com.zlh.he_ma_master.config.annotation.TokenToAdminUser;
import com.zlh.he_ma_master.dao.AdminUserTokenMapper;
import com.zlh.he_ma_master.entity.AdminUserToken;
import com.zlh.he_ma_master.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import javax.annotation.Resource;

@Component
public class TokenToAdminUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Resource
    private AdminUserTokenMapper adminUserTokenMapper;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TokenToAdminUser.class);
    }

    /**
     * TokenToAdminUser自动注入参数处理
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader("token");
        // 1. 是否有token参数
        if (StringUtils.hasText(token) && token.length() == Constants.TOKEN_LENGTH){
            QueryWrapper<AdminUserToken> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("token",token);
            AdminUserToken adminUserToken = adminUserTokenMapper.selectOne(queryWrapper);
            if (adminUserToken != null){
                if (adminUserToken.getExpireTime().getTime() > System.currentTimeMillis()){
                    return adminUserToken;
                }
                throw new HeMaException(ServiceResultEnum.ADMIN_EXPIRE_ERROR.getResult());
            }
        }
            throw new HeMaException(ServiceResultEnum.ADMIN_NOT_LOGIN_ERROR.getResult());
        }
}
