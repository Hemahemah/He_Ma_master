package com.zlh.he_ma_master.api.mall.param;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lh
 */
@Data
public class MallUserUpdateParam implements Serializable {

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 密码(MD5)
     */
    private String passwordMd5;

    /**
     * 个性签名
     */
    private String introduceSign;
}
