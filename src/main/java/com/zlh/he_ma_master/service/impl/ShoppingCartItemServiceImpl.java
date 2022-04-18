package com.zlh.he_ma_master.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlh.he_ma_master.api.mall.param.SaveCartParam;
import com.zlh.he_ma_master.api.mall.param.UpdateCartItemParam;
import com.zlh.he_ma_master.api.mall.vo.MallShoppingCartItemVO;
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
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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
        shoppingCartItem.setUpdateTime(new Date());
        return save(shoppingCartItem);
    }

    @Override
    public List<MallShoppingCartItemVO> getCartItems(Long userId) {
        QueryWrapper<ShoppingCartItem> queryWrapper = new QueryWrapper<>();
        queryWrapper = queryWrapper.eq("user_id", userId);
        List<MallShoppingCartItemVO> shoppingCartItemVos = new ArrayList<>();
        List<ShoppingCartItem> itemList = list(queryWrapper);
        // 购物车是否有商品
        if (!CollectionUtils.isEmpty(itemList)){
            getMallShoppingCartItemVOList(itemList, shoppingCartItemVos);
        }
        return shoppingCartItemVos;
    }

    @Override
    public boolean updateCartItem(UpdateCartItemParam updateCartItemParam, Long userId) {
        // 1. 获取购物车记录
        QueryWrapper<ShoppingCartItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cart_item_id", updateCartItemParam.getCartItemId()).eq("user_id", userId);
        ShoppingCartItem cartItem = getOne(queryWrapper);
        if (cartItem == null){
            throw new HeMaException(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        // 2. 校验用户id是否一致
        if (!userId.equals(cartItem.getUserId())){
            throw new HeMaException(ServiceResultEnum.NO_PERMISSION_ERROR.getResult());
        }
        //  若修改参数与原先一致则不执行修改操作
        if (cartItem.getGoodCount().equals(updateCartItemParam.getGoodCount())){
            return true;
        }
        // 3. 修改
        cartItem.setGoodCount(updateCartItemParam.getGoodCount());
        cartItem.setUpdateTime(new Date());
        return updateById(cartItem);
    }

    @Override
    public boolean deleteCartItem(Long shoppingCartItemId, Long userId) {
        QueryWrapper<ShoppingCartItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cart_item_id", shoppingCartItemId).eq("user_id", userId);
        ShoppingCartItem cartItem = getOne(queryWrapper);
        // 1. 校验购物车记录是否存在
        if (cartItem == null){
            throw new HeMaException(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        // 2. 校验用户id是否一致
        if (!userId.equals(cartItem.getUserId())){
            throw new HeMaException(ServiceResultEnum.NO_PERMISSION_ERROR.getResult());
        }
        cartItem.setUpdateTime(new Date());
        return removeById(cartItem);
    }

    @Override
    public List<MallShoppingCartItemVO> getCartItemsForSettle(Long[] cartItemIds, Long userId) {
        QueryWrapper<ShoppingCartItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).in("cart_item_id", Arrays.asList(cartItemIds));
        List<ShoppingCartItem> itemList = list(queryWrapper);
        if (CollectionUtils.isEmpty(itemList)){
            throw new HeMaException(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        if (cartItemIds.length != itemList.size()){
            throw new HeMaException(ServiceResultEnum.PARAM_EXCEPTION.getResult());
        }
        List<MallShoppingCartItemVO> shoppingCartItemVos = new ArrayList<>();
        getMallShoppingCartItemVOList(itemList, shoppingCartItemVos);
        return shoppingCartItemVos;
    }

    /**
     * 类型转换 将 ShoppingCartItem 转换为 MallShoppingCartItemVO
     * @param itemList ShoppingCartItem
     * @param shoppingCartItemVos MallShoppingCartItemVO
     */
    private void getMallShoppingCartItemVOList(List<ShoppingCartItem> itemList, List<MallShoppingCartItemVO> shoppingCartItemVos){
        List<Long> goodIds = itemList.stream().map(ShoppingCartItem::getGoodId).collect(Collectors.toList());
        // 获取购物车中商品信息
        List<GoodsInfo> goodsInfos = goodsInfoService.listByIds(goodIds);
        // 类型转换
        goodsInfos.forEach(goodsInfo -> {
            MallShoppingCartItemVO mallShoppingCartItemVo = new MallShoppingCartItemVO();
            BeanUtils.copyProperties(goodsInfo, mallShoppingCartItemVo);
            Optional<ShoppingCartItem> first = itemList.stream().filter(item ->item.getGoodId().equals(mallShoppingCartItemVo.getGoodId())).findFirst();
            BeanUtils.copyProperties(first.orElse(new ShoppingCartItem()), mallShoppingCartItemVo);
            shoppingCartItemVos.add(mallShoppingCartItemVo);
        });
    }
}




