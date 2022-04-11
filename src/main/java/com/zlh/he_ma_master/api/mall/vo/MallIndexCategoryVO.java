package com.zlh.he_ma_master.api.mall.vo;

import lombok.Data;
import java.util.List;

/**
 * @author lh
 */
@Data
public class MallIndexCategoryVO {

    private Long categoryId;

    private String categoryName;

    private Integer categoryLevel;

    private List<MallIndexCategoryVO> childrenCategories;
}
