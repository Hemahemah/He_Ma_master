package com.zlh.he_ma_master.api.mall.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lh
 */
@Data
public class MallUserAddressVO implements Serializable {

    /**
     * 地址编号
     */
    private Long addressId;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 收件人姓名
     */
    private String userName;

    /**
     * 收件人手机号
     */
    private String userPhone;

    /**
     * 省
     */
    private String provinceName;

    /**
     * 城
     */
    private String cityName;

    /**
     * 区
     */
    private String regionName;

    /**
     * 收件详细地址(街道/楼宇/单元)
     */
    private String detailAddress;

    /**
     * 是否为默认 0-非默认 1-是默认
     */
    private Integer defaultFlag;
}
