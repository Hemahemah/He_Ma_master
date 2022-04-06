package com.zlh.he_ma_master.api.admin.param;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author lh
 */
@Data
public class ConfigAddParam {

    @Min(value = 1, message = "configType最小为1")
    @Max(value = 5, message = "configType最大为5")
    @NotNull(message = "配置类型不能为空")
    private Integer configType;

    @NotEmpty(message = "商品名称不能为空")
    private String configName;

    @NotEmpty(message = "跳转链接不能为空")
    private String redirectUrl;

    @NotNull(message = "商品id不能为空")
    private Long goodsId;

    @NotNull(message = "分类排序值不能空")
    @Min(value = 1, message = "categoryRank最低为1")
    @Max(value = 200, message = "categoryRank最高为200")
    private Integer configRank;
}
