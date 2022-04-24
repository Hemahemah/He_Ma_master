package com.zlh.he_ma_master.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlh.he_ma_master.api.admin.param.BatchIdParam;
import com.zlh.he_ma_master.api.mall.param.SaveOrderParam;
import com.zlh.he_ma_master.api.mall.vo.MallOrderDetailVO;
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
    Page<MallOrderListVO> getOrderList(Integer pageNumber, Integer status, Long userId);

    /**
     * 获取订单详情
     * @param orderNo 订单号
     * @param userId 用户编号
     * @return MallOrderDetailVO
     */
    MallOrderDetailVO getOrderDetail(String orderNo, Long userId);

    /**
     * 确认收货
     * @param orderNo 订单号
     * @param userId 用户编号
     * @return true 收货成功
     */
    boolean finishOrder(String orderNo, Long userId);

    /**
     * 取消订单
     * @param orderNo 订单号
     * @param userId 用户编号
     * @return true 取消订单成功
     */
    boolean cancelOrder(String orderNo, Long userId);

    /**
     * 后台分页
     * @param pageNumber 页码
     * @param pageSize 分页大小
     * @param orderNo 订单号
     * @param orderStatus 订单状态
     * @return 分页信息
     */
    Page<MallOrderListVO> getOrderPage(Integer pageNumber, Integer pageSize, String orderNo, Integer orderStatus);

    /**
     * 后台获取订单详情
     * @param orderId 订单编号
     * @return MallOrderDetailVO
     */
    MallOrderDetailVO getOrderDetailByOrderId(Integer orderId);

    /**
     * 订单配货
     * @param idParam 订单编号
     * @return true 配货成功
     */
    boolean checkDone(BatchIdParam idParam);

    /**
     * 订单出库
     * @param idParam 订单编号
     * @return true 出库成功
     */
    boolean checkOut(BatchIdParam idParam);

    /**
     * 关闭订单
     * @param idParam 订单编号
     * @return true 关闭成功
     */
    boolean handleClose(BatchIdParam idParam);
}
