package com.wz.cashloan.core.model;

import java.math.BigDecimal;
import java.util.Date;

public class UserAmountBill {
    private Long id;

    private Long userId;

    private BigDecimal total;

    private String gameOrderNo;

    private Date createTime;

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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getGameOrderNo() {
        return gameOrderNo;
    }

    public void setGameOrderNo(String gameOrderNo) {
        this.gameOrderNo = gameOrderNo == null ? null : gameOrderNo.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}