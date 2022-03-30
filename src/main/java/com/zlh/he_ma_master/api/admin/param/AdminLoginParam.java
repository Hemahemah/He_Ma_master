package com.zlh.he_ma_master.api.admin.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
/**
 * 接收登录参数
 */
public class AdminLoginParam implements Serializable {

    @NotEmpty(message = "用户名不能为空")
    private String userName;

    @NotEmpty(message = "密码不能为空")
    private String passwordMd5;
}
