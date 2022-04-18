package com.zlh.he_ma_master.api.mall;

import com.zlh.he_ma_master.api.mall.param.MallUserAddressAddParam;
import com.zlh.he_ma_master.api.mall.param.MallUserAddressUpdateParam;
import com.zlh.he_ma_master.api.mall.vo.MallUserAddressVO;
import com.zlh.he_ma_master.config.annotation.TokenToMallUser;
import com.zlh.he_ma_master.entity.MallUser;
import com.zlh.he_ma_master.service.UserAddressService;
import com.zlh.he_ma_master.utils.Result;
import com.zlh.he_ma_master.utils.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author lh
 */
@RestController
@RequestMapping("/he_ma_api")
public class MallUserAddressController {

    @Resource
    private UserAddressService userAddressService;

    private static final Logger logger = LoggerFactory.getLogger(MallUserAddressController.class);

    @GetMapping("/address")
    public Result<List<MallUserAddressVO>> getAddressList(@TokenToMallUser MallUser mallUser){
        logger.info("get address api,user={}",mallUser.getUserId());
        List<MallUserAddressVO> mallUserAddressVos = userAddressService.getAddress(mallUser.getUserId());
        if (mallUserAddressVos != null){
            return ResultGenerator.getSuccessResult(mallUserAddressVos);
        }else {
            return ResultGenerator.getFailResult("获取地址失败");
        }
    }

    @GetMapping("/address/{addressId}")
    public Result<MallUserAddressVO> getAddressDetail(@TokenToMallUser MallUser mallUser, @PathVariable Long addressId){
        logger.info("get address detail api,user={}",mallUser.getUserId());
        MallUserAddressVO mallUserAddressVo = userAddressService.getAddressDetail(addressId);
        return ResultGenerator.getSuccessResult(mallUserAddressVo);
    }

    @GetMapping("/address/default")
    public Result<MallUserAddressVO> getAddressDefault(@TokenToMallUser MallUser mallUser){
        logger.info("get address detail api,user={}",mallUser.getUserId());
        MallUserAddressVO mallUserAddressVo = userAddressService.getAddressDefault(mallUser.getUserId());
        return ResultGenerator.getSuccessResult(mallUserAddressVo);
    }

    @PostMapping("/address")
    public Result<String> addAddress(@TokenToMallUser MallUser mallUser, @RequestBody @Valid MallUserAddressAddParam addressAddParam){
        logger.info("add address api,user={}",mallUser.getUserId());
        if (userAddressService.addAddress(addressAddParam, mallUser.getUserId())){
            return ResultGenerator.getSuccessResult();
        }else {
            return ResultGenerator.getFailResult("添加地址失败");
        }
    }

    @PutMapping("/address")
    public Result<String> updateAddress(@TokenToMallUser MallUser mallUser, @RequestBody @Valid MallUserAddressUpdateParam updateParam){
        logger.info("update address api,user={}",mallUser.getUserId());
        if (userAddressService.updateAddress(updateParam, mallUser.getUserId())){
            return ResultGenerator.getSuccessResult();
        }else {
            return ResultGenerator.getFailResult("添加地址失败");
        }
    }

    @DeleteMapping("/address/{addressId}")
    public Result<String> deleteAddress(@TokenToMallUser MallUser mallUser, @PathVariable Long addressId){
        logger.info("update address api,user={}",mallUser.getUserId());
        if (userAddressService.deleteAddress(addressId, mallUser.getUserId())){
            return ResultGenerator.getSuccessResult();
        }else {
            return ResultGenerator.getFailResult("删除地址失败");
        }
    }
}
