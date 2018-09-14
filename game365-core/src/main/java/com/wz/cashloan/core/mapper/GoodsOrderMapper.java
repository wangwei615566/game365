package com.wz.cashloan.core.mapper;

import com.wz.cashloan.core.common.mapper.RDBatisDao;
import com.wz.cashloan.core.model.GoodsOrder;

import java.util.List;
import java.util.Map;

@RDBatisDao
public interface GoodsOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsOrder record);

    int insertSelective(GoodsOrder record);

    GoodsOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsOrder record);

    int updateByPrimaryKey(GoodsOrder record);

    List<GoodsOrder> selectByUserId(Long userId);

    List<Map<String,Object>> listSelect(Map<String,Object> params);

    int updateOrder(Map<String,Object> params);
}