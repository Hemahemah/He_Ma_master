package com.zlh.he_ma_master.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlh.he_ma_master.api.admin.param.CarouselAddParam;
import com.zlh.he_ma_master.api.admin.param.CarouselEditParam;
import com.zlh.he_ma_master.api.mall.vo.MallIndexCarouselVO;
import com.zlh.he_ma_master.api.mall.vo.MallIndexInfoVO;
import com.zlh.he_ma_master.common.HeMaException;
import com.zlh.he_ma_master.common.ServiceResultEnum;
import com.zlh.he_ma_master.entity.Carousel;
import com.zlh.he_ma_master.service.CarouselService;
import com.zlh.he_ma_master.dao.CarouselMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
* @author lh
* @createDate 2022-03-24 19:35:32
*/
@Service
public class CarouselServiceImpl extends ServiceImpl<CarouselMapper, Carousel>
    implements CarouselService{

    @Override
    public Page<Carousel> getCarouselPage(Integer pageNumber, Integer pageSize) {
        Page<Carousel> page = new Page<>(pageNumber,pageSize);
        QueryWrapper<Carousel> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("carousel_rank");
        return page(page,queryWrapper);
    }

    @Override
    public boolean saveCarousels(CarouselAddParam carouselAddParam) {
        Carousel carousel = new Carousel();
        carousel.setCarouselUrl(carouselAddParam.getCarouselUrl());
        carousel.setRedirectUrl(carouselAddParam.getRedirectUrl());
        carousel.setCarouselRank(carouselAddParam.getCarouselRank());
        carousel.setCreateTime(new Date());
        carousel.setUpdateTime(new Date());
        return save(carousel);
    }

    @Override
    public Carousel getCarousel(Integer id) {
        return getById(id);
    }

    @Override
    public boolean updateCarousel(CarouselEditParam carouselEditParam) {
        Carousel carousel = getById(carouselEditParam.getCarouselId());
        if (carousel != null){
            carousel.setCarouselUrl(carouselEditParam.getCarouselUrl());
            carousel.setRedirectUrl(carouselEditParam.getRedirectUrl());
            carousel.setCarouselRank(carouselEditParam.getCarouselRank());
            carousel.setUpdateTime(new Date());
            return updateById(carousel);
        }
        throw new HeMaException(ServiceResultEnum.MESSAGE_UPDATE_ERROR.getResult());
    }

    @Override
    public boolean deleteCarousel(Long[] ids) {
        return removeBatchByIds(Arrays.asList(ids));
    }

    @Override
    public List<MallIndexCarouselVO> getCarouselForIndex(int indexCarouselNumber) {
        List<MallIndexCarouselVO> mallIndexCarouselVos = new ArrayList<>(indexCarouselNumber);
        Page<Carousel> carouselPage = getCarouselPage(1, indexCarouselNumber);
        List<Carousel> records = carouselPage.getRecords();
        records.forEach(record->{
            MallIndexCarouselVO mallIndexCarouselVO = new MallIndexCarouselVO();
            BeanUtils.copyProperties(record, mallIndexCarouselVO);
            mallIndexCarouselVos.add(mallIndexCarouselVO);
        });
        return mallIndexCarouselVos;
    }
}




