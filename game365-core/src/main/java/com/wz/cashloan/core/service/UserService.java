package com.wz.cashloan.core.service;

import com.github.pagehelper.Page;
import com.wz.cashloan.core.model.UserAmount;

import java.util.Map;

public interface UserService {

    Page<Map<String,Object>> pageList(Map<String, Object> params, int current, int pageSize);

    int updateByPrimaryKeySelective(UserAmount record);

}
