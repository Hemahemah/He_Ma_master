package com.zlh.he_ma_master.api.mall.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author lh
 */
@Data
public class MallOrderListVO implements Serializable {

    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 支付方式：0.无  1.支付宝支付  2.微信支付
     */
    private Integer payType;

    /**
     * 订单状态：0.待支付 1.已支付 2.配货完成 3:出库成功 4.交易成功 -1.手动关闭 -2.超时关闭 -3.商家关闭
     */
    private Integer orderStatus;

    /**
     * 订单总价
     */
    private BigDecimal totalPrice;

    /**
     * 订单状态
     */
    private String orderStatusString;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 订单项列表
     */
    private List<MallOrderItemVO> mallOrderItemVos;
}