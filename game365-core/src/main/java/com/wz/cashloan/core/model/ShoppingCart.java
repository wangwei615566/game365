package com.wz.cashloan.core.model;

import java.util.Date;

public class ShoppingCart {
    private Long id;

    private Long userId;

    private Long gameBetId;

    private Date createTime;

    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGameBetId() {
        return gameBetId;
    }

    public void setGameBetId(Long gameBetId) {
        this.gameBetId = gameBetId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}