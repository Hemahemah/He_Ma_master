package com.zlh.he_ma_master.service.impl;

import java.util.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlh.he_ma_master.api.admin.param.BatchIdParam;
import com.zlh.he_ma_master.api.admin.param.CategoryAddParam;
import com.zlh.he_ma_master.api.admin.param.CategoryEditParam;
import com.zlh.he_ma_master.common.HeMaException;
import com.zlh.he_ma_master.common.ServiceResultEnum;
import com.zlh.he_ma_master.entity.GoodsCategory;
import com.zlh.he_ma_master.service.GoodsCategoryService;
import com.zlh.he_ma_master.dao.GoodsCategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author lh
* @createDate 2022-03-27 19:16:32
*/
@Service
public class GoodsCategoryServiceImpl extends ServiceImpl<GoodsCategoryMapper, GoodsCategory>
    implements GoodsCategoryService{

    @Override
    public Page<GoodsCategory> getCategoryPage(Integer pageNumber, Integer pageSize, Integer categoryLevel, Long parentId) {
        QueryWrapper<GoodsCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",parentId).eq("category_level",categoryLevel).orderByDesc("category_rank");
        return page(new Page<>(pageNumber, pageSize), queryWrapper);
    }

    @Override
    public boolean saveCategory(CategoryAddParam categoryAddParam) {
        // 1. 校验是否有相同分类
        QueryWrapper<GoodsCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_level",categoryAddParam.getCategoryLevel()).eq("category_name",categoryAddParam.getCategoryName());
        GoodsCategory category = getOne(queryWrapper);
        if (category != null){
            throw new HeMaException(ServiceResultEnum.SAME_CATEGORY_EXIST.getResult());
        }
        // 2. 保存
        GoodsCategory goodsCategory = new GoodsCategory();
        goodsCategory.setCategoryLevel(categoryAddParam.getCategoryLevel());
        goodsCategory.setParentId(categoryAddParam.getParentId());
        goodsCategory.setCategoryName(categoryAddParam.getCategoryName());
        goodsCategory.setCategoryRank(categoryAddParam.getCategoryRank());
        goodsCategory.setCreateTime(new Date());
        goodsCategory.setUpdateTime(new Date());
        return save(goodsCategory);
    }

    @Override
    public boolean updateCategory(CategoryEditParam editParam) {
        // 1. 查询是否有此分类
        GoodsCategory category = getById(editParam.getCategoryId());
        if (category == null){
            throw new HeMaException(ServiceResultEnum.PARAM_EXCEPTION.getResult());
        }
        // 2. 查询是否有相同分类名称且不是一个id的分类
        QueryWrapper<GoodsCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_level",editParam.getCategoryLevel()).
                eq("category_name",editParam.getCategoryName()).
                ne("category_id",editParam.getCategoryId());
        GoodsCategory goodsCategory = getOne(queryWrapper);
        if (goodsCategory != null){
            throw new HeMaException(ServiceResultEnum.SAME_CATEGORY_EXIST.getResult());
        }
        GoodsCategory updateCategory = new GoodsCategory();
        updateCategory.setCategoryId(editParam.getCategoryId());
        updateCategory.setCategoryLevel(editParam.getCategoryLevel());
        updateCategory.setParentId(editParam.getParentId());
        updateCategory.setCategoryName(editParam.getCategoryName());
        updateCategory.setCategoryRank(editParam.getCategoryRank());
        updateCategory.setUpdateTime(new Date());
        return updateById(updateCategory);
    }

    // todo warn Transaction not enabled ??
    @Override
    public boolean removeCategory(BatchIdParam idParam) {
        // 1. 如果不存在id参数,直接返回
        if (idParam.getIds().length == 0){
            return false;
        }
        QueryWrapper<GoodsCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("parent_id", Arrays.asList(idParam.getIds()));
        // 2. 查询标签是否存在子标签
        List<GoodsCategory> categoryList = list(queryWrapper);
        List<Long> idList = new ArrayList<>();
        categoryList.forEach((category)-> idList.add(category.getCategoryId()));
        // 3. 递归删除
        BatchIdParam batchIdParam = new BatchIdParam(idList.toArray(new Long[]{}));
        removeCategory(batchIdParam);
        // 4. 删除标签
        return removeBatchByIds(Arrays.asList(idParam.getIds()));
    }

}




