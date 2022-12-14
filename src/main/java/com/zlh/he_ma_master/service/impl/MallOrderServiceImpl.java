package com.zlh.he_ma_master.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlh.he_ma_master.api.admin.param.BatchIdParam;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* @author lh
* @createDate 2022-04-20 19:07:33
*/
@Slf4j
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

    @Autowired
    private DelayOrderService delayOrderService;

    @Resource
    private NumberUtil numberUtil;

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public String saveOrder(SaveOrderParam saveOrderParam, MallUser mallUser) {
        Map<String, Object> orderMap = checkOrder(saveOrderParam, mallUser.getUserId());
        // 1. ???????????????????????????
        List<ShoppingCartItem> shoppingCartItemList = (List<ShoppingCartItem>) orderMap.get("shoppingCartItem");
        List<Long> cartItemIds = shoppingCartItemList.stream().map(ShoppingCartItem::getCartItemId).collect(Collectors.toList());
        if (!shoppingCartItemService.removeBatchByIds(cartItemIds)){
            throw new HeMaException(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        }
        // 2.????????????
        MallOrder order = new MallOrder();
        Date createDate = new Date();
        String orderNo = numberUtil.getOrderNo();
        order.setOrderNo(orderNo);
        order.setUserId(mallUser.getUserId());
        order.setCreateTime(createDate);
        order.setOrderStatus(0);
        order.setExtraInfo("");
        order.setTotalPrice((BigDecimal) orderMap.get("totalPrice"));
        // 2.1 ?????????????????????????????????
        if (!save(order) || !delayOrderService.addToDelayQueue(new DelayItem(order.getOrderId(), createDate))){
            throw new HeMaException(ServiceResultEnum.SAVE_ORDER_ERROR.getResult());
        }
        // 3. ????????????????????????????????????
        List<GoodsInfo> goodsInfoList = (List<GoodsInfo>) orderMap.get("goodsInfos");
        List<OrderItem> orderItemList = new ArrayList<>();
        Map<Long, ShoppingCartItem> shoppingCartItemMap = shoppingCartItemList.stream().
                collect(Collectors.toMap(ShoppingCartItem::getGoodId, Function.identity(), (entity1, entity2) -> entity1));
        goodsInfoList.forEach(goodsInfo -> {
            OrderItem orderItem = new OrderItem();
            ShoppingCartItem cartItem = shoppingCartItemMap.get(goodsInfo.getGoodId());
            //???????????????
            BeanUtils.copyProperties(goodsInfo, orderItem);
            BeanUtils.copyProperties(cartItem, orderItem);
            orderItem.setOrderId(order.getOrderId());
            goodsInfo.setStockNum(goodsInfo.getStockNum() - cartItem.getGoodCount());
            if (!goodsInfoService.updateById(goodsInfo)){
                throw new HeMaException(goodsInfo.getGoodName()+ServiceResultEnum.GOODS_ITEM_COUNT_ERROR.getResult());
            }
            orderItemList.add(orderItem);
        });
        if (!orderItemService.saveBatch(orderItemList)){
            throw new HeMaException(ServiceResultEnum.GOODS_ITEM_ERROR.getResult());
        }
        // 4. ????????????????????????
        UserAddress userAddress = (UserAddress) orderMap.get("userAddress");
        OrderAddress orderAddress = new OrderAddress();
        BeanUtils.copyProperties(userAddress, orderAddress);
        orderAddress.setOrderId(order.getOrderId());
        if (!orderAddressService.save(orderAddress)){
            throw new HeMaException(ServiceResultEnum.SAVE_ORDER_ERROR.getResult());
        }
        // 5. ???????????????
        return orderNo;
    }

    @Override
    public boolean payOrder(String orderNo, int payType, Long userId) {
        // 1. ????????????
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
        // 2.???????????????????????????
        delayOrderService.removeToDelayQueue(mallOrder);
        return updateById(mallOrder);
    }

    @Override
    public Page<MallOrderListVO> getOrderList(Integer pageNumber, Integer status, Long userId) {
        // 1. ????????????
        QueryWrapper<MallOrder> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("user_id", userId).orderByDesc("create_time");
        if (status != null){
            orderQueryWrapper.eq("order_status", status);
        }
        Page<MallOrder> mallOrderPage = page(new Page<>(pageNumber, Constants.ORDER_SEARCH_PAGE_LIMIT), orderQueryWrapper);
        Page<MallOrderListVO> mallOrderListVoPage = new Page<>(pageNumber, Constants.ORDER_SEARCH_PAGE_LIMIT);
       if (!CollectionUtils.isEmpty(mallOrderPage.getRecords())){
           // 2. ????????????????????????????????????????????????
           List<Long> orderIdList = new ArrayList<>();
           List<MallOrderListVO> mallOrderListVoList = new ArrayList<>();
           // ???orderVO???order????????????
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
           // 3. ???????????????
           QueryWrapper<OrderItem> orderItemQueryWrapper = new QueryWrapper<>();
           orderItemQueryWrapper.in("order_id", orderIdList);
           List<OrderItem> orderItems = orderItemService.list(orderItemQueryWrapper);
           // 4. ??????????????????????????????orderVO???
           orderItems.forEach(orderItem -> {
               MallOrderItemVO mallOrderItemVO = new MallOrderItemVO();
               BeanUtils.copyProperties(orderItem, mallOrderItemVO);
               MallOrderListVO mallOrderListVO = mallOrderListVoMap.get(orderItem.getOrderId());
               mallOrderListVO.getMallOrderItemVos().add(mallOrderItemVO);
           });
       }
        mallOrderListVoPage.setSearchCount(mallOrderPage.hasNext());
        return mallOrderListVoPage;
    }

    @Override
    public MallOrderDetailVO getOrderDetail(String orderNo, Long userId) {
        QueryWrapper<MallOrder> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("order_no", orderNo).eq("user_id", userId);
       return getMallOrderDetailVO(orderQueryWrapper);
    }

    @Override
    public boolean finishOrder(String orderNo, Long userId) {
        // 1. ????????????
        QueryWrapper<MallOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo).eq("user_id", userId);
        MallOrder mallOrder = getOne(queryWrapper);
        // 2. ??????????????????
        if (mallOrder == null){
            throw new HeMaException(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        if (mallOrder.getOrderStatus() != MallOrderStatusEnum.ORDER_EXPRESS.getOrderStatus()){
            throw new HeMaException(ServiceResultEnum.ORDER_STATUS_ERROR.getResult());
        }
        // 3. ??????????????????
        mallOrder.setOrderStatus(MallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
        mallOrder.setUpdateTime(new Date());
        return updateById(mallOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(String orderNo, Long userId) {
        // 1. ????????????
        QueryWrapper<MallOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo).eq("user_id", userId);
        MallOrder mallOrder = getOne(queryWrapper);
        // 2. ??????????????????
        if (mallOrder == null){
            throw new HeMaException(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
       if (mallOrder.getOrderStatus() == MallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus() ||
               mallOrder.getOrderStatus() == MallOrderStatusEnum.ORDER_CLOSED_BY_USER.getOrderStatus() ||
               mallOrder.getOrderStatus() == MallOrderStatusEnum.ORDER_CLOSED_BY_EXPIRED.getOrderStatus() ||
               mallOrder.getOrderStatus() == MallOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()){
            throw new HeMaException(ServiceResultEnum.ORDER_STATUS_ERROR.getResult());
       }
        // 3. ??????????????????
        mallOrder.setOrderStatus(MallOrderStatusEnum.ORDER_CLOSED_BY_USER.getOrderStatus());
        mallOrder.setUpdateTime(new Date());
        delayOrderService.removeToDelayQueue(mallOrder);
        // 4. ??????????????????
        List<OrderItem> orderList = orderItemService.getOrderListByOrderNo(orderNo);
        return updateById(mallOrder) && goodsInfoService.updateGoodsCount(orderList);
    }

    @Override
    public Page<MallOrderListVO> getOrderPage(Integer pageNumber, Integer pageSize, String orderNo, Integer orderStatus) {
        QueryWrapper<MallOrder> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(orderNo)){
            queryWrapper.eq("order_no", orderNo);
        }
        if(orderStatus != null){
            queryWrapper.eq("order_status", orderStatus);
        }
        Page<MallOrder> mallOrderPage = page(new Page<>(pageNumber, pageSize), queryWrapper);
        Page<MallOrderListVO> mallOrderListVoPage = new Page<>(pageNumber, pageSize);
        List<MallOrderListVO> mallOrderListVoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(mallOrderPage.getRecords())){
            mallOrderPage.getRecords().forEach(mallOrder -> {
                MallOrderListVO mallOrderListVo = new MallOrderListVO();
                BeanUtils.copyProperties(mallOrder, mallOrderListVo);
                mallOrderListVo.setOrderStatusString(Objects.requireNonNull(MallOrderStatusEnum.getOrderStatusByStatus(mallOrder.getOrderStatus())).getName());
                mallOrderListVoList.add(mallOrderListVo);
            });
        }
        mallOrderListVoPage.setRecords(mallOrderListVoList);
        return mallOrderListVoPage;
    }

    @Override
    public MallOrderDetailVO getOrderDetailByOrderId(Integer orderId) {
        QueryWrapper<MallOrder> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("order_id", orderId);
        return getMallOrderDetailVO(orderQueryWrapper);
    }

    @Override
    public boolean checkDone(BatchIdParam idParam) {
        // 1. ????????????
        QueryWrapper<MallOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_status", MallOrderStatusEnum.ORDER_PAID.getOrderStatus()).in("order_id", Arrays.asList(idParam.getIds()));
        List<MallOrder> mallOrders = list(queryWrapper);
        if (CollectionUtils.isEmpty(mallOrders) || mallOrders.size() != idParam.getIds().length){
            throw new HeMaException(ServiceResultEnum.ORDER_STATUS_ERROR.getResult());
        }
        // 2. ????????????
        mallOrders.forEach(mallOrder -> mallOrder.setOrderStatus(MallOrderStatusEnum.ORDER_PACKAGED.getOrderStatus()));
        return updateBatchById(mallOrders);
    }

    @Override
    public boolean checkOut(BatchIdParam idParam) {
        // 1. ????????????
        QueryWrapper<MallOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_status", MallOrderStatusEnum.ORDER_PACKAGED.getOrderStatus()).in("order_id", Arrays.asList(idParam.getIds()));
        List<MallOrder> mallOrders = list(queryWrapper);
        if (CollectionUtils.isEmpty(mallOrders) || mallOrders.size() != idParam.getIds().length){
            throw new HeMaException(ServiceResultEnum.ORDER_STATUS_ERROR.getResult());
        }
        // 2. ????????????
        mallOrders.forEach(mallOrder -> mallOrder.setOrderStatus(MallOrderStatusEnum.ORDER_EXPRESS.getOrderStatus()));
        return updateBatchById(mallOrders);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handleClose(BatchIdParam idParam) {
        // 1. ????????????
        QueryWrapper<MallOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("order_id", Arrays.asList(idParam.getIds()));
        List<MallOrder> mallOrders = list(queryWrapper);
        if (CollectionUtils.isEmpty(mallOrders) || mallOrders.size() != idParam.getIds().length){
            throw new HeMaException(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        mallOrders.forEach(mallOrder -> {
            // 2. ??????????????????(??????????????????????????????????????????)
            if (mallOrder.getOrderStatus() == MallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus() || mallOrder.getOrderStatus() < 0){
                throw new HeMaException(ServiceResultEnum.ORDER_STATUS_ERROR.getResult());
            }
            mallOrder.setOrderStatus(MallOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus());
        });
        // 2. ???????????????????????????
        List<OrderItem> orderItemList = new ArrayList<>();
        for (Long id : idParam.getIds()) {
            List<OrderItem> itemList = orderItemService.getOrderListByOrderId(id);
            orderItemList.addAll(itemList);
        }
        return updateBatchById(mallOrders) && goodsInfoService.updateGoodsCount(orderItemList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeOvertimeOrder(Long orderId) {
        // 1. ????????????
        QueryWrapper<MallOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        MallOrder mallOrder = getOne(queryWrapper);
        if (mallOrder == null){
            throw new HeMaException(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        if (mallOrder.getOrderStatus() != MallOrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()){
            throw new HeMaException(ServiceResultEnum.ORDER_STATUS_ERROR.getResult());
        }
        mallOrder.setOrderStatus(MallOrderStatusEnum.ORDER_CLOSED_BY_EXPIRED.getOrderStatus());
        // 2.??????????????????
        List<OrderItem> orderList = orderItemService.getOrderListByOrderId(orderId);
        updateById(mallOrder);
        log.info("MallOrderService:close overtime order:{}",orderId);
        goodsInfoService.updateGoodsCount(orderList);
    }

    /**
     * ??????queryWrapper??????????????????
     * @param orderQueryWrapper ????????????
     * @return MallOrderDetailVO
     */
    private MallOrderDetailVO getMallOrderDetailVO(QueryWrapper<MallOrder> orderQueryWrapper) {
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
        //????????????
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
     * ??????????????????
     * @param saveOrderParam ????????????
     * @param userId ????????????
     * @return ??????????????? ????????????
     */
    private Map<String, Object> checkOrder(SaveOrderParam saveOrderParam, Long userId){
        // 1. ?????????????????????????????????
        QueryWrapper<ShoppingCartItem> cartItemQueryWrapper = new QueryWrapper<>();
        cartItemQueryWrapper.eq("user_id", userId).in("cart_item_id", Arrays.asList(saveOrderParam.getCartItemIds()));
        List<ShoppingCartItem> itemList = shoppingCartItemService.list(cartItemQueryWrapper);
        if (CollectionUtils.isEmpty(itemList)){
            throw new HeMaException(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        // 2. ?????????????????????(????????????????????????,??????)
        List<Long> goodsIdList = itemList.stream().map(ShoppingCartItem::getGoodId).collect(Collectors.toList());
        Map<Long, ShoppingCartItem> shoppingCartItemMap = itemList.stream().
                collect(Collectors.toMap(ShoppingCartItem::getGoodId, Function.identity(), (entity1, entity2) -> entity1));
        // ????????????
        List<GoodsInfo> goodsInfos = goodsInfoService.listByIds(goodsIdList);
        //????????????
        BigDecimal totalPrice = new BigDecimal(0);
        for (GoodsInfo goodsInfo : goodsInfos) {
            //????????????????????????
            if (goodsInfo.getGoodSellStatus() == Constants.SELL_STATUS_DOWN){
                throw new HeMaException(goodsInfo.getGoodName()+ ServiceResultEnum.GOODS_STATUS_DOWN.getResult());
            }
            ShoppingCartItem shoppingCartItem = shoppingCartItemMap.get(goodsInfo.getGoodId());
            //????????????????????????????????????
            if (shoppingCartItem == null){
                throw new HeMaException(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            //??????????????????
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
        // 3. ????????????????????????
        UserAddress userAddress = userAddressService.getById(saveOrderParam.getAddressId());
        if (userAddress == null){
            throw new HeMaException(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        orderMap.put("userAddress", userAddress);
        return orderMap;
    }


}




