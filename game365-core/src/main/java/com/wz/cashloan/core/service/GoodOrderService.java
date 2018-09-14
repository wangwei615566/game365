package com.wz.cashloan.core.service;

import com.github.pagehelper.Page;
import com.wz.cashloan.core.model.GoodsOrder;

import java.util.Map;

public interface GoodOrderService {

    Page<Map<String,Object>> pageList(Map<String, Object> params, int current, int pageSize);

    int updateByPrimaryKeySelective(GoodsOrder goodsOrder);

}
