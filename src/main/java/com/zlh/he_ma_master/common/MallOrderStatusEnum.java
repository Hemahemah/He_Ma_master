package com.zlh.he_ma_master.common;

/**
 * @author lh
 */
public enum MallOrderStatusEnum {
    /**
     * 订单待支付
     */
    ORDER_PRE_PAY(0, "待支付"),

    /**
     * 订单待支付
     */
    ORDER_PAID(1, "已支付"),

    /**
     * 订单待支付
     */
    ORDER_PACKAGED(2, "配货完成"),

    /**
     * 订单待支付
     */
    ORDER_EXPRESS(3, "出库成功"),

    /**
     * 订单待支付
     */
    ORDER_SUCCESS(4, "交易成功"),

    /**
     * 订单待支付
     */
    ORDER_CLOSED_BY_USER(-1, "手动关闭"),

    /**
     * 订单待支付
     */
    ORDER_CLOSED_BY_EXPIRED(-2, "超时关闭"),

    /**
     * 订单待支付
     */
    ORDER_CLOSED_BY_JUDGE(-3, "商家关闭");

    private int orderStatus;

    private String name;

    MallOrderStatusEnum(int orderStatus, String name) {
        this.orderStatus = orderStatus;
        this.name = name;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static MallOrderStatusEnum getOrderStatusByStatus(int status){
        for (MallOrderStatusEnum mallOrderStatusEnum : MallOrderStatusEnum.values()) {
            if (mallOrderStatusEnum.getOrderStatus() == status){
                return mallOrderStatusEnum;
            }
        }
        return null;
    }
}
