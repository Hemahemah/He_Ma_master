package com.zlh.he_ma_master.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zlh.he_ma_master.common.MallOrderStatusEnum;
import com.zlh.he_ma_master.entity.DelayItem;
import com.zlh.he_ma_master.entity.MallOrder;
import com.zlh.he_ma_master.service.DelayOrderService;
import com.zlh.he_ma_master.service.MallOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executor;


/**
 * @author lh
 */
@Slf4j
@Service
public class DelayOrderServiceImpl implements DelayOrderService {


    @Autowired
    private MallOrderService mallOrderService;



    private static final DelayQueue<DelayItem> DELAY_QUEUE = new DelayQueue<>();

    @Resource(name = "delayServiceExecutor")
    private Executor executor;

    @Override
    public boolean addToDelayQueue(DelayItem delayItem) {
        return DELAY_QUEUE.add(delayItem);
    }

    @Override
    public void removeToDelayQueue(MallOrder mallOrder) {
        DELAY_QUEUE.forEach(delayItem -> {
            if(mallOrder.getOrderId().equals(delayItem.getId())){
                DELAY_QUEUE.remove(delayItem);
            }
        });
    }

    @PostConstruct
    public void init(){
        // 1.获取未支付订单
        log.info("DelayOrderService:Scan unpaid orders");
        QueryWrapper<MallOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_status", MallOrderStatusEnum.ORDER_PRE_PAY);
        List<MallOrder> mallOrderList = mallOrderService.list(queryWrapper);
        // 2.加入队列
        mallOrderList.forEach(mallOrder -> addToDelayQueue(new DelayItem(mallOrder.getOrderId(), mallOrder.getCreateTime())));
        log.info("DelayOrderService:{} unpaid orders", DELAY_QUEUE.size());
        if (DELAY_QUEUE.size() == 0){
            return;
        }
        executor.execute(()->{
            while (true){
                try {
                    DelayItem delayItem = DELAY_QUEUE.take();
                    mallOrderService.closeOvertimeOrder(delayItem.getId());
                    log.info("close overtime order, id = {}", delayItem.getId());
                } catch (InterruptedException e) {
                    log.error("close overtime order error:", e);
                }
            }
        });
    }

}
