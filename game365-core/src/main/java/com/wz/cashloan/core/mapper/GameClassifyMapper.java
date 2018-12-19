package com.wz.cashloan.core.mapper;

import com.wz.cashloan.core.common.mapper.RDBatisDao;
import com.wz.cashloan.core.model.GameClassify;

import java.util.List;

@RDBatisDao
public interface GameClassifyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GameClassify record);

    int insertSelective(GameClassify record);

    GameClassify selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GameClassify record);

    int updateByPrimaryKey(GameClassify record);

    GameClassify selectByName(String name);

    List<GameClassify> selectAll();
}