package com.zlh.he_ma_master.api.mall.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lh
 */
@Data
public class MallUserVO implements Serializable {
    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 登录名(手机号)
     */
    private String loginName;

    /**
     * 个性签名
     */
    private String introduceSign;
}
