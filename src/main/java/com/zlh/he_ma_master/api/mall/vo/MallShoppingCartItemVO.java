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
public class MallShoppingCartItemVO implements Serializable {

    private Long cartItemId;

    private Long goodId;

    private Integer goodCount;

    private String goodName;

    private String goodImg;

    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal sellingPrice;

}
