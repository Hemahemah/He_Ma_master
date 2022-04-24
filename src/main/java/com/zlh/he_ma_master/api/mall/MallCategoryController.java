package com.zlh.he_ma_master.api.mall;

import com.zlh.he_ma_master.api.mall.vo.MallIndexCategoryVO;
import com.zlh.he_ma_master.service.GoodsCategoryService;
import com.zlh.he_ma_master.utils.Result;
import com.zlh.he_ma_master.utils.ResultGenerator;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author lh
 */
@RestController
@RequestMapping("/he_ma_api")
public class MallCategoryController {

    @Resource
    private GoodsCategoryService goodsCategoryService;



    @GetMapping("/categories")
    public Result<List<MallIndexCategoryVO>> getCategory(){
        List<MallIndexCategoryVO> mallIndexCategoryVos = goodsCategoryService.getCategoryForIndex();
        if (CollectionUtils.isEmpty(mallIndexCategoryVos)){
            return ResultGenerator.getFailResult("获取分类数据失败!");
        }else {
            return ResultGenerator.getSuccessResult(mallIndexCategoryVos);
        }
    }
}
