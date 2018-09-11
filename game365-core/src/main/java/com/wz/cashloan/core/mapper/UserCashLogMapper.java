package com.wz.cashloan.core.mapper;

import com.wz.cashloan.core.common.mapper.RDBatisDao;
import com.wz.cashloan.core.model.UserCashLog;

import java.util.List;


import java.util.List;
import java.util.Map;

@RDBatisDao
public interface UserCashLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserCashLog record);

    int insertSelective(UserCashLog record);

    UserCashLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserCashLog record);

    int updateByPrimaryKey(UserCashLog record);

    List<UserCashLog> selectByUserIdAndToday(Long userId);

    List<Map<String,Object>> listSelective(Map<String, Object> params);

    int updateOrder(Map<String, Object> params);
}