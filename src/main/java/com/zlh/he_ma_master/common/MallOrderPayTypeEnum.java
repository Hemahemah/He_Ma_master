package com.zlh.he_ma_master.common;

/**
 * @author lh
 */

public enum MallOrderPayTypeEnum {
    /**
     * 支付失败
     */
    DEFAULT(-1, "ERROR"),
    /**
     * 未支付
     */
    NOT_PAY(0, "无"),

    /**
     * 支付宝支付
     */
    ALI_PAY(1, "支付宝"),

    /**
     * 微信支付
     */
    WECHAT_PAY(2, "微信支付");

    private int status;

    private String name;

    public static MallOrderPayTypeEnum getOrderPayTypeByStatus(int status) {
        for (MallOrderPayTypeEnum value : MallOrderPayTypeEnum.values()) {
            if (value.status == status) {
                return value;
            }
        }
        return null;
    }

    MallOrderPayTypeEnum(int status, String name) {
        this.status = status;
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
