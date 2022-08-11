package com.zlh.he_ma_master.service;

import com.zlh.he_ma_master.dao.GoodsInfoMapper;
import com.zlh.he_ma_master.dao.OrderItemMapper;
import com.zlh.he_ma_master.entity.OrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class OrderItemServiceTest {

    @Resource
    private OrderItemMapper orderItemMapper;

    @Resource
    private GoodsInfoMapper goodsInfoMapper;

    @Test
    public void getOrderItem(){
        List<OrderItem> orderList = orderItemMapper.getOrderListByOrderId(71L);
        orderList.forEach(System.out::println);

    }


}
