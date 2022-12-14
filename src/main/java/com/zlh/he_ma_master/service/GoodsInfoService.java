package com.zlh.he_ma_master.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlh.he_ma_master.api.admin.param.BatchIdParam;
import com.zlh.he_ma_master.api.admin.param.GoodAddParam;
import com.zlh.he_ma_master.api.admin.param.GoodsEditParam;
import com.zlh.he_ma_master.api.mall.vo.MallGoodsInfoVO;
import com.zlh.he_ma_master.api.mall.vo.MallSearchGoodsVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zlh.he_ma_master.entity.GoodsInfo;
import com.zlh.he_ma_master.entity.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 商品管理模块
* @author lh
* @createDate 2022-03-28 22:06:09
*/
public interface GoodsInfoService extends IService<GoodsInfo> {

    /**
     * 获取商品分页信息
     * @param pageNumber 页码
     * @param pageSize 分页大小
     * @return 分页参数
     */
    Page<GoodsInfo> getGoodsPage(Integer pageNumber, Integer pageSize);

    /**
     * 获取商品信息
     * @param id 商品编号
     * @return 商品信息
     */
    Map<String, Object> getGoodInfo(Long id);

    /**
     * 添加商品信息
     * @param goodAddParam 商品添加参数
     * @return true 添加成功
     */
    boolean saveGoodInfo(GoodAddParam goodAddParam);

    /**
     * 修改商品信息
     * @param editParam 商品修改参数
     * @return true 添加成功
     */
    boolean updateGoodsInfo(GoodsEditParam editParam);

    /**
     * 修改商品状态
     * @param sellStatus 商品状态
     * @param idParam 商品编号
     * @return true 添加成功
     */
    boolean updateStatus(int sellStatus, BatchIdParam idParam);

    /**
     * 搜索商品
     * @param keyword 关键词
     * @param goodsCategoryId 商品分类Id
     * @param orderBy 排序
     * @param pageNumber 页码
     * @return 商品信息
     */
    Page<MallSearchGoodsVO> searchGoods(String keyword, Long goodsCategoryId, String orderBy, Integer pageNumber);

    /**
     * 获取商品详情
     * @param goodId 商品编号
     * @return 商品详情
     */
    MallGoodsInfoVO getGoodsDetail(Long goodId);

    /**
     * 根据取消订单信息更新商品数量
     * @param orderItems 订单信息
     * @return boolean
     */
    boolean updateGoodsCount(@Param("orderItems") List<OrderItem> orderItems);
}
