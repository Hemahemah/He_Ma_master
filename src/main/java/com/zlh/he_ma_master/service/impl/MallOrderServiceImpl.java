package com.zlh.he_ma_master.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlh.he_ma_master.api.mall.param.SaveOrderParam;
import com.zlh.he_ma_master.api.mall.vo.MallOrderDetailVO;
import com.zlh.he_ma_master.api.mall.vo.MallOrderItemVO;
import com.zlh.he_ma_master.api.mall.vo.MallOrderListVO;
import com.zlh.he_ma_master.common.*;
import com.zlh.he_ma_master.entity.*;
import com.zlh.he_ma_master.service.*;
import com.zlh.he_ma_master.dao.MallOrderMapper;
import com.zlh.he_ma_master.utils.Constants;
import com.zlh.he_ma_master.utils.NumberUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* @author lh
* @createDate 2022-04-20 19:07:33
*/
@Service
public class MallOrderServiceImpl extends ServiceImpl<MallOrderMapper, MallOrder>
    implements MallOrderService{

    @Resource
    private ShoppingCartItemService shoppingCartItemService;

    @Resource
    private UserAddressService userAddressService;

    @Resource
    private GoodsInfoService goodsInfoService;

    @Resource
    private OrderItemService orderItemService;

    @Resource
    private OrderAddressService orderAddressService;

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public String saveOrder(SaveOrderParam saveOrderParam, MallUser mallUser) {
        Map<String, Object> orderMap = checkOrder(saveOrderParam, mallUser.getUserId());
        // 1. 删除购物车中的信息
        List<ShoppingCartItem> shoppingCartItemList = (List<ShoppingCartItem>) orderMap.get("shoppingCartItem");
        List<Long> cartItemIds = shoppingCartItemList.stream().map(ShoppingCartItem::getCartItemId).collect(Collectors.toList());
        if (!shoppingCartItemService.removeBatchByIds(cartItemIds)){
            throw new HeMaException(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        }
        // 2.生成订单
        MallOrder order = new MallOrder();
        String orderNo = NumberUtil.getOrderNo();
        order.setOrderNo(orderNo);
        order.setUserId(mallUser.getUserId());
        order.setOrderStatus(0);
        order.setExtraInfo("");
        order.setTotalPrice((BigDecimal) orderMap.get("totalPrice"));
        if (!save(order)){
            throw new HeMaException(ServiceResultEnum.SAVE_ORDER_ERROR.getResult());
        }
        // 3. 更新商品库存并生成订单项
        List<GoodsInfo> goodsInfoList = (List<GoodsInfo>) orderMap.get("goodsInfos");
        List<OrderItem> orderItemList = new ArrayList<>();
        Map<Long, ShoppingCartItem> shoppingCartItemMap = shoppingCartItemList.stream().
                collect(Collectors.toMap(ShoppingCartItem::getGoodId, Function.identity(), (entity1, entity2) -> entity1));
        goodsInfoList.forEach(goodsInfo -> {
            OrderItem orderItem = new OrderItem();
            ShoppingCartItem cartItem = shoppingCartItemMap.get(goodsInfo.getGoodId());
            //生成订单项
            BeanUtils.copyProperties(goodsInfo, orderItem);
            BeanUtils.copyProperties(cartItem, orderItem);
            orderItem.setOrderId(order.getOrderId());
            goodsInfo.setStockNum(goodsInfo.getStockNum() - cartItem.getGoodCount());
            orderItemList.add(orderItem);
        });
        if (!(goodsInfoService.updateBatchById(goodsInfoList) && orderItemService.saveBatch(orderItemList))){
            throw new HeMaException(ServiceResultEnum.GOODS_ITEM_ERROR.getResult());
        }
        // 4. 生成订单地址快照
        UserAddress userAddress = (UserAddress) orderMap.get("userAddress");
        OrderAddress orderAddress = new OrderAddress();
        BeanUtils.copyProperties(userAddress, orderAddress);
        orderAddress.setOrderId(order.getOrderId());
        if (!orderAddressService.save(orderAddress)){
            throw new HeMaException(ServiceResultEnum.SAVE_ORDER_ERROR.getResult());
        }
        // 5. 返回订单号
        return orderNo;
    }

    @Override
    public boolean payOrder(String orderNo, int payType, Long userId) {
        // 1. 查询订单
        QueryWrapper<MallOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo).eq("user_id", userId);
        MallOrder mallOrder = getOne(queryWrapper);
        if (mallOrder == null){
            throw new HeMaException(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        if (mallOrder.getOrderStatus() != MallOrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()){
            throw new HeMaException(ServiceResultEnum.ORDER_STATUS_ERROR.getResult());
        }
        mallOrder.setPayTime(new Date());
        mallOrder.setPayStatus(OrderPayStatusEnum.PAY_SUCCESS.getPayStatus());
        mallOrder.setPayType(payType);
        mallOrder.setUpdateTime(new Date());
        mallOrder.setOrderStatus(MallOrderStatusEnum.ORDER_PAID.getOrderStatus());
        return updateById(mallOrder);
    }

    @Override
    public Page<MallOrderListVO> getOrderList(Integer pageNumber, Integer status, Long userId) {
        // 1. 获取订单
        QueryWrapper<MallOrder> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("user_id", userId);
        if (status != null){
            orderQueryWrapper.eq("order_status", status);
        }
        Page<MallOrder> mallOrderPage = page(new Page<>(pageNumber, Constants.ORDER_SEARCH_PAGE_LIMIT), orderQueryWrapper);
        Page<MallOrderListVO> mallOrderListVoPage = new Page<>(pageNumber, Constants.ORDER_SEARCH_PAGE_LIMIT);
       if (!CollectionUtils.isEmpty(mallOrderPage.getRecords())){
           // 2. 设置订单状态为中文并进行类型转换
           List<Long> orderIdList = new ArrayList<>();
           List<MallOrderListVO> mallOrderListVoList = new ArrayList<>();
           // 将orderVO与order形成映射
           Map<Long, MallOrderListVO> mallOrderListVoMap = new HashMap<>(mallOrderPage.getRecords().size());
           mallOrderPage.getRecords().forEach(mallOrder -> {
               MallOrderListVO mallOrderListVo = new MallOrderListVO();
               BeanUtils.copyProperties(mallOrder, mallOrderListVo);
               mallOrderListVo.setOrderStatusString(Objects.requireNonNull(MallOrderStatusEnum.getOrderStatusByStatus(mallOrder.getOrderStatus())).getName());
               mallOrderListVo.setMallOrderItemVos(new ArrayList<>());
               mallOrderListVoList.add(mallOrderListVo);
               orderIdList.add(mallOrder.getOrderId());
               mallOrderListVoMap.put(mallOrderListVo.getOrderId(), mallOrderListVo);
           });
           mallOrderListVoPage.setRecords(mallOrderListVoList);
           // 3. 获取订单项
           QueryWrapper<OrderItem> orderItemQueryWrapper = new QueryWrapper<>();
           orderItemQueryWrapper.in("order_id", orderIdList);
           List<OrderItem> orderItems = orderItemService.list(orderItemQueryWrapper);
           // 4. 类型转换并将结果存入orderVO中
           orderItems.forEach(orderItem -> {
               MallOrderItemVO mallOrderItemVO = new MallOrderItemVO();
               BeanUtils.copyProperties(orderItem, mallOrderItemVO);
               MallOrderListVO mallOrderListVO = mallOrderListVoMap.get(orderItem.getOrderId());
               mallOrderListVO.getMallOrderItemVos().add(mallOrderItemVO);
           });
       }
        return mallOrderListVoPage;
    }

    @Override
    public MallOrderDetailVO getOrderDetail(String orderNo, Long userId) {
        QueryWrapper<MallOrder> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("order_no", orderNo).eq("user_id", userId);
        MallOrder mallOrder = getOne(orderQueryWrapper);
        if (mallOrder == null){
            throw new HeMaException(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        QueryWrapper<OrderItem> itemQueryWrapper = new QueryWrapper<>();
        itemQueryWrapper.eq("order_id", mallOrder.getOrderId());
        List<OrderItem> orderItems = orderItemService.list(itemQueryWrapper);
        if (CollectionUtils.isEmpty(orderItems)){
            throw new HeMaException(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        //类型转换
        MallOrderDetailVO mallOrderDetailVO = new MallOrderDetailVO();
        List<MallOrderItemVO> mallOrderItemVoList = new ArrayList<>();
        mallOrderDetailVO.setOrderStatusString(Objects.requireNonNull(MallOrderStatusEnum.getOrderStatusByStatus(mallOrder.getOrderStatus())).getName());
        mallOrderDetailVO.setPayTypeString(Objects.requireNonNull(MallOrderPayTypeEnum.getOrderPayTypeByStatus(mallOrder.getPayStatus())).getName());
        BeanUtils.copyProperties(mallOrder, mallOrderDetailVO);
        orderItems.forEach(orderItem -> {
            MallOrderItemVO mallOrderItemVO = new MallOrderItemVO();
            BeanUtils.copyProperties(orderItem, mallOrderItemVO);
            mallOrderItemVoList.add(mallOrderItemVO);
        });
        mallOrderDetailVO.setMallOrderItemVos(mallOrderItemVoList);
        return mallOrderDetailVO;
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
        // 2. 校验商品合法性(是否包含下架商品,库存)
        List<Long> goodsIdList = itemList.stream().map(ShoppingCartItem::getGoodId).collect(Collectors.toList());
        Map<Long, ShoppingCartItem> shoppingCartItemMap = itemList.stream().
                collect(Collectors.toMap(ShoppingCartItem::getGoodId, Function.identity(), (entity1, entity2) -> entity1));
        // 获取商品
        List<GoodsInfo> goodsInfos = goodsInfoService.listByIds(goodsIdList);
        //商品总价
        BigDecimal totalPrice = new BigDecimal(0);
        for (GoodsInfo goodsInfo : goodsInfos) {
            //商品下架抛出异常
            if (goodsInfo.getGoodSellStatus() == Constants.SELL_STATUS_DOWN){
                throw new HeMaException(goodsInfo.getGoodName()+ ServiceResultEnum.GOODS_STATUS_DOWN.getResult());
            }
            ShoppingCartItem shoppingCartItem = shoppingCartItemMap.get(goodsInfo.getGoodId());
            //校验购物车是否存在此商品
            if (shoppingCartItem == null){
                throw new HeMaException(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            //校验商品库存
            if (shoppingCartItem.getGoodCount() > goodsInfo.getStockNum()){
                throw new HeMaException(goodsInfo.getGoodName()+ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
            totalPrice = totalPrice.add(goodsInfo.getSellingPrice().multiply(new BigDecimal(shoppingCartItem.getGoodCount())));
        }
        Map<String, Object> orderMap = new HashMap<>(4);
        if (totalPrice.compareTo(new BigDecimal(0)) < 0){
            throw new HeMaException(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
        }
        orderMap.put("totalPrice", totalPrice);
        orderMap.put("goodsInfos", goodsInfos);
        orderMap.put("shoppingCartItem",itemList);
        // 3. 校验收货地址信息
        UserAddress userAddress = userAddressService.getById(saveOrderParam.getAddressId());
        if (userAddress == null){
            throw new HeMaException(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        orderMap.put("userAddress", userAddress);
        return orderMap;
    }
}




