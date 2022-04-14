package com.zlh.he_ma_master.api.mall.vo;

import lombok.Data;

import java.io.Serializable;

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

    private Integer sellingPrice;

}
