package com.zlh.he_ma_master.api.mall.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lh
 */
@Data
public class MallUserAddressAddParam implements Serializable {

    @NotEmpty(message = "用户名不能为空")
    private String userName;

    @NotEmpty(message = "省市区编号不能为空")
    private String areaCode;

    @NotEmpty(message = "手机号不能为空")
    private String userPhone;

    @NotEmpty(message = "省不能为空")
    private String provinceName;

    @NotEmpty(message = "区不能为空")
    private String cityName;

    @NotEmpty(message = "城不能为空")
    private String regionName;

    @NotEmpty(message = "详细地址不能为空")
    private String detailAddress;

    @NotNull(message = "默认设置不能为空")
    private Integer defaultFlag;
}
