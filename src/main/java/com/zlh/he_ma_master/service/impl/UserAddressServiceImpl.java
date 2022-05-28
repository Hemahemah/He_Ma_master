package com.zlh.he_ma_master.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlh.he_ma_master.api.mall.param.MallUserAddressAddParam;
import com.zlh.he_ma_master.api.mall.param.MallUserAddressUpdateParam;
import com.zlh.he_ma_master.api.mall.vo.MallUserAddressVO;
import com.zlh.he_ma_master.common.HeMaException;
import com.zlh.he_ma_master.common.ServiceResultEnum;
import com.zlh.he_ma_master.dao.UserAddressMapper;
import com.zlh.he_ma_master.entity.UserAddress;
import com.zlh.he_ma_master.service.UserAddressService;
import com.zlh.he_ma_master.utils.NumberUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
* @author lh
* @createDate 2022-04-17 19:42:12
*/
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress>
    implements UserAddressService{

    @Override
    public List<MallUserAddressVO> getAddress(Long userId) {
        QueryWrapper<UserAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<UserAddress> userAddressList = list(queryWrapper);
        List<MallUserAddressVO> mallUserAddressVoList = new ArrayList<>();
       userAddressList.forEach(userAddress -> {
           //类型转换
           MallUserAddressVO mallUserAddressVO = new MallUserAddressVO();
           BeanUtils.copyProperties(userAddress, mallUserAddressVO);
           mallUserAddressVoList.add(mallUserAddressVO);
        });
        return mallUserAddressVoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addAddress(MallUserAddressAddParam addressAddParam, Long userId) {
        // 1. 校验手机号
        if (!NumberUtil.isPhone(addressAddParam.getUserPhone())){
            throw new HeMaException(ServiceResultEnum.PARAM_EXCEPTION.getResult());
        }
        // 2. 是否存在默认地址
        if (addressAddParam.getDefaultFlag() == 1){
            QueryWrapper<UserAddress> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId).eq("default_flag", 1);
            UserAddress userAddress = getOne(queryWrapper);
            //设置为非默认
            if (userAddress != null){
                userAddress.setDefaultFlag(0);
                userAddress.setUpdateTime(new Date());
                updateById(userAddress);
            }
        }
        // 3. 类型转换
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(addressAddParam, userAddress);
        userAddress.setUserId(userId);
        return save(userAddress);
    }

    @Override
    public MallUserAddressVO getAddressDetail(Long addressId) {
        UserAddress userAddress = getById(addressId);
        // 1. 校验地址是否存在
        if (userAddress == null){
            throw new HeMaException(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        // 2. 类型转换
        MallUserAddressVO userAddressVo = new MallUserAddressVO();
        BeanUtils.copyProperties(userAddress, userAddressVo);
        return userAddressVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAddress(MallUserAddressUpdateParam updateParam, Long userId) {
        // 1. 校验手机号
        if (!NumberUtil.isPhone(updateParam.getUserPhone())){
            throw new HeMaException(ServiceResultEnum.PARAM_EXCEPTION.getResult());
        }
        // 2. 校验地址是否存在
        QueryWrapper<UserAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("address_id", updateParam.getAddressId()).eq("user_id", userId);
        UserAddress userAddress = getOne(queryWrapper);
        if (userAddress == null){
            throw new HeMaException(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        // 3. 是否设置为默认地址
        if (updateParam.getDefaultFlag() == 1){
            QueryWrapper<UserAddress> addressQueryWrapper = new QueryWrapper<>();
            addressQueryWrapper.eq("user_id", userId).eq("default_flag", 1);
            UserAddress address = getOne(addressQueryWrapper);
            //设置为非默认
            if (address != null && !userAddress.equals(address)){
                address.setDefaultFlag(0);
                address.setUpdateTime(new Date());
                updateById(address);
            }
        }
        // 4. 修改数据
        BeanUtils.copyProperties(updateParam, userAddress);
        userAddress.setUpdateTime(new Date());
        return updateById(userAddress);
    }

    @Override
    public boolean deleteAddress(Long addressId, Long userId) {
        QueryWrapper<UserAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("address_id", addressId).eq("user_id", userId);
        return remove(queryWrapper);
    }

    @Override
    public MallUserAddressVO getAddressDefault(Long userId) {
        QueryWrapper<UserAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("default_flag", 1);
        UserAddress userAddress = getOne(queryWrapper);
        if (userAddress == null){
            return null;
        }
        MallUserAddressVO mallUserAddressVo = new MallUserAddressVO();
        BeanUtils.copyProperties(userAddress, mallUserAddressVo);
        return mallUserAddressVo;
    }


}




