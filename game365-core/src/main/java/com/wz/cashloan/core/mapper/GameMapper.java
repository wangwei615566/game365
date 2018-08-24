package com.wz.cashloan.core.mapper;

import com.wz.cashloan.core.common.mapper.RDBatisDao;
import com.wz.cashloan.core.model.Game;
import com.wz.cashloan.core.model.GameModel;

import java.util.List;
import java.util.Map;

@RDBatisDao
public interface GameMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Game record);

    int insertSelective(Game record);

    Game selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Game record);

    int updateByPrimaryKey(Game record);

    Game selectByGameCode(String code);

    List<Game> listSelective(Map<String, Object> map);

    List<GameModel> listGameModel(Map<String, Object> map);
}