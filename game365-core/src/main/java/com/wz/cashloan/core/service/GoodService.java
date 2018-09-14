package com.wz.cashloan.core.service;

import com.github.pagehelper.Page;
import com.wz.cashloan.core.model.Goods;

import java.util.Map;

public interface GoodService {

    Page<Goods> pageList(Map<String, Object> params, int current, int pageSize);

    int updateByPrimaryKeySelective(Goods record);

    int insertGoods(Goods record);

    int deleteById(long id);

}
