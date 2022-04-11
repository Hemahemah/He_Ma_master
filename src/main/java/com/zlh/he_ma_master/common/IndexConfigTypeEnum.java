package com.zlh.he_ma_master.common;

/**
 * @author lh
 */

public enum IndexConfigTypeEnum {

    /**
     * 1-搜索框热搜
     */
    INDEX_SEARCH_HOTS(1, "INDEX_SEARCH_HOTS"),

    /**
     * 2-搜索下拉框热搜
     */
    INDEX_SEARCH_DOWN_HOTS(2, "INDEX_SEARCH_DOWN_HOTS"),

    /**
     * 3-(首页)热销商品
     */
    INDEX_GOODS_HOT(3, "INDEX_GOODS_HOTS"),

    /**
     * 4-(首页)新品上线
     */
    INDEX_GOODS_NEW(4, "INDEX_GOODS_NEW"),

    /**
     * 5-(首页)为你推荐
     */
    INDEX_GOODS_RECOMMEND(5, "INDEX_GOODS_RECOMMEND");

    private int type;

    private String name;

    IndexConfigTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
