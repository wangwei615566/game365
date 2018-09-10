package com.wz.cashloan.core.mapper;

import com.wz.cashloan.core.common.mapper.RDBatisDao;
import com.wz.cashloan.core.model.GoodsOrder;

import java.util.List;

@RDBatisDao
public interface GoodsOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsOrder record);

    int insertSelective(GoodsOrder record);

    GoodsOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsOrder record);

    int updateByPrimaryKey(GoodsOrder record);

    List<GoodsOrder> selectByUserId(Long userId);
}