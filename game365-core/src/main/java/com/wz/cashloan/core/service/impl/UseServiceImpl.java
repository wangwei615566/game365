package com.wz.cashloan.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wz.cashloan.core.mapper.UserAmountMapper;
import com.wz.cashloan.core.model.UserAmount;
import com.wz.cashloan.core.service.UserService;
import com.wz.cashloan.core.mapper.UserMapper;
import com.wz.cashloan.core.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("userService")
public class UseServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserAmountMapper userAmountMapper;

    @Override
    public Page<Map<String,Object>> pageList(Map<String, Object> params, int current, int pageSize) {
        PageHelper.startPage(current, pageSize);
        List<Map<String,Object>> orderList = userMapper.userList(params);
        return (Page<Map<String,Object>>) orderList;
    }

    @Override
    public int updateByPrimaryKeySelective(UserAmount record) {
        return userAmountMapper.updateByPrimaryKeySelective(record);
    }

}
