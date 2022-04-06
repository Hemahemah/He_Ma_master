package com.zlh.he_ma_master.api.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlh.he_ma_master.api.admin.param.BatchIdParam;
import com.zlh.he_ma_master.api.admin.param.ConfigAddParam;
import com.zlh.he_ma_master.api.admin.param.ConfigEditParam;
import com.zlh.he_ma_master.config.annotation.TokenToAdminUser;
import com.zlh.he_ma_master.entity.AdminUserToken;
import com.zlh.he_ma_master.entity.IndexConfig;
import com.zlh.he_ma_master.service.IndexConfigService;
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
public class IndexConfigController {

    @Resource
    private IndexConfigService indexConfigService;

    private static final Logger logger = LoggerFactory.getLogger(IndexConfigController.class);

    @RequestMapping("/indexConfigs")
    public Result getConfigsPage(@RequestParam("pageNumber") Integer pageNumber,
                                 @RequestParam("pageSize") Integer pageSize,
                                 @RequestParam("configType") Integer configType,
                                 @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser indexConfig api, adminUserId={}",adminUserToken.getUserId());
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10){
            return ResultGenerator.getFailResult("分页参数异常");
        }
        Page<IndexConfig> indexConfigPage = indexConfigService.getPages(pageNumber,pageSize,configType);
        if (indexConfigPage == null){
            return ResultGenerator.getFailResult("未查询到数据!");
        }else {
            return ResultGenerator.getSuccessResult(indexConfigPage);
        }
    }

    @GetMapping("/indexConfigs/{id}")
    public Result getConfigInfo(@PathVariable String id, @TokenToAdminUser AdminUserToken adminUserToken) {
        logger.info("adminUser indexConfig api, adminUserId={}", adminUserToken.getUserId());
        IndexConfig indexConfig = indexConfigService.getById(id);
        if (indexConfig == null) {
           return ResultGenerator.getFailResult("为查询到该数据");
        } else {
           return ResultGenerator.getSuccessResult(indexConfig);
        }
    }

    @PutMapping("/indexConfigs")
    public Result updateConfig(@RequestBody @Valid ConfigEditParam editParam, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser indexConfig api, adminUserId={}", adminUserToken.getUserId());
        if (indexConfigService.updateConfig(editParam)){
            return ResultGenerator.getSuccessResult();
        } else {
            return ResultGenerator.getFailResult("修改失败,请查看控制台异常信息");
        }
    }

    @PostMapping("/indexConfigs")
    public Result addIndexConfig(@RequestBody @Valid ConfigAddParam addParam, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser indexConfig api, adminUserId={}", adminUserToken.getUserId());
        if (indexConfigService.addConfig(addParam)){
            return ResultGenerator.getSuccessResult();
        } else {
            return ResultGenerator.getFailResult("修改失败,请查看控制台异常信息");
        }
    }

    @DeleteMapping("/indexConfigs")
    public Result deleteIndexConfig(@RequestBody BatchIdParam idParam, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser indexConfig api, adminUserId={}", adminUserToken.getUserId());
        if (idParam == null || idParam.getIds().length < 1) {
            return ResultGenerator.getFailResult("参数异常");
        }
        if (indexConfigService.removeConfig(idParam.getIds())){
            return ResultGenerator.getSuccessResult();
        } else {
            return ResultGenerator.getFailResult("删除失败,请查看控制台异常信息");
        }
    }
}
