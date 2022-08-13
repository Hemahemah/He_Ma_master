package com.zlh.he_ma_master.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zlh.he_ma_master.utils.Constants;
import lombok.Data;

/**
 *
 * @author lh
 * @TableName mall_order
 */
@TableName(value ="mall_order")
@Data
public class MallOrder implements Serializable {
    /**
     *  订单主键id
     */
    @TableId(type = IdType.AUTO)
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 订单总价
     */
    private BigDecimal totalPrice;

    /**
     * 支付状态：0.未支付  1-支付成功   -1.支付失败
     */
    private Integer payStatus;

    /**
     * 支付方式：0.无  1.支付宝支付  2.微信支付
     */
    private Integer payType;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 订单状态：0.待支付 1.已支付 2.配货完成 3:出库成功 4.交易成功 -1.手动关闭 -2.超时关闭 -3.商家关闭
     */
    private Integer orderStatus;

    /**
     * 订单信息
     */
    private String extraInfo;

    /**
     * 是否删除 0 未删除
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        MallOrder other = (MallOrder) that;
        return (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getOrderNo() == null ? other.getOrderNo() == null : this.getOrderNo().equals(other.getOrderNo()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getTotalPrice() == null ? other.getTotalPrice() == null : this.getTotalPrice().equals(other.getTotalPrice()))
            && (this.getPayStatus() == null ? other.getPayStatus() == null : this.getPayStatus().equals(other.getPayStatus()))
            && (this.getPayType() == null ? other.getPayType() == null : this.getPayType().equals(other.getPayType()))
            && (this.getPayTime() == null ? other.getPayTime() == null : this.getPayTime().equals(other.getPayTime()))
            && (this.getOrderStatus() == null ? other.getOrderStatus() == null : this.getOrderStatus().equals(other.getOrderStatus()))
            && (this.getExtraInfo() == null ? other.getExtraInfo() == null : this.getExtraInfo().equals(other.getExtraInfo()))
            && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getOrderNo() == null) ? 0 : getOrderNo().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getTotalPrice() == null) ? 0 : getTotalPrice().hashCode());
        result = prime * result + ((getPayStatus() == null) ? 0 : getPayStatus().hashCode());
        result = prime * result + ((getPayType() == null) ? 0 : getPayType().hashCode());
        result = prime * result + ((getPayTime() == null) ? 0 : getPayTime().hashCode());
        result = prime * result + ((getOrderStatus() == null) ? 0 : getOrderStatus().hashCode());
        result = prime * result + ((getExtraInfo() == null) ? 0 : getExtraInfo().hashCode());
        result = prime * result + ((getIsDeleted() == null) ? 0 : getIsDeleted().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", orderId=").append(orderId);
        sb.append(", orderNo=").append(orderNo);
        sb.append(", userId=").append(userId);
        sb.append(", totalPrice=").append(totalPrice);
        sb.append(", payStatus=").append(payStatus);
        sb.append(", payType=").append(payType);
        sb.append(", payTime=").append(payTime);
        sb.append(", orderStatus=").append(orderStatus);
        sb.append(", extraInfo=").append(extraInfo);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }


}
