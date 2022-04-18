package com.zlh.he_ma_master.api.mall.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lh
 */
@Data
public class SaveOrderParam implements Serializable {

    @NotNull(message = "地址编号不能为空")
    private Long addressId;

    @NotNull(message = "购物车编号不能为空")
    private Long[] cartItemIds;
}
