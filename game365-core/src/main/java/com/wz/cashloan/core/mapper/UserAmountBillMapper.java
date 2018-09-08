package com.wz.cashloan.core.mapper;

import com.wz.cashloan.core.common.mapper.RDBatisDao;
import com.wz.cashloan.core.model.UserAmountBill;
@RDBatisDao
public interface UserAmountBillMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserAmountBill record);

    int insertSelective(UserAmountBill record);

    UserAmountBill selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserAmountBill record);

    int updateByPrimaryKey(UserAmountBill record);
}