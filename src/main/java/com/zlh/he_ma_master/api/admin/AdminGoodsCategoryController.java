package com.zlh.he_ma_master.api.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlh.he_ma_master.api.admin.param.BatchIdParam;
import com.zlh.he_ma_master.api.admin.param.CategoryAddParam;
import com.zlh.he_ma_master.api.admin.param.CategoryEditParam;
import com.zlh.he_ma_master.config.annotation.TokenToAdminUser;
import com.zlh.he_ma_master.entity.AdminUserToken;
import com.zlh.he_ma_master.entity.GoodsCategory;
import com.zlh.he_ma_master.service.GoodsCategoryService;
import com.zlh.he_ma_master.utils.Result;
import com.zlh.he_ma_master.utils.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;

/**
 * @author lh
 */
@RestController
@RequestMapping("/he_ma")
public class AdminGoodsCategoryController {

    @Resource
    private GoodsCategoryService goodsCategoryService;

    private static final Logger logger = LoggerFactory.getLogger(AdminGoodsCategoryController.class);

    @GetMapping("/categories")
    public Result getCategoryPage(@RequestParam Integer pageNumber, @RequestParam Integer pageSize,
                                  @RequestParam Integer categoryLevel, @RequestParam Long parentId,
                                  @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser GoodsCategory api, adminUserId={}",adminUserToken.getUserId());
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10 || categoryLevel == null
                || categoryLevel < 0 || categoryLevel > 3 || parentId == null || parentId < 0) {
            return ResultGenerator.getFailResult("分页参数异常！");
        }
        Page<GoodsCategory> categoryPage = goodsCategoryService.getCategoryPage(pageNumber,pageSize,categoryLevel,parentId);
        if (categoryPage == null){
            return ResultGenerator.getFailResult("分类数据获取异常!");
        }
        return ResultGenerator.getSuccessResult(categoryPage);
    }

    @GetMapping("/categories/{id}")
    public Result<GoodsCategory> getCategory(@PathVariable Long id){
        GoodsCategory category = goodsCategoryService.getById(id);
        if (category != null){
            return ResultGenerator.getSuccessResult(category);
        }else {
            return ResultGenerator.getFailResult("获取详情失败!");
        }
    }

    @PostMapping("/categories")
    public Result saveCategory(@RequestBody @Valid CategoryAddParam categoryAddParam, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser GoodsCategory api, adminUserId={}",adminUserToken.getUserId());
         if (goodsCategoryService.saveCategory(categoryAddParam)){
             return ResultGenerator.getSuccessResult();
         }else {
             return ResultGenerator.getFailResult("添加失败!");
         }
    }

    @PutMapping("/categories")
    public Result updateCategory(@RequestBody @Valid CategoryEditParam editParam, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser GoodsCategory api, adminUserId={}",adminUserToken.getUserId());
        if(goodsCategoryService.updateCategory(editParam)){
            return ResultGenerator.getSuccessResult();
        }else {
            return ResultGenerator.getFailResult();
        }
    }

    @DeleteMapping("/categories")
    public Result deleteCategory(@RequestBody @Valid BatchIdParam idParam,@TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser GoodsCategory api, adminUserId={}",adminUserToken.getUserId());
        if (goodsCategoryService.removeCategory(idParam)){
            return ResultGenerator.getSuccessResult();
        }else {
            return ResultGenerator.getFailResult();
        }
    }
}
