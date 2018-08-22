package com.wz.cashloan.core.mapper;

import com.wz.cashloan.core.common.mapper.RDBatisDao;
import com.wz.cashloan.core.model.GameBet;
@RDBatisDao
public interface GameBetMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GameBet record);

    int insertSelective(GameBet record);

    GameBet selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GameBet record);

    int updateByPrimaryKey(GameBet record);
}