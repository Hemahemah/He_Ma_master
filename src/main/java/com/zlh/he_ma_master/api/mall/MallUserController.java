package com.zlh.he_ma_master.api.mall;

import com.zlh.he_ma_master.api.mall.param.MallUserLoginParam;
import com.zlh.he_ma_master.api.mall.param.MallUserUpdateParam;
import com.zlh.he_ma_master.api.mall.vo.MallUserVO;
import com.zlh.he_ma_master.config.annotation.TokenToMallUser;
import com.zlh.he_ma_master.entity.MallUser;
import com.zlh.he_ma_master.service.MallUserService;
import com.zlh.he_ma_master.service.MallUserTokenService;
import com.zlh.he_ma_master.utils.Constants;
import com.zlh.he_ma_master.utils.NumberUtil;
import com.zlh.he_ma_master.utils.Result;
import com.zlh.he_ma_master.utils.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author lh
 */
@RestController
@RequestMapping("/he_ma")
public class MallUserController {

    @Resource
    private MallUserService mallUserService;

    @Resource
    private MallUserTokenService mallUserTokenService;

    private static final Logger logger = LoggerFactory.getLogger(MallUserController.class);

    @PostMapping("/user/login")
    public Result<String> login(@RequestBody @Valid MallUserLoginParam loginParam){
        // 校验用户名是否为手机号
        if (!NumberUtil.isPhone(loginParam.getLoginName())){
            return ResultGenerator.getFailResult("请输入正确的手机号!");
        }
        String loginResult = mallUserService.login(loginParam.getLoginName(),loginParam.getPasswordMd5());
        logger.info("login api,loginName={},loginResult={}", loginParam.getLoginName(), loginResult);
        // 返回token则登录成功
        if (StringUtils.hasText(loginResult) && loginResult.length() == Constants.TOKEN_LENGTH){
            return ResultGenerator.getSuccessResult(loginResult);
        }else {
            return ResultGenerator.getFailResult(loginResult);
        }
    }

    @PostMapping("/user/logout")
    public Result<String> logout(@TokenToMallUser MallUser mallUser){
        logger.info("logout api,loginName={}", mallUser.getLoginName());
        if (mallUserTokenService.logout(mallUser.getUserId())){
            return ResultGenerator.getSuccessResult();
        }else {
            return ResultGenerator.getFailResult("logout error");
        }
    }

    @GetMapping("/user/info")
    public Result<MallUserVO> getUserInfo(@TokenToMallUser MallUser mallUser){
        MallUserVO mallUserVO = new MallUserVO();
        BeanUtils.copyProperties(mallUser,mallUserVO);
        return ResultGenerator.getSuccessResult(mallUserVO);
    }

    @PutMapping("/user/info")
    public Result<String> updateInfo(@RequestBody @Valid MallUserUpdateParam userUpdateParam, @TokenToMallUser MallUser mallUser){
        if (mallUserService.updateInfo(userUpdateParam, mallUser.getUserId())){
            return ResultGenerator.getSuccessResult();
        }else {
            return ResultGenerator.getFailResult("修改失败!");
        }
    }



}
