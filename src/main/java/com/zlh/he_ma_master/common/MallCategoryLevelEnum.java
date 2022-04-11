package com.zlh.he_ma_master.common;

/**
 * @author lh
 */
public enum MallCategoryLevelEnum {

    /**
     * 商品一级分类
     */
    LEVEL_ONE(1, "一级分类"),

    /**
     * 商品二级分类
     */
    LEVEL_TWO(2, "二级分类"),

    /**
     * 商品三级分类
     */
    LEVEL_THREE(3, "三级分类");

    private int level;

    private String name;

    MallCategoryLevelEnum(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
