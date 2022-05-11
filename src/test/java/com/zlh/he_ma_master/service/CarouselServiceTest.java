package com.zlh.he_ma_master.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlh.he_ma_master.api.mall.vo.MallIndexCategoryVO;
import com.zlh.he_ma_master.entity.Carousel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.validation.executable.ValidateOnExecution;
import java.util.List;

@SpringBootTest
public class CarouselServiceTest {

    @Resource
    private CarouselService carouselService;

    @Resource
    private GoodsCategoryService goodsCategoryService;

    @Test
    void testPage(){
        Page<Carousel> carouselPage = carouselService.getCarouselPage(1, 10);
        System.out.println(carouselPage);
    }
}
