package com.wz.cashloan.core.service;

import com.github.pagehelper.Page;
import com.wz.cashloan.core.model.GameOrder;

import java.util.Map;

public interface GameOrderService {

    Page<Map<String,Object>> pageList(Map<String, Object> params, int current, int pageSize);

    int update(GameOrder gameOrder);

}
