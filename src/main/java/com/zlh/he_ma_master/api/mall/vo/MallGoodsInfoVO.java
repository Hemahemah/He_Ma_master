package com.zlh.he_ma_master.api.mall.vo;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lh
 */
@Data
public class MallGoodsInfoVO implements Serializable {

    private Long goodId;

    private String goodName;

    private String goodIntro;

    private String goodImg;

    private BigDecimal sellingPrice;

    private String tag;

    private String[] goodsCarouselList;

    private BigDecimal originalPrice;

    private String goodDetailContent;

}
