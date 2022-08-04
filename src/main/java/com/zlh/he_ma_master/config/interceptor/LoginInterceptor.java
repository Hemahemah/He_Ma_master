package com.zlh.he_ma_master.config.interceptor;

import cn.hutool.json.JSONUtil;
import com.zlh.he_ma_master.entity.MallUser;
import com.zlh.he_ma_master.utils.Constants;
import com.zlh.he_ma_master.utils.RedisConstants;
import com.zlh.he_ma_master.utils.ThreadUser;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 * @author lh
 */
public class LoginInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;

    public LoginInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token) || token.length() != Constants.TOKEN_LENGTH){
           return true;
        }
        // 2. 查询用户
        String userStr = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_USER_KEY + token);
        if (!StringUtils.hasText(userStr)){
            return true;
        }
        MallUser mallUser = JSONUtil.toBean(userStr, MallUser.class);
        ThreadUser.saveUser(mallUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadUser.removeUser();
    }
}
