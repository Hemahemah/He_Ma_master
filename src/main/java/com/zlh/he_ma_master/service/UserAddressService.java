package com.zlh.he_ma_master.service;

import com.zlh.he_ma_master.api.mall.param.MallUserAddressAddParam;
import com.zlh.he_ma_master.api.mall.param.MallUserAddressUpdateParam;
import com.zlh.he_ma_master.api.mall.vo.MallUserAddressVO;
import com.zlh.he_ma_master.entity.UserAddress;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* 用户地址模块
* @author lh
* @createDate 2022-04-17 19:42:12
*/
public interface UserAddressService extends IService<UserAddress> {

    /**
     * 获取地址信息
     * @param userId 用户编号
     * @return UserAddressVO
     */
    List<MallUserAddressVO> getAddress(Long userId);

    /**
     * 添加地址
     * @param addressAddParam 添加地址参数
     * @param userId 用户编号
     * @return true 添加成功
     */
    boolean addAddress(MallUserAddressAddParam addressAddParam, Long userId);

    /**
     * 获取地址详情
     * @param addressId 地址编号
     * @return MallUserAddressVO
     */
    MallUserAddressVO getAddressDetail(Long addressId);

    /**
     * 修改地址信息
     * @param updateParam 地址修改参数
     * @param userId 用户Id
     * @return true 修改成功
     */
    boolean updateAddress(MallUserAddressUpdateParam updateParam, Long userId);

    /**
     * 删除地址
     * @param addressId 地址编号
     * @param userId 用户编号
     * @return true 删除成功
     */
    boolean deleteAddress(Long addressId, Long userId);

    /**
     * 获取默认地址
     * @param userId 用户Id
     * @return true 删除成功
     */
    MallUserAddressVO getAddressDefault(Long userId);
}
