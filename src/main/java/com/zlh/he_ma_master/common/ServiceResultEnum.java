package com.zlh.he_ma_master.common;

public enum ServiceResultEnum {

    ERROR("error"),

    SUCCESS("success"),

    LOGIN_ERROR("登录失败!"),

    ADMIN_NOT_LOGIN_ERROR("管理员未登录!"),

    ADMIN_EXPIRE_ERROR("管理员身份过期!"),

    ADMIN_NOT_EXIST("未查询到该管理员!"),

    DATA_NOT_EXIST("未查询到记录！"),

    MESSAGE_UPDATE_ERROR("信息修改失败!"),

    ADMIN_PASSWORD_ERROR("密码错误!"),

    FILE_UPLOAD_ERROR("文件上传失败!"),

    PARAM_EXCEPTION("参数异常!"),

    SAME_CATEGORY_EXIST("已存在同级同名的分类！"),

    GOODS_CATEGORY_ERROR("分类数据异常！"),

    SAME_GOODS_EXIST("已存在相同的商品信息！"),
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
