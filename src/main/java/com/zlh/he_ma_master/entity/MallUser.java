package com.zlh.he_ma_master.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 *
 * @TableName mall_user
 */
@TableName(value ="mall_user")
@Data
public class MallUser implements Serializable {
    /**
     * 用户主键
     */
    @TableId(type = IdType.AUTO)
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 登录名(手机号)
     */
    private String loginName;

    /**
     * 密码(MD5)
     */
    private String passwordMd5;

    /**
     * 个性签名
     */
    private String introduceSign;

    /**
     * 注销标识字段(0-正常 1-已注销)
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 锁定标识字段(0-未锁定 1-已锁定)
     */
    private Integer lockedFlag;

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
        MallUser other = (MallUser) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getNickName() == null ? other.getNickName() == null : this.getNickName().equals(other.getNickName()))
            && (this.getLoginName() == null ? other.getLoginName() == null : this.getLoginName().equals(other.getLoginName()))
            && (this.getPasswordMd5() == null ? other.getPasswordMd5() == null : this.getPasswordMd5().equals(other.getPasswordMd5()))
            && (this.getIntroduceSign() == null ? other.getIntroduceSign() == null : this.getIntroduceSign().equals(other.getIntroduceSign()))
            && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getLockedFlag() == null ? other.getLockedFlag() == null : this.getLockedFlag().equals(other.getLockedFlag()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getNickName() == null) ? 0 : getNickName().hashCode());
        result = prime * result + ((getLoginName() == null) ? 0 : getLoginName().hashCode());
        result = prime * result + ((getPasswordMd5() == null) ? 0 : getPasswordMd5().hashCode());
        result = prime * result + ((getIntroduceSign() == null) ? 0 : getIntroduceSign().hashCode());
        result = prime * result + ((getIsDeleted() == null) ? 0 : getIsDeleted().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getLockedFlag() == null) ? 0 : getLockedFlag().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", nickName=").append(nickName);
        sb.append(", loginName=").append(loginName);
        sb.append(", passwordMd5=").append(passwordMd5);
        sb.append(", introduceSign=").append(introduceSign);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", lockedFlag=").append(lockedFlag);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
