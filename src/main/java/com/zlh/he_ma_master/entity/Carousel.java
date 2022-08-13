package com.zlh.he_ma_master.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 *
 * @TableName carousel
 */
@TableName(value ="carousel")
@Data
public class Carousel implements Serializable {
    /**
     * 轮播图id
     */
    @TableId(type = IdType.AUTO)
    private Integer carouselId;

    /**
     * 轮播图url
     */
    private String carouselUrl;

    /**
     * 跳转地址
     */
    private String redirectUrl;

    /**
     * 排序值，值越大越靠前
     */
    private Integer carouselRank;

    /**
     * 创建者id
     */
    private Integer createUser;

    /**
     * 修改者id
     */
    private Integer updateUser;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 是否删除,0-未删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Carousel other = (Carousel) that;
        return (this.getCarouselId() == null ? other.getCarouselId() == null : this.getCarouselId().equals(other.getCarouselId()))
            && (this.getCarouselUrl() == null ? other.getCarouselUrl() == null : this.getCarouselUrl().equals(other.getCarouselUrl()))
            && (this.getRedirectUrl() == null ? other.getRedirectUrl() == null : this.getRedirectUrl().equals(other.getRedirectUrl()))
            && (this.getCarouselRank() == null ? other.getCarouselRank() == null : this.getCarouselRank().equals(other.getCarouselRank()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCarouselId() == null) ? 0 : getCarouselId().hashCode());
        result = prime * result + ((getCarouselUrl() == null) ? 0 : getCarouselUrl().hashCode());
        result = prime * result + ((getRedirectUrl() == null) ? 0 : getRedirectUrl().hashCode());
        result = prime * result + ((getCarouselRank() == null) ? 0 : getCarouselRank().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", carouselId=").append(carouselId);
        sb.append(", carouselUrl=").append(carouselUrl);
        sb.append(", redirectUrl=").append(redirectUrl);
        sb.append(", carouselRank=").append(carouselRank);
        sb.append(", createUser=").append(createUser);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
