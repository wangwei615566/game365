package com.wz.cashloan.core.mapper;

import com.wz.cashloan.core.common.mapper.RDBatisDao;
import com.wz.cashloan.core.model.GameOrder;

import java.util.List;
import java.util.Map;

@RDBatisDao
public interface GameOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GameOrder record);

    int insertSelective(GameOrder record);

    GameOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GameOrder record);

    int updateByPrimaryKey(GameOrder record);

    List<Map> pendingOrder();

    List<Map> notOverOrderNo();

    Double calcScore(String orderNo);
}