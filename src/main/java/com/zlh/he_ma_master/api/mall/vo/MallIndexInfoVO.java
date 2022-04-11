package com.zlh.he_ma_master.api.mall.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lh
 */
@Data
public class MallIndexInfoVO implements Serializable {

    /**
     * 轮播图列表
     */
    private List<MallIndexCarouselVO> carousels;

    /**
     * 热销商品
     */
    private List<MallIndexConfigGoodsVO> hotGoodses;

    /**
     * 新品推荐
     */
    private List<MallIndexConfigGoodsVO> newGoodses;

    /**
     * 首页推荐
     */
    private List<MallIndexConfigGoodsVO> recommendGoodses;

}
