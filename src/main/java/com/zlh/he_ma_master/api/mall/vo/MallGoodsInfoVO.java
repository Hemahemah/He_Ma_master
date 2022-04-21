package com.zlh.he_ma_master.api.mall.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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

    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal sellingPrice;

    private String tag;

    private String[] goodsCarouselList;

    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal originalPrice;

    private String goodDetailContent;

}
