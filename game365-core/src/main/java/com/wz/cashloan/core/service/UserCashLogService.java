package com.wz.cashloan.core.service;

import com.github.pagehelper.Page;
import com.wz.cashloan.core.model.UserCashLog;

import java.util.List;
import java.util.Map;

public interface UserCashLogService {

    Page<Map<String,Object>> pageList(Map<String, Object> params, int current, int pageSize);

    int updateOrder(Map<String, Object> params);

}
