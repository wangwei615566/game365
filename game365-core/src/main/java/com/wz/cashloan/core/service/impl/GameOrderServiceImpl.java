package com.wz.cashloan.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wz.cashloan.core.mapper.GameOrderMapper;
import com.wz.cashloan.core.model.GameOrder;
import com.wz.cashloan.core.service.GameOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("gameOrderService")
public class GameOrderServiceImpl implements GameOrderService {

    @Resource
    GameOrderMapper gameOrderMapper;

    @Override
   public Page<Map<String,Object>> pageList(Map<String, Object> params, int current, int pageSize){
        PageHelper.startPage(current, pageSize);
        List<Map<String,Object>> list = gameOrderMapper.listSelect(params);
        return (Page<Map<String,Object>>) list;
    }

    @Override
    public int update(GameOrder gameOrder){
        return gameOrderMapper.updateByPrimaryKeySelective(gameOrder);
    }

}
