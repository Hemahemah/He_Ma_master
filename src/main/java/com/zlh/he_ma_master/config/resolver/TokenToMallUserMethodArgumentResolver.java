package com.zlh.he_ma_master.config.resolver;

import cn.hutool.json.JSONUtil;
import com.zlh.he_ma_master.common.HeMaException;
import com.zlh.he_ma_master.common.ServiceResultEnum;
import com.zlh.he_ma_master.config.annotation.TokenToMallUser;
import com.zlh.he_ma_master.entity.MallUser;
import com.zlh.he_ma_master.utils.ThreadUser;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author lh
 */
@Component
public class TokenToMallUserMethodArgumentResolver implements HandlerMethodArgumentResolver {


    public TokenToMallUserMethodArgumentResolver() {
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TokenToMallUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        MallUser mallUser = ThreadUser.getUser();
        // 1. 判断用户是否为空
        if (mallUser != null){
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
