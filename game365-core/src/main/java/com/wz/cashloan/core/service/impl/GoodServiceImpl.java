package com.wz.cashloan.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wz.cashloan.core.mapper.GoodsMapper;
import com.wz.cashloan.core.model.Goods;
import com.wz.cashloan.core.service.GoodService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("goodService")
public class GoodServiceImpl implements GoodService{

    @Resource
    GoodsMapper goodsMapper;

    @Override
   public Page<Goods> pageList(Map<String, Object> params, int current, int pageSize){
        PageHelper.startPage(current, pageSize);
        List<Goods> list = goodsMapper.listSelective(params);
        return (Page<Goods>) list;
    }
    @Override
    public int updateByPrimaryKeySelective(Goods record){
        return goodsMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int insertGoods(Goods record){
        return goodsMapper.insert(record);
    }

    @Override
    public int deleteById(long id) {
        return goodsMapper.deleteByPrimaryKey(id);
    }
}
