package com.zlh.he_ma_master.common;

/**
 * @author lh
 */

public enum ServiceResultEnum {

    /**
     *
     */
    ERROR("error"),

    /**
     *
     */
    SUCCESS("success"),

    /**
     *
     */
    LOGIN_ERROR("登录失败!"),

    /**
     *
     */
    ADMIN_NOT_LOGIN_ERROR("管理员未登录!"),

    /**
     *
     */
    ADMIN_EXPIRE_ERROR("管理员身份过期!"),

    /**
     *
     */
    ADMIN_NOT_EXIST("未查询到该管理员!"),

    /**
     *
     */
    DATA_NOT_EXIST("未查询到记录！"),

    /**
     *
     */
    MESSAGE_UPDATE_ERROR("信息修改失败!"),

    /**
     *
     */
    ADMIN_PASSWORD_ERROR("密码错误!"),

    /**
     *
     */
    FILE_UPLOAD_ERROR("文件上传失败!"),

    /**
     * 参数异常信息
     */
    PARAM_EXCEPTION("参数异常!"),

    /**
     * 分类异常信息
     */
    SAME_CATEGORY_EXIST("已存在同级同名的分类！"),

    /**
     *
     */
    GOODS_CATEGORY_ERROR("分类数据异常！"),

    /**
     *
     */
    SAME_GOODS_EXIST("已存在相同的商品信息！"),

    /**
     *
     */
    GOODS_NOT_EXIST("商品不存在"),

    /**
     * 配置异常信息
     */
    SAME_INDEX_CONFIG_EXIST("已存在相同的首页配置项！"),

    /**
     * 用户禁止登录信息
     */
    LOGIN_USER_LOCKED_ERROR("用户已被禁止登录！"),

    /**
     * 用户状态异常
     */
    USER_STATUS_ERROR("用户状态异常"),

    /**
     * token无效信息
     */
    TOKEN_EXPIRE_ERROR("无效认证！请重新登录！"),

    /**
     * 用户未登录
     */
    MALL_USER_NOT_LOGIN_ERROR("未登录!"),

    /**
     * 手机号已被注册
     */
    SAME_LOGIN_NAME_EXIST("手机号已被注册！"),


    /**
     * 购物车商品存在信息
     */
    CART_ITEM_EXIST_ERROR("已存在！无需重复添加！"),

    /**
     * 用户简介信息
     */
    USER_INTRO("暂无简介"),

    /**
     * 用户无权限信息
     */
    NO_PERMISSION_ERROR("无权限！"),

    /**
     * 商品下架信息
     */
    GOODS_STATUS_DOWN("商品已下架"),

    /**
     * 购物车数据异常信息
     */
    SHOPPING_ITEM_ERROR("购物车数据异常！"),

    /**
     * 商品库存不足信息
     */
    SHOPPING_ITEM_COUNT_ERROR("库存不足！"),

    /**
     * 商品数据异常信息
     */
    GOODS_ITEM_ERROR("商品数据异常!"),

    /**
     * 商品数据异常信息
     */
    GOODS_ITEM_COUNT_ERROR("商品库存异常!:"),

    /**
     * 订单价格异常信息
     */
    ORDER_PRICE_ERROR("订单价格异常！"),

    /**
     * 订单状态异常信息
     */
    ORDER_STATUS_ERROR("订单状态异常！"),

    /**
     * 生成订单异常信息
     */
    SAVE_ORDER_ERROR("生成订单异常"),
    ;

    private String result;

    ServiceResultEnum(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
