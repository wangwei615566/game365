package com.wz.cashloan.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wz.cashloan.core.mapper.GameBetMapper;
import com.wz.cashloan.core.mapper.GameMapper;
import com.wz.cashloan.core.model.GameBet;
import com.wz.cashloan.core.service.GameService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("gameService")
public class GameServiceImpl implements GameService {

    @Resource
    GameMapper gameMapper;
    @Resource
    GameBetMapper gameBetMapper;

    @Override
   public Page<Map<String, Object>> pageList(Map<String, Object> params, int current, int pageSize){
        PageHelper.startPage(current, pageSize);
        List<Map<String, Object>> list = gameMapper.listSelect(params);
        return (Page<Map<String, Object>>) list;
    }
    @Override
    public int updateByPrimaryKeySelective(GameBet gameBet){
        return gameBetMapper.updateByPrimaryKeySelective(gameBet);
    }

}
