package com.zlh.he_ma_master.api.admin;

import com.zlh.he_ma_master.api.admin.param.AdminLoginParam;
import com.zlh.he_ma_master.api.admin.param.AdminUpdateNameParam;
import com.zlh.he_ma_master.api.admin.param.AdminUpdatePasswordParam;
import com.zlh.he_ma_master.config.annotation.TokenToAdminUser;
import com.zlh.he_ma_master.entity.AdminUser;
import com.zlh.he_ma_master.entity.AdminUserToken;
import com.zlh.he_ma_master.service.AdminUserService;
import com.zlh.he_ma_master.utils.Constants;
import com.zlh.he_ma_master.utils.Result;
import com.zlh.he_ma_master.utils.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * @author lh
 */
@CrossOrigin
@RestController
@RequestMapping("/he_ma")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    @PostMapping(value = "/adminUser/login")
    public Result<String> login(@RequestBody @Valid AdminLoginParam adminLoginParam){
        // 1. 获取用户token
        String loginResult = adminUserService.login(adminLoginParam.getUserName(), adminLoginParam.getPasswordMd5());
        logger.info("adminUser login api, username={},result={}",adminLoginParam.getUserName(),loginResult);
        if (StringUtils.hasText(loginResult) && loginResult.length() == Constants.TOKEN_LENGTH){
            // 2. 获取成功返回成功响应信息
            Result<String> successResult = ResultGenerator.getSuccessResult();
            successResult.setData(loginResult);
            return successResult;
        }else {
            //否则返回失败响应
            Result<String> failResult = ResultGenerator.getFailResult();
            failResult.setMessage(loginResult);
            return failResult;
        }
    }

    @GetMapping("/adminUser/profile")
    public Result<AdminUser> profile(@TokenToAdminUser AdminUserToken adminUserToken){
        AdminUser adminUser = adminUserService.getById(adminUserToken.getUserId());
        if (adminUser != null){
            Result<AdminUser> successResult = ResultGenerator.getSuccessResult();
            adminUser.setUserPassword("******");
            successResult.setData(adminUser);
            return successResult;
        }
        Result<AdminUser> failResult = ResultGenerator.getFailResult();
        failResult.setMessage("未查询到该管理员");
        return failResult;
    }

    @PutMapping("/adminUser/name")
    public Result<String> updateName(@TokenToAdminUser AdminUserToken adminUserToken,
                                     @RequestBody @Valid AdminUpdateNameParam nameParam){
        if (adminUserService.updateName(adminUserToken,nameParam)){
            return ResultGenerator.getSuccessResult();
        }
        Result<String> failResult = ResultGenerator.getFailResult();
        failResult.setMessage("信息修改失败");
        return failResult;
    }

    @PutMapping("/adminUser/password")
    public Result<String> updatePassword(@TokenToAdminUser AdminUserToken adminUserToken,
                                         @RequestBody @Valid AdminUpdatePasswordParam passwordParam){
        if (adminUserService.updatePassword(adminUserToken,passwordParam)){
            return ResultGenerator.getSuccessResult();
        }
        Result<String> failResult = ResultGenerator.getFailResult();
        failResult.setMessage("信息修改失败");
        return failResult;
    }

    @DeleteMapping("/logout")
    public Result<String> logout(@TokenToAdminUser AdminUserToken adminUserToken){
        if (adminUserService.logout(adminUserToken)){
           return ResultGenerator.getSuccessResult();
        }
        return ResultGenerator.getFailResult();
    }

}
