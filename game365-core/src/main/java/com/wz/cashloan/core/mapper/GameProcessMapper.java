package com.wz.cashloan.core.mapper;

import com.wz.cashloan.core.common.mapper.RDBatisDao;
import com.wz.cashloan.core.model.GameProcess;

import java.util.List;
import java.util.Map;
@RDBatisDao
public interface GameProcessMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GameProcess record);

    int insertSelective(GameProcess record);

    GameProcess selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GameProcess record);

    int updateByPrimaryKey(GameProcess record);

    GameProcess findByMap(Map<String, Object> map);

    List<GameProcess> ListByMap(Map<String, Object> map);
}