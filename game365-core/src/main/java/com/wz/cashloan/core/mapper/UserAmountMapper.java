package com.wz.cashloan.core.mapper;

import com.wz.cashloan.core.common.mapper.RDBatisDao;
import com.wz.cashloan.core.model.UserAmount;

import java.util.Map;

/**
 * <p>Author: HBX
 * <p>Time: 2018/8/24 22:09
 */
@RDBatisDao
public interface UserAmountMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserAmount record);

    int insertSelective(UserAmount record);

    UserAmount selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserAmount record);

    int updateBySelective(Map<String, Object> map);

    int updateByPrimaryKey(UserAmount record);

    UserAmount findByUserId(Long userId);

}
