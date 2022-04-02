package com.zlh.he_ma_master.api.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
            // todo update
        }
        return null;
    }
}
