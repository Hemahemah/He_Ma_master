package com.zlh.he_ma_master.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlh.he_ma_master.entity.OrderItem;
import com.zlh.he_ma_master.service.OrderItemService;
import com.zlh.he_ma_master.dao.OrderItemMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
* @author lh
* @description 针对表【order_item】的数据库操作Service实现
* @createDate 2022-04-19 20:11:36
*/
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem>
    implements OrderItemService{

    @Resource
    private OrderItemMapper orderItemMapper;

    @Override
    public List<OrderItem> getOrderListByOrderNo(String orderNo) {
        return orderItemMapper.getOrderListByOrderNo(orderNo);
    }

    @Override
    public List<OrderItem> getOrderListByOrderId(Long orderId) {
        return orderItemMapper.getOrderListByOrderId(orderId);
    }
}




