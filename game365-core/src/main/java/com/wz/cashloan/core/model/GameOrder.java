package com.wz.cashloan.core.model;

import java.math.BigDecimal;
import java.util.Date;

public class GameOrder {
    private Long id;

    private String orderNo;

    private Long gameBetId;

    private Long userId;

    private BigDecimal score;

    private Byte type;

    private String state;

    private Date createTime;

    private Date updateTime;

    private BigDecimal clearingScore;

    private Integer result;

    private String overState;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Long getGameBetId() {
        return gameBetId;
    }

    public void setGameBetId(Long gameBetId) {
        this.gameBetId = gameBetId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public BigDecimal getClearingScore() {
        return clearingScore;
    }

    public void setClearingScore(BigDecimal clearingScore) {
        this.clearingScore = clearingScore;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getOverState() {
        return overState;
    }

    public void setOverState(String overState) {
        this.overState = overState;
    }
}