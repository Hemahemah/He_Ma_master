package com.zlh.he_ma_master.api.admin.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.*;

@Data
public class CategoryAddParam {

    @NotNull(message = "商品分类不能为空")
    @Min(value = 1, message = "分类级别最低为1")
    @Max(value = 3, message = "分类级别最高为3")
    private Integer categoryLevel;

    @NotNull(message = "父分类id不能为空")
    @Min(value = 0, message = "父分类id值最低为0")
    private Long parentId;

    @NotEmpty(message = "商品分类不能空")
    @Length(max = 16,message = "分类名称过长")
    private String categoryName;

    @NotNull(message = "分类排序值不能空")
    @Min(value = 1, message = "categoryRank最低为1")
    @Max(value = 200, message = "categoryRank最高为200")
    private Integer categoryRank;
}
