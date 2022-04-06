package com.zlh.he_ma_master.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlh.he_ma_master.entity.Order;
import com.zlh.he_ma_master.service.OrderService;
import com.zlh.he_ma_master.dao.OrderMapper;
import org.springframework.stereotype.Service;

/**
* @author lh
* @createDate 2022-04-04 21:35:41
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

}




