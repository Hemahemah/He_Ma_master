package com.zlh.he_ma_master.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlh.he_ma_master.api.mall.param.SaveOrderParam;
import com.zlh.he_ma_master.common.HeMaException;
import com.zlh.he_ma_master.common.ServiceResultEnum;
import com.zlh.he_ma_master.entity.MallUser;
import com.zlh.he_ma_master.entity.Order;
import com.zlh.he_ma_master.entity.ShoppingCartItem;
import com.zlh.he_ma_master.entity.UserAddress;
import com.zlh.he_ma_master.service.OrderService;
import com.zlh.he_ma_master.dao.OrderMapper;
import com.zlh.he_ma_master.service.ShoppingCartItemService;
import com.zlh.he_ma_master.service.UserAddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author lh
* @createDate 2022-04-04 21:35:41
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

    @Resource
    private ShoppingCartItemService shoppingCartItemService;

    @Resource
    private UserAddressService userAddressService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveOrder(SaveOrderParam saveOrderParam, MallUser mallUser) {
        Map<String, Object> stringOrderMap = checkOrder(saveOrderParam, mallUser.getUserId());
        return null;
    }

    /**
     * 订单生成校验
     * @param saveOrderParam 订单参数
     * @param userId 用户编号
     * @return 购物车数据 用户信息
     */
    private Map<String, Object> checkOrder(SaveOrderParam saveOrderParam, Long userId){
        // 1. 校验购物车是否存在数据
        QueryWrapper<ShoppingCartItem> cartItemQueryWrapper = new QueryWrapper<>();
        cartItemQueryWrapper.eq("user_id", userId).in("cart_item_id", Arrays.asList(saveOrderParam.getCartItemIds()));
        List<ShoppingCartItem> itemList = shoppingCartItemService.list(cartItemQueryWrapper);
        if (CollectionUtils.isEmpty(itemList)){
            throw new HeMaException(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        //todo  校验商品合法性(是否包含下架商品,库存)
        Map<String, Object> orderMap = new HashMap<>(2);
        orderMap.put("shoppingCartItem", itemList);
        // 2. 校验收货地址信息
        UserAddress userAddress = userAddressService.getById(saveOrderParam.getAddressId());
        if (userAddress == null){
            throw new HeMaException(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        orderMap.put("userAddress", userAddress);
        return orderMap;
    }
}




