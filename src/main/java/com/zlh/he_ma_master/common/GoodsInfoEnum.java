package com.zlh.he_ma_master.common;

/**
 * @author lh
 */
public enum GoodsInfoEnum {

    /**
     * 新品排序
     */
    GOODS_ORDER_NEW("new"),

    /**
     * 价格排序
     */
    GOODS_ORDER_PRICE("price");

    private String message;

    GoodsInfoEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
