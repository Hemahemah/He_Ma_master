package com.zlh.he_ma_master.api.mall.param;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lh
 */
@Data
public class UpdateCartItemParam implements Serializable {

    @NotNull(message = "商品编号不能为空")
    private Long cartItemId;

    @NotNull(message = "商品数量不能为空")
    @Min(value = 1, message = "商品数量不能小于1")
    private Integer goodCount;
}
