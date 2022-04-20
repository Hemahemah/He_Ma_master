package com.zlh.he_ma_master.common;

/**
 * @author lh
 */

public enum OrderPayStatusEnum {

    /**
     * 订单支付失败
     */
    DEFAULT(-1, "支付失败"),

    /**
     * 订单支付中
     */
    PAY_ING(0, "支付中"),

    /**
     * 订单支付成功
     */
    PAY_SUCCESS(1, "支付成功");

    private int payStatus;

    private String name;

    OrderPayStatusEnum(int payStatus, String name) {
        this.payStatus = payStatus;
        this.name = name;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
