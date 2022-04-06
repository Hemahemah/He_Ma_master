package com.zlh.he_ma_master.api.mall.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author lh
 */
@Data
public class MallUserLoginParam implements Serializable {

    @NotEmpty(message = "登录名不能为空")
    private String loginName;

    @NotEmpty(message = "密码不能为空")
    private String passwordMd5;
}
