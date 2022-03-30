package com.zlh.he_ma_master.api.admin.param;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CarouselAddParam implements Serializable {

    @NotEmpty(message = "轮播图URL不能为空")
    private String carouselUrl;

    @NotEmpty(message = "跳转路径不能为空")
    private String redirectUrl;

    @Min(value = 1,message = "最小值不能低于1")
    @Max(value = 200,message = "最大值不能超过200")
    @NotNull(message = "排序值不能为空")
    private Integer carouselRank;
}
