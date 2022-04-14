package com.zlh.he_ma_master.service;

import com.zlh.he_ma_master.api.mall.param.SaveCartParam;
import com.zlh.he_ma_master.entity.ShoppingCartItem;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author lh
* @createDate 2022-04-14 20:15:52
*/
public interface ShoppingCartItemService extends IService<ShoppingCartItem> {

    /**
     * 添加商品至购物车
     * @param saveCartParam 商品参数
     * @param userId 用户编号
     * @return true 添加成功
     */
    boolean saveCartItem(SaveCartParam saveCartParam, Long userId);
}
