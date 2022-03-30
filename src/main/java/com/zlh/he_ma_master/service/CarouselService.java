package com.zlh.he_ma_master.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlh.he_ma_master.api.admin.param.CarouselAddParam;
import com.zlh.he_ma_master.api.admin.param.CarouselEditParam;
import com.zlh.he_ma_master.entity.Carousel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* 轮播图模块
* @author lh
* @createDate 2022-03-24 19:35:32
*/
public interface CarouselService extends IService<Carousel> {

    /**
     * 获取轮播图分页参数
     * @param pageNumber 当前页数
     * @param pageSize 分页大小
     * @return 分页参数
     */
     Page<Carousel> getCarouselPage(Integer pageNumber, Integer pageSize);

    /**
     * 添加轮播图
     * @param carouselAddParam 轮播图添加参数
     * @return true 插入成功
     */
    boolean saveCarousels(CarouselAddParam carouselAddParam);


    /**
     * 获取轮播图详情
     * @param id 轮播图编号
     * @return Carousel
     */
    Carousel getCarousel(Integer id);

    /**
     * 修改轮播图
     * @param carouselEditParam 轮播图修改参数
     * @return true 修改成功
     */
    boolean updateCarousel(CarouselEditParam carouselEditParam);

    /**
     * 删除轮播图
     * @param ids 轮播图编号
     * @return true 删除成功
     */
    boolean deleteCarousel(Long[] ids);
}
