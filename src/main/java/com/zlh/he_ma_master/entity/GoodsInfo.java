package com.zlh.he_ma_master.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 *
 * @TableName goods_info
 */
@TableName(value ="goods_info")
@Data
public class GoodsInfo implements Serializable {
    /**
     * 商品表主键id
     */
    @TableId(type = IdType.AUTO)
    private Long goodId;

    /**
     * 商品名称
     */
    private String goodName;

    /**
     * 商品简介
     */
    private String goodIntro;

    /**
     * 商品分类id
     */
    private Long goodCategoryId;

    /**
     * 商品主图
     */
    private String goodImg;

    /**
     * 商品轮播图
     */
    private String goodCarousel;

    /**
     * 商品价格
     */
    private Integer originalPrice;

    /**
     * 商品实际售价
     */
    private Integer sellingPrice;

    /**
     * 商品库存数量
     */
    private Integer stockNum;

    /**
     * 商品详情
     */
    private String goodDetailContent;

    /**
     * 商品标签
     */
    private String tag;

    /**
     * 商品状态 0-下架 1-上架
     */
    private Integer goodSellStatus;

    /**
     * 添加者主键id
     */
    private Integer createUser;

    /**
     * 商品添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改者主键id
     */
    private Integer updateUser;

    /**
     * 商品修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     *  是否删除 0-未删除 1-删除
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
        GoodsInfo other = (GoodsInfo) that;
        return (this.getGoodId() == null ? other.getGoodId() == null : this.getGoodId().equals(other.getGoodId()))
            && (this.getGoodName() == null ? other.getGoodName() == null : this.getGoodName().equals(other.getGoodName()))
            && (this.getGoodIntro() == null ? other.getGoodIntro() == null : this.getGoodIntro().equals(other.getGoodIntro()))
            && (this.getGoodCategoryId() == null ? other.getGoodCategoryId() == null : this.getGoodCategoryId().equals(other.getGoodCategoryId()))
            && (this.getGoodImg() == null ? other.getGoodImg() == null : this.getGoodImg().equals(other.getGoodImg()))
            && (this.getGoodCarousel() == null ? other.getGoodCarousel() == null : this.getGoodCarousel().equals(other.getGoodCarousel()))
            && (this.getOriginalPrice() == null ? other.getOriginalPrice() == null : this.getOriginalPrice().equals(other.getOriginalPrice()))
            && (this.getSellingPrice() == null ? other.getSellingPrice() == null : this.getSellingPrice().equals(other.getSellingPrice()))
            && (this.getStockNum() == null ? other.getStockNum() == null : this.getStockNum().equals(other.getStockNum()))
            && (this.getGoodDetailContent() == null ? other.getGoodDetailContent() == null : this.getGoodDetailContent().equals(other.getGoodDetailContent()))
            && (this.getTag() == null ? other.getTag() == null : this.getTag().equals(other.getTag()))
            && (this.getGoodSellStatus() == null ? other.getGoodSellStatus() == null : this.getGoodSellStatus().equals(other.getGoodSellStatus()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getGoodId() == null) ? 0 : getGoodId().hashCode());
        result = prime * result + ((getGoodName() == null) ? 0 : getGoodName().hashCode());
        result = prime * result + ((getGoodIntro() == null) ? 0 : getGoodIntro().hashCode());
        result = prime * result + ((getGoodCategoryId() == null) ? 0 : getGoodCategoryId().hashCode());
        result = prime * result + ((getGoodImg() == null) ? 0 : getGoodImg().hashCode());
        result = prime * result + ((getGoodCarousel() == null) ? 0 : getGoodCarousel().hashCode());
        result = prime * result + ((getOriginalPrice() == null) ? 0 : getOriginalPrice().hashCode());
        result = prime * result + ((getSellingPrice() == null) ? 0 : getSellingPrice().hashCode());
        result = prime * result + ((getStockNum() == null) ? 0 : getStockNum().hashCode());
        result = prime * result + ((getGoodDetailContent() == null) ? 0 : getGoodDetailContent().hashCode());
        result = prime * result + ((getTag() == null) ? 0 : getTag().hashCode());
        result = prime * result + ((getGoodSellStatus() == null) ? 0 : getGoodSellStatus().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
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
        sb.append(", goodId=").append(goodId);
        sb.append(", goodName=").append(goodName);
        sb.append(", goodIntro=").append(goodIntro);
        sb.append(", goodCategoryId=").append(goodCategoryId);
        sb.append(", goodImg=").append(goodImg);
        sb.append(", goodCarousel=").append(goodCarousel);
        sb.append(", originalPrice=").append(originalPrice);
        sb.append(", sellingPrice=").append(sellingPrice);
        sb.append(", stockNum=").append(stockNum);
        sb.append(", goodDetailContent=").append(goodDetailContent);
        sb.append(", tag=").append(tag);
        sb.append(", goodSellStatus=").append(goodSellStatus);
        sb.append(", createUser=").append(createUser);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
