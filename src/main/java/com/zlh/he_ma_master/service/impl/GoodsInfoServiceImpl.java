package com.zlh.he_ma_master.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlh.he_ma_master.api.admin.param.BatchIdParam;
import com.zlh.he_ma_master.api.admin.param.GoodAddParam;
import com.zlh.he_ma_master.api.admin.param.GoodsEditParam;
import com.zlh.he_ma_master.common.HeMaException;
import com.zlh.he_ma_master.common.ServiceResultEnum;
import com.zlh.he_ma_master.entity.GoodsCategory;
import com.zlh.he_ma_master.entity.GoodsInfo;
import com.zlh.he_ma_master.service.GoodsCategoryService;
import com.zlh.he_ma_master.service.GoodsInfoService;
import com.zlh.he_ma_master.dao.GoodsInfoMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;

/**
* @author lh
* @createDate 2022-03-28 22:06:09
*/
@Service
public class GoodsInfoServiceImpl extends ServiceImpl<GoodsInfoMapper, GoodsInfo>
    implements GoodsInfoService{

    @Resource
    private GoodsCategoryService categoryService;

    @Override
    public Page<GoodsInfo> getGoodsPage(Integer pageNumber, Integer pageSize) {
        Page<GoodsInfo> goodsInfoPage = new Page<>(pageNumber,pageSize);
        return page(goodsInfoPage);
    }

    @Override
    public Map<String, Object> getGoodInfo(Long id) {
        // 1. 校验是否有此商品
        GoodsInfo goodsInfo = getById(id);
        if (goodsInfo == null){
            return null;
        }
        // 2. 查询商品的分类
        Map<String, Object> goodInfoMap = new HashMap<>(8);
        goodInfoMap.put("goods",goodsInfo);
        GoodsCategory thirdCategory = categoryService.getById(goodsInfo.getGoodCategoryId());
        if (thirdCategory != null){
            goodInfoMap.put("thirdCategory",thirdCategory);
            GoodsCategory secondCategory = categoryService.getById(thirdCategory.getParentId());
            if (secondCategory != null){
                goodInfoMap.put("secondCategory",secondCategory);
                GoodsCategory firstCategory = categoryService.getById(secondCategory.getParentId());
                if (firstCategory != null){
                    goodInfoMap.put("firstCategory",firstCategory);
                }
            }
        }
        return goodInfoMap;
    }

    @Override
    public boolean saveGoodInfo(GoodAddParam goodAddParam) {
        GoodsInfo goodsInfo = copyProperties(goodAddParam);
        GoodsCategory category = categoryService.getById(goodsInfo.getGoodCategoryId());
        // 1. 校验分类是否为三级分类
        if (category == null || category.getCategoryLevel() != 3){
            throw new HeMaException(ServiceResultEnum.GOODS_CATEGORY_ERROR.getResult());
        }
        // 2. 判断相同分类下是否有相同商品
        QueryWrapper<GoodsInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("good_name",goodsInfo.getGoodName()).eq("good_category_id",goodsInfo.getGoodCategoryId());
        GoodsInfo info = getOne(queryWrapper);
        if (info != null){
            throw new HeMaException(ServiceResultEnum.SAME_GOODS_EXIST.getResult());
        }
        return save(goodsInfo);
    }

    @Override
    public boolean updateGoodsInfo(GoodsEditParam editParam) {
        GoodsInfo goodsInfo = copyProperties(editParam);
        GoodsCategory category = categoryService.getById(goodsInfo.getGoodCategoryId());
        // 1. 校验分类是否为三级分类
        if (category == null || category.getCategoryLevel() != 3){
            throw new HeMaException(ServiceResultEnum.GOODS_CATEGORY_ERROR.getResult());
        }
        // 2. 查询是否存在该商品
        GoodsInfo goods = getById(goodsInfo.getGoodId());
        if (goods == null){
            throw new HeMaException(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        // 3. 判断相同分类下是否有相同名字且id不同的商品
        QueryWrapper<GoodsInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("good_name",goodsInfo.getGoodName()).
                eq("good_category_id",goodsInfo.getGoodCategoryId()).ne("good_id",goodsInfo.getGoodId());
        GoodsInfo info = getOne(queryWrapper);
        if (info != null){
            throw new HeMaException(ServiceResultEnum.SAME_GOODS_EXIST.getResult());
        }
        return updateById(goodsInfo);
    }

    @Override
    public boolean updateStatus(int sellStatus, BatchIdParam idParam) {
        // 1. 是否存在商品
        QueryWrapper<GoodsInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("good_id", Arrays.asList(idParam.getIds()));
        List<GoodsInfo> goodsInfos = list(queryWrapper);
        if ( goodsInfos.size() != idParam.getIds().length ){
            throw new HeMaException(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        // 2. 更改商品状态
        goodsInfos.forEach((goodsInfo -> goodsInfo.setGoodSellStatus(sellStatus)));
        return updateBatchById(goodsInfos);
    }

    private GoodsInfo copyProperties(Object obj){
        GoodsInfo goodsInfo = new GoodsInfo();
        BeanUtils.copyProperties(obj,goodsInfo);
        goodsInfo.setGoodCarousel(goodsInfo.getGoodImg());
        goodsInfo.setCreateTime(new Date());
        goodsInfo.setUpdateTime(new Date());
        return goodsInfo;
    }
}




