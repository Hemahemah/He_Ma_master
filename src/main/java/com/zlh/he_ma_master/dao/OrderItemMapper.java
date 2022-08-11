package com.zlh.he_ma_master.dao;

import com.zlh.he_ma_master.entity.OrderItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author lh
* @createDate 2022-04-19 20:11:36
*/
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    /**
     * 根据订单编号获取订单详情
     * @param orderNo 订单编号
     * @return 订单详情
     */
    List<OrderItem> getOrderListByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 根据订单id获取订单详情
     * @param orderId 订单Id
     * @return 订单详情
     */
    List<OrderItem> getOrderListByOrderId(@Param("orderId") Long orderId);


}




