package com.zlh.he_ma_master.api.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lh
 */
@Data
public class MallSearchGoodsVO {

    private Long goodId;

    private String goodName;

    private String goodImg;

    private BigDecimal sellingPrice;

    private String goodIntro;
}
