package com.pos.api.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wz.cashloan.core.mapper.GameBetMapper;
import com.wz.cashloan.core.mapper.GameMapper;
import com.wz.cashloan.core.model.GameBet;
import com.wz.cashloan.core.model.GameModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hbx on 2018/8/24.
 */
@Service
public class GameService {
    @Autowired
    private GameMapper gameMapper;
    @Autowired
    private GameBetMapper gameBetMapper;

    /**
     * 按条件分頁查询
     *
     * @param params
     * @param current
     * @param pageSize
     * @return
     */
    public Page<GameModel> listGame(Map<String, Object> params, int current, int pageSize) {
        PageHelper.startPage(current, pageSize);
//        List<Game> games = gameMapper.listSelective(params);
//        List<GameModel> gameModels= new ArrayList<>();
        List<GameModel> gameModels = gameMapper.listGameModel(params);
        for (int i = 0; i < gameModels.size(); i++) {
            GameModel gameModel = gameModels.get(i);
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("gameId", gameModel.getId());
            List<GameBet> gameBetList = gameBetMapper.findSelective(queryMap);

            gameModel.setGameBetList(gameBetList);


        }
        return (Page<GameModel>) gameModels;
    }

}
