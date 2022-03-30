package com.zlh.he_ma_master.api.admin.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class GoodAddParam implements Serializable {

    @NotNull(message = "分类id不能为空")
    @Min(value = 1, message = "分类id最低为1")
    private Long goodCategoryId;

    @NotEmpty(message = "商品主图不能为空")
    private String goodImg;

    @NotEmpty(message = "商品详情不能为空")
    private String goodsDetailContent;

    @NotEmpty(message = "商品简介不能为空")
    @Length(max = 200,message = "商品简介内容过长")
    private String goodIntro;

    @NotEmpty(message = "商品名称不能为空")
    @Length(max = 128,message = "商品名称内容过长")
    private String goodName;

    @NotEmpty(message = "商品标签不能为空")
    @Length(max = 16,message = "商品标签内容过长")
    private Integer goodSellStatus;

    @NotNull(message = "originalPrice不能为空")
    @Min(value = 1, message = "originalPrice最低为1")
    @Max(value = 1000000, message = "originalPrice最高为1000000")
    private Integer originalPrice;

    @NotNull(message = "sellingPrice不能为空")
    @Min(value = 1, message = "sellingPrice最低为1")
    @Max(value = 1000000, message = "sellingPrice最高为1000000")
    private Integer sellingPrice;

    @NotNull(message = "库存不能为空")
    @Min(value = 1, message = "库存最低为1")
    @Max(value = 100000, message = "库存最高为100000")
    private Integer stockNum;

    @NotEmpty(message = "商品标签不能为空")
    @Length(max = 16,message = "商品标签内容过长")
    private String tag;
}
