package com.wz.cashloan.core.model;

import java.util.List;

/**
 * Created by Hbx on 2018/8/24.
 */
public class GameModel extends Game {
    private List<GameBet> gameBetList;

    public List<GameBet> getGameBetList() {
        return gameBetList;
    }

    public void setGameBetList(List<GameBet> gameBetList) {
        this.gameBetList = gameBetList;
    }
}
