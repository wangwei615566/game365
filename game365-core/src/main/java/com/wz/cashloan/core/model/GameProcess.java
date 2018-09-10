package com.wz.cashloan.core.model;

import java.util.Date;

public class GameProcess {
    private Long id;

    private Long gameId;

    private String processName;

    private Integer rightScore;

    private Integer leftScore;

    private String round;

    private Date createTime;

    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Integer getRightScore() {
        return rightScore;
    }

    public void setRightScore(Integer rightScore) {
        this.rightScore = rightScore;
    }

    public Integer getLeftScore() {
        return leftScore;
    }

    public void setLeftScore(Integer leftScore) {
        this.leftScore = leftScore;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round == null ? null : round.trim();
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

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }
}