package com.wz.cashloan.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wz.cashloan.core.mapper.GoodsOrderMapper;
import com.wz.cashloan.core.model.GoodsOrder;
import com.wz.cashloan.core.service.GoodOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("goodOrderService")
public class GoodOrderServiceImpl implements GoodOrderService{

    @Resource
    GoodsOrderMapper goodsOrderMapper;

    @Override
   public Page<Map<String,Object>> pageList(Map<String, Object> params, int current, int pageSize){
        PageHelper.startPage(current, pageSize);
        List<Map<String,Object>> list = goodsOrderMapper.listSelect(params);
        return (Page<Map<String,Object>>) list;
    }
    @Override
    public int updateByPrimaryKeySelective(GoodsOrder goodsOrder){
        return goodsOrderMapper.updateByPrimaryKeySelective(goodsOrder);
    }

}
