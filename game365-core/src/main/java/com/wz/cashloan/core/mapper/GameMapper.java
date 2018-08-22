package com.wz.cashloan.core.mapper;

import com.wz.cashloan.core.common.mapper.RDBatisDao;
import com.wz.cashloan.core.model.Game;
@RDBatisDao
public interface GameMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Game record);

    int insertSelective(Game record);

    Game selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Game record);

    int updateByPrimaryKey(Game record);
}