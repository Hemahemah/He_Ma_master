package com.zlh.he_ma_master.api.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlh.he_ma_master.api.admin.param.BatchIdParam;
import com.zlh.he_ma_master.config.annotation.TokenToAdminUser;
import com.zlh.he_ma_master.entity.AdminUserToken;
import com.zlh.he_ma_master.entity.MallUser;
import com.zlh.he_ma_master.service.MallUserService;
import com.zlh.he_ma_master.utils.Constants;
import com.zlh.he_ma_master.utils.Result;
import com.zlh.he_ma_master.utils.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * @author lh
 */
@RestController
@RequestMapping("/he_ma")
public class AdminCustomerController {

    @Resource
    private MallUserService mallUserService;

    private static final Logger logger = LoggerFactory.getLogger(AdminCustomerController.class);

    @GetMapping("/users")
    public Result<Page<MallUser>> getUsersList(@RequestParam(required = false) Integer pageNumber,
                                               @RequestParam(required = false) Integer pageSize,
                                               @TokenToAdminUser AdminUserToken adminUserToken){
    logger.info("get customer api,adminUser={}",adminUserToken.getUserId());
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < Constants.PAGE_MIN_SIZE){
            return ResultGenerator.getFailResult("分页参数异常");
        }
        Page<MallUser> mallUserPage = mallUserService.getUsersList(pageNumber, pageSize);
        if (mallUserPage != null){
            return ResultGenerator.getSuccessResult(mallUserPage);
        }else {
            return ResultGenerator.getFailResult("获取用户数据失败!");
        }
    }

    @PutMapping("/users/{lockStatus}")
    public Result<String> handleSolve(@PathVariable Integer lockStatus, @RequestBody BatchIdParam idParam, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("update customerStatus api,adminUser={}",adminUserToken.getUserId());
        if (mallUserService.updateUserStatus(lockStatus, idParam.getIds())){
            return ResultGenerator.getSuccessResult();
        }else {
            return ResultGenerator.getFailResult("修改用户状态失败!");
        }
    }
}
