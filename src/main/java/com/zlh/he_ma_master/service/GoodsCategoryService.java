package com.zlh.he_ma_master.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlh.he_ma_master.api.admin.param.BatchIdParam;
import com.zlh.he_ma_master.api.admin.param.CategoryAddParam;
import com.zlh.he_ma_master.api.admin.param.CategoryEditParam;
import com.zlh.he_ma_master.entity.GoodsCategory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* 商品分类服务模块
* @author lh
* @createDate 2022-03-27 19:16:32
*/
public interface GoodsCategoryService extends IService<GoodsCategory> {

    /**
     * 获取商品分类列表
     * @param pageNumber 当前页
     * @param pageSize 分页大小
     * @param categoryLevel 分页等级
     * @param parentId 父分类id
     * @return 分页对象
     */
    Page<GoodsCategory> getCategoryPage(Integer pageNumber, Integer pageSize, Integer categoryLevel, Long parentId);

    /**
     * 添加商品分类
     * @param categoryAddParam 添加分类参数
     * @return true 添加成功
     */
    boolean saveCategory(CategoryAddParam categoryAddParam);

    /**
     * 修改商品分类
     * @param editParam 商品分类参数
     * @return true 修改成功
     */
    boolean updateCategory(CategoryEditParam editParam);

    /**
     * 删除商品分类
     * @param idParam 商品编号
     * @return true成功
     */
    boolean removeCategory(BatchIdParam idParam);
}
