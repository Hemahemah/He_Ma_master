package com.zlh.he_ma_master.config;

import com.zlh.he_ma_master.config.resolver.TokenToAdminUserMethodArgumentResolver;
import com.zlh.he_ma_master.config.resolver.TokenToMallUserMethodArgumentResolver;
import com.zlh.he_ma_master.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lh
 */
@Configuration
public class HeMaWebMvcConfigurer implements WebMvcConfigurer {


    @Resource
    private TokenToAdminUserMethodArgumentResolver tokenToAdminUserMethodArgumentResolver;

    @Resource
    private TokenToMallUserMethodArgumentResolver tokenToMallUserMethodArgumentResolver;

    @Value("${constants.goodImgPath}")
    private String goodImgPath;

    @Value("${constants.filePath}")
    private String filePath;



    /**
     * 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOriginPatterns("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true).maxAge(3600);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(tokenToAdminUserMethodArgumentResolver);
        resolvers.add(tokenToMallUserMethodArgumentResolver);
    }

    /**
     * 文件资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/he_ma/goods-img/**").addResourceLocations("file:" + goodImgPath);
        registry.addResourceHandler("/images/**").addResourceLocations("file:" + filePath);
    }
}
