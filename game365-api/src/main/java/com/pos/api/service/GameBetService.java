package com.pos.api.service;

import com.wz.cashloan.core.common.context.Constant;
import com.wz.cashloan.core.mapper.ShoppingCartMapper;
import com.wz.cashloan.core.model.ShoppingCart;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Author: HBX
 * <p>Time: 2018/8/29 21:10
 */
@Service
public class GameBetService {
    @Resource
    private ShoppingCartMapper shoppingCartMapper;

    public Map add(Long userId, Long gameBetId) {
        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("gameBetId", gameBetId);

        ShoppingCart shoppingCart = shoppingCartMapper.selectByMap(result);
        result.clear();
        if (shoppingCart != null) {
            result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_PARAM_INSUFFICIENT);
            result.put(Constant.RESPONSE_CODE_MSG, "已添加!");

            return result;
        }

        shoppingCart = new ShoppingCart();
        shoppingCart.setCreateTime(new Date());
        shoppingCart.setGameBetId(gameBetId);
        shoppingCart.setUserId(userId);

        shoppingCartMapper.insert(shoppingCart);

        result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
        result.put(Constant.RESPONSE_CODE_MSG, "添加成功");

        return result;
    }

    public Map remove(Long userId, Long gameBetId) {
        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("gameBetId", gameBetId);

        ShoppingCart shoppingCart = shoppingCartMapper.selectByMap(result);
        result.clear();
        if (shoppingCart != null) {
            shoppingCartMapper.deleteByPrimaryKey(shoppingCart.getId());
        }

        result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
        result.put(Constant.RESPONSE_CODE_MSG, "移除成功");

        return result;
    }

    public Map listDetail(Long userId){
        return null;
    }

}
