package com.wz.cashloan.core.mapper;

import com.wz.cashloan.core.common.mapper.RDBatisDao;
import com.wz.cashloan.core.model.ShoppingCart;

import java.util.List;
import java.util.Map;

@RDBatisDao
public interface ShoppingCartMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShoppingCart record);

    int insertSelective(ShoppingCart record);

    ShoppingCart selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShoppingCart record);

    int updateByPrimaryKey(ShoppingCart record);

    ShoppingCart selectByMap(Map<String, Object> map);

    List<ShoppingCart> listByMap(Map<String, Object> map);

    int deleteByGameBetIds(Map<String, Object> map);
}