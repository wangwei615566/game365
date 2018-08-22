package com.wz.cashloan.core.model;

import java.util.Date;

public class Game {
    private Long id;

    private String externalGameCode;

    private Long gameClassifyId;

    private String name;

    private String leftTeam;

    private String leftTeamImg;

    private String rightTeam;

    private String rightTeamImg;

    private Date contestDate;

    private Date contestTime;

    private Byte state;

    private Date updateTime;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalGameCode() {
        return externalGameCode;
    }

    public void setExternalGameCode(String externalGameCode) {
        this.externalGameCode = externalGameCode == null ? null : externalGameCode.trim();
    }

    public Long getGameClassifyId() {
        return gameClassifyId;
    }

    public void setGameClassifyId(Long gameClassifyId) {
        this.gameClassifyId = gameClassifyId;
    }

    public String getLeftTeam() {
        return leftTeam;
    }

    public void setLeftTeam(String leftTeam) {
        this.leftTeam = leftTeam == null ? null : leftTeam.trim();
    }

    public String getLeftTeamImg() {
        return leftTeamImg;
    }

    public void setLeftTeamImg(String leftTeamImg) {
        this.leftTeamImg = leftTeamImg == null ? null : leftTeamImg.trim();
    }

    public String getRightTeam() {
        return rightTeam;
    }

    public void setRightTeam(String rightTeam) {
        this.rightTeam = rightTeam == null ? null : rightTeam.trim();
    }

    public String getRightTeamImg() {
        return rightTeamImg;
    }

    public void setRightTeamImg(String rightTeamImg) {
        this.rightTeamImg = rightTeamImg == null ? null : rightTeamImg.trim();
    }

    public Date getContestDate() {
        return contestDate;
    }

    public void setContestDate(Date contestDate) {
        this.contestDate = contestDate;
    }

    public Date getContestTime() {
        return contestTime;
    }

    public void setContestTime(Date contestTime) {
        this.contestTime = contestTime;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}