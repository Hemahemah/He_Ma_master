package com.zlh.he_ma_master.api.mall.param;

import lombok.Data;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lh
 */
@Data
public class SaveCartParam implements Serializable {

    @NotNull(message = "商品数量不能为空")
    @Max(value = 5, message = "商品数量不能超过5")
    @Min(value = 1, message = "商品数量不能低于1")
    private Integer goodCount;

    @NotNull(message = "商品id不能为空")
    private Long goodId;
}
