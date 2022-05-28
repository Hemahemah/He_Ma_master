package com.zlh.he_ma_master.api.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlh.he_ma_master.api.admin.param.BatchIdParam;
import com.zlh.he_ma_master.api.admin.param.GoodAddParam;
import com.zlh.he_ma_master.api.admin.param.GoodsEditParam;
import com.zlh.he_ma_master.config.annotation.TokenToAdminUser;
import com.zlh.he_ma_master.entity.AdminUserToken;
import com.zlh.he_ma_master.entity.GoodsInfo;
import com.zlh.he_ma_master.service.GoodsInfoService;
import com.zlh.he_ma_master.utils.Constants;
import com.zlh.he_ma_master.utils.Result;
import com.zlh.he_ma_master.utils.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

/**
 * @author lh
 */
@RestController
@RequestMapping("/he_ma")
public class AdminGoodsInfoController {

    @Resource
    private GoodsInfoService goodsInfoService;

    private static final Logger logger = LoggerFactory.getLogger(AdminGoodsInfoController.class);

    @GetMapping("/goods/list")
    public Result<Page<GoodsInfo>> getGoodsPage(@RequestParam Integer pageNumber,
                                                @RequestParam Integer pageSize,
                                                @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser GoodsInfo api, adminUserId={}",adminUserToken.getUserId());
        if ( pageNumber == null || pageSize == null || pageNumber < 1 || pageSize < 10 ){
            return ResultGenerator.getFailResult("分页参数异常!");
        }
         Page<GoodsInfo> goodsInfoPage = goodsInfoService.getGoodsPage(pageNumber,pageSize);
        if (goodsInfoPage == null){
            return ResultGenerator.getFailResult("未查询到数据!");
        }else {
            return ResultGenerator.getSuccessResult(goodsInfoPage);
        }
    }

    @GetMapping("/goods/{id}")
    public Result<Map<String,Object>> getGoods(@PathVariable Long id, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser GoodsInfo api, adminUserId={}",adminUserToken.getUserId());
        Map<String,Object> goodInfoMap = goodsInfoService.getGoodInfo(id);
        if (goodInfoMap == null){
            return ResultGenerator.getFailResult("未查询到该商品!");
        }else {
            return ResultGenerator.getSuccessResult(goodInfoMap);
        }
    }

    @PostMapping("/goods")
    public Result saveGoods(@RequestBody @Valid GoodAddParam goodAddParam, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser GoodsInfo api, adminUserId={}",adminUserToken.getUserId());
        if (goodsInfoService.saveGoodInfo(goodAddParam)){
            return ResultGenerator.getSuccessResult();
        }else {
            return ResultGenerator.getFailResult("保存商品失败!");
        }
    }

    @PutMapping("/goods")
    public Result updateGoods(@RequestBody @Valid GoodsEditParam editParam, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser GoodsInfo api, adminUserId={}",adminUserToken.getUserId());
        if (goodsInfoService.updateGoodsInfo(editParam)) {
            return ResultGenerator.getSuccessResult();
        }else {
            return ResultGenerator.getFailResult("修改商品失败");
        }
    }

    @PutMapping("/goods/status/{sellStatus}")
    public Result updateStatus(@PathVariable int sellStatus, @RequestBody BatchIdParam idParam, @TokenToAdminUser AdminUserToken adminUserToken){
        if (idParam.getIds().length < 1){
            return ResultGenerator.getFailResult("参数异常");
        }
        if (sellStatus != Constants.SELL_STATUS_UP && sellStatus != Constants.SELL_STATUS_DOWN){
            return ResultGenerator.getFailResult("状态参数异常");
        }
        if (goodsInfoService.updateStatus(sellStatus,idParam)){
            return ResultGenerator.getSuccessResult();
        }else {
            return ResultGenerator.getFailResult("更新状态失败");
        }
    }


}
