package com.zlh.he_ma_master.api.admin.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class AdminUpdatePasswordParam implements Serializable {

    @NotEmpty(message = "密码不能为空!")
    private String originalPassword;

    @NotEmpty(message = "新密码不能为空!")
    private String newPassword;
}
