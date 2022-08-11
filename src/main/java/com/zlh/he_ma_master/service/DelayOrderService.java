package com.zlh.he_ma_master.service;

import com.zlh.he_ma_master.entity.DelayItem;
import com.zlh.he_ma_master.entity.MallOrder;
import org.springframework.stereotype.Service;


/**
 * 订单超时接口
 * @author lh
 */
@Service
public interface DelayOrderService {


    /**
     * 添加订单对象至延迟队列
     * @param delayItem 定时对象
     * @return boolean
     */
    boolean addToDelayQueue(DelayItem delayItem);

    /**
     * 从队列中移除指定订单
     * @param mallOrder 定时对象
     */
    void removeToDelayQueue(MallOrder mallOrder);
}
