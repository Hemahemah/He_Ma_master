package com.zlh.he_ma_master.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlh.he_ma_master.api.mall.param.SaveOrderParam;
import com.zlh.he_ma_master.api.mall.vo.MallOrderListVO;
import com.zlh.he_ma_master.entity.MallOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zlh.he_ma_master.entity.MallUser;

/**
* @author lh
* @createDate 2022-04-20 19:07:33
*/
public interface MallOrderService extends IService<MallOrder> {

    /**
     * 生成订单
     * @param saveOrderParam 订单参数
     * @param mallUser 用户信息
     * @return 订单号
     */
    String saveOrder(SaveOrderParam saveOrderParam, MallUser mallUser);

    /**
     * 支付订单(模拟)
     * @param orderNo 订单编号
     * @param payType 支付类型 0.无  1.支付宝支付  2.微信支付
     * @param userId 用户编号
     * @return true 支付成功
     */
    boolean payOrder(String orderNo, int payType, Long userId);

    /**
     * 获取订单列表
     * @param pageNumber 页码
     * @param status 订单状态
     * @param userId 用户编号
     * @return page
     */
    Page<MallOrderListVO> getOrderList(Integer pageNumber, int status, Long userId);
}
