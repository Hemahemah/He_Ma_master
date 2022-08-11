package com.zlh.he_ma_master.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlh.he_ma_master.entity.GoodsInfo;
import com.zlh.he_ma_master.entity.OrderItem;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* @author lh
* @createDate 2022-03-28 22:06:09
*/
public interface GoodsInfoMapper extends BaseMapper<GoodsInfo> {

    /**
     * 根据取消订单信息更新商品数量
     * @param orderItems 订单信息
     * @return boolean
     */
    boolean updateGoodsCount(@Param("orderItems") List<OrderItem> orderItems);

}




