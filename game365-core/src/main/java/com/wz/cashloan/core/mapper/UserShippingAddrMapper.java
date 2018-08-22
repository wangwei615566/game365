package com.wz.cashloan.core.mapper;

import com.wz.cashloan.core.common.mapper.RDBatisDao;
import com.wz.cashloan.core.model.UserShippingAddr;
@RDBatisDao
public interface UserShippingAddrMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserShippingAddr record);

    int insertSelective(UserShippingAddr record);

    UserShippingAddr selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserShippingAddr record);

    int updateByPrimaryKey(UserShippingAddr record);
}