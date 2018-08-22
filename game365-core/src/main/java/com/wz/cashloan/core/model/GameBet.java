package com.wz.cashloan.core.model;

import java.math.BigDecimal;
import java.util.Date;

public class GameBet {
    private Long id;

    private Long gameId;

    private String name;

    private String team;

    private BigDecimal odds;

    private Date guessOverTime;

    private Date updateTime;

    private Date createTime;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team == null ? null : team.trim();
    }

    public BigDecimal getOdds() {
        return odds;
    }

    public void setOdds(BigDecimal odds) {
        this.odds = odds;
    }

    public Date getGuessOverTime() {
        return guessOverTime;
    }

    public void setGuessOverTime(Date guessOverTime) {
        this.guessOverTime = guessOverTime;
    }

    public Date getUdpateTime() {
        return updateTime;
    }

    public void setUdpateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}