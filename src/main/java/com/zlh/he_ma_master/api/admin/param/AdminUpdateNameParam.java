package com.zlh.he_ma_master.api.admin.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class AdminUpdateNameParam implements Serializable {

    @NotEmpty(message = "用户名不能为空!")
    private String loginName;

    @NotEmpty(message = "昵称不能为空!")
    private String nickName;
}
