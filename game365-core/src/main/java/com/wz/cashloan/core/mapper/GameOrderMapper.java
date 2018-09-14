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

    int updateOverStateByOrderNo(String orderNo);

    List<Map> pendingOrder();

    List<Map> notOverOrderNo();

    Double calcScore(String orderNo);

    List<String> selectOrderByStateAndUserId(Map<String, Object> objectMap);

    List<Map> selectMapByOrderNo(String orderNo);

}