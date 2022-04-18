package com.zlh.he_ma_master.service;

import com.zlh.he_ma_master.api.mall.param.SaveCartParam;
import com.zlh.he_ma_master.api.mall.param.UpdateCartItemParam;
import com.zlh.he_ma_master.api.mall.vo.MallShoppingCartItemVO;
import com.zlh.he_ma_master.entity.ShoppingCartItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

    /**
     * 获取购物车信息
     * @param userId 用户Id
     * @return page
     */
    List<MallShoppingCartItemVO> getCartItems(Long userId);

    /**
     * 修改购物车信息
     * @param updateCartItemParam 修改参数
     * @param userId 用户Id
     * @return true 修改成功
     */
    boolean updateCartItem(UpdateCartItemParam updateCartItemParam, Long userId);

    /**
     * 删除购物车信息
     * @param shoppingCartItemId 购物车Id
     * @param userId 用户Id
     * @return true 删除成功
     */
    boolean deleteCartItem(Long shoppingCartItemId, Long userId);

    /**
     * 确认订单中获取购物车信息
     * @param cartItemIds 购物车编号
     * @param userId 用户Id
     * @return 商品信息
     */
    List<MallShoppingCartItemVO> getCartItemsForSettle(Long[] cartItemIds, Long userId);
}
