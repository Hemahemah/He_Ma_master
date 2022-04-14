package com.zlh.he_ma_master.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlh.he_ma_master.api.admin.param.BatchIdParam;
import com.zlh.he_ma_master.api.admin.param.GoodAddParam;
import com.zlh.he_ma_master.api.admin.param.GoodsEditParam;
import com.zlh.he_ma_master.api.mall.vo.MallGoodsInfoVO;
import com.zlh.he_ma_master.api.mall.vo.MallSearchGoodsVO;
import com.zlh.he_ma_master.common.GoodsInfoEnum;
import com.zlh.he_ma_master.common.HeMaException;
import com.zlh.he_ma_master.common.MallCategoryLevelEnum;
import com.zlh.he_ma_master.common.ServiceResultEnum;
import com.zlh.he_ma_master.entity.GoodsCategory;
import com.zlh.he_ma_master.entity.GoodsInfo;
import com.zlh.he_ma_master.service.GoodsCategoryService;
import com.zlh.he_ma_master.service.GoodsInfoService;
import com.zlh.he_ma_master.dao.GoodsInfoMapper;
import com.zlh.he_ma_master.utils.Constants;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
        if (category == null || category.getCategoryLevel() != MallCategoryLevelEnum.LEVEL_THREE.getLevel()){
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
        if (category == null || category.getCategoryLevel() != MallCategoryLevelEnum.LEVEL_THREE.getLevel()){
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
        goodsInfo.setUpdateTime(new Date());
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

    @Override
    public Page<MallSearchGoodsVO> searchGoods(String keyword, Long goodsCategoryId, String orderBy, Integer pageNumber) {
        QueryWrapper<GoodsInfo> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)){
            // 商品简介或商品名是否匹配
            queryWrapper.nested(goodsInfoQueryWrapper ->
                   goodsInfoQueryWrapper.like("good_intro", keyword.trim()).or().like("good_name", keyword.trim()));
        }
       if (goodsCategoryId != null){
            //商品分类
            queryWrapper.eq("good_category_id", goodsCategoryId);
        }
        if (StringUtils.hasText(orderBy)){
            if (GoodsInfoEnum.GOODS_ORDER_NEW.getMessage().equals(orderBy)){
                // 新品按商品id排序
                queryWrapper.orderByDesc("good_id");
            }else if (GoodsInfoEnum.GOODS_ORDER_PRICE.getMessage().equals(orderBy)){
                // 价格按商品价格排序
                queryWrapper.orderByAsc("selling_price");
            }else {
                // 按物品库存排序
                queryWrapper.orderByDesc("stock_num");
            }
        }
        // 上线的商品
        queryWrapper.eq("good_sell_status",Constants.SELL_STATUS_UP);
        Page<GoodsInfo> goodsInfoPage = new Page<>(pageNumber, Constants.GOODS_SEARCH_PAGE_LIMIT);
        goodsInfoPage = page(goodsInfoPage, queryWrapper);
        Page<MallSearchGoodsVO> searchGoodsVoPage = new Page<>();
        //转换类型
        BeanUtils.copyProperties(goodsInfoPage, searchGoodsVoPage);
        List<MallSearchGoodsVO> mallSearchGoodsVoList = new ArrayList<>();
        goodsInfoPage.getRecords().forEach(goodsInfo -> {
            // 商品名称过长处理
            if (goodsInfo.getGoodName().length() > Constants.NAME_MAX_LENGTH){
                goodsInfo.setGoodName(goodsInfo.getGoodName().substring(0,30)+"...");
            }
            if (goodsInfo.getGoodIntro().length() > Constants.NAME_MAX_LENGTH){
                goodsInfo.setGoodIntro(goodsInfo.getGoodIntro().substring(0,30)+"...");
            }
            MallSearchGoodsVO searchGoodsVo = new MallSearchGoodsVO();
            BeanUtils.copyProperties(goodsInfo, searchGoodsVo);
            mallSearchGoodsVoList.add(searchGoodsVo);
        });
        searchGoodsVoPage.setRecords(mallSearchGoodsVoList);
        return searchGoodsVoPage;
    }

    @Override
    public MallGoodsInfoVO getGoodsDetail(Long goodId) {
        GoodsInfo goodsInfo = getById(goodId);
        // 商品不存在或商品已下架
        if (goodsInfo == null || Constants.SELL_STATUS_DOWN == goodsInfo.getGoodSellStatus()){
            throw new HeMaException(ServiceResultEnum.GOODS_NOT_EXIST.getResult());
        }
        MallGoodsInfoVO mallGoodsInfoVO = new MallGoodsInfoVO();
        BeanUtils.copyProperties(goodsInfo, mallGoodsInfoVO);
        mallGoodsInfoVO.setGoodsCarouselList(goodsInfo.getGoodCarousel().split(","));
        return mallGoodsInfoVO;
    }

    private GoodsInfo copyProperties(Object obj){
        GoodsInfo goodsInfo = new GoodsInfo();
        BeanUtils.copyProperties(obj,goodsInfo);
        goodsInfo.setGoodCarousel(goodsInfo.getGoodImg());
        goodsInfo.setCreateTime(new Date());
        return goodsInfo;
    }
}




