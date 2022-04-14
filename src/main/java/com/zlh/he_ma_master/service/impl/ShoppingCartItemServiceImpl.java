package com.zlh.he_ma_master.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlh.he_ma_master.api.mall.param.SaveCartParam;
import com.zlh.he_ma_master.common.HeMaException;
import com.zlh.he_ma_master.common.ServiceResultEnum;
import com.zlh.he_ma_master.entity.GoodsInfo;
import com.zlh.he_ma_master.entity.ShoppingCartItem;
import com.zlh.he_ma_master.service.GoodsInfoService;
import com.zlh.he_ma_master.service.ShoppingCartItemService;
import com.zlh.he_ma_master.dao.ShoppingCartItemMapper;
import com.zlh.he_ma_master.utils.Constants;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
* @author lh
* @createDate 2022-04-14 20:15:52
*/
@Service
public class ShoppingCartItemServiceImpl extends ServiceImpl<ShoppingCartItemMapper, ShoppingCartItem>
    implements ShoppingCartItemService{

    @Resource
    private GoodsInfoService goodsInfoService;

    @Override
    public boolean saveCartItem(SaveCartParam saveCartParam, Long userId) {
        // 1. 校验购物车是否存在数据
        QueryWrapper<ShoppingCartItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("good_id", saveCartParam.getGoodId()).eq("user_id", userId);
        ShoppingCartItem cartItem = getOne(queryWrapper);
        if (cartItem != null){
            throw new HeMaException(ServiceResultEnum.CART_ITEM_EXIST_ERROR.getResult());
        }
        // 2. 商品是否存在
        QueryWrapper<GoodsInfo> infoQueryWrapper = new QueryWrapper<>();
        infoQueryWrapper.eq("good_id", saveCartParam.getGoodId()).eq("good_sell_status", Constants.SELL_STATUS_UP);
        GoodsInfo goodsInfo = goodsInfoService.getOne(infoQueryWrapper);
        if (goodsInfo == null){
            throw new HeMaException(ServiceResultEnum.GOODS_NOT_EXIST.getResult());
        }
        // 3. 类型转换
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        BeanUtils.copyProperties(saveCartParam, shoppingCartItem);
        shoppingCartItem.setUserId(userId);
        return save(shoppingCartItem);
    }
}




