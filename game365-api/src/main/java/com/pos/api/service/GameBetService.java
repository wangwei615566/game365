package com.pos.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wz.cashloan.core.common.context.Constant;
import com.wz.cashloan.core.mapper.GameBetMapper;
import com.wz.cashloan.core.mapper.GameOrderMapper;
import com.wz.cashloan.core.mapper.ShoppingCartMapper;
import com.wz.cashloan.core.mapper.UserAmountMapper;
import com.wz.cashloan.core.model.GameBet;
import com.wz.cashloan.core.model.GameOrder;
import com.wz.cashloan.core.model.ShoppingCart;
import com.wz.cashloan.core.model.UserAmount;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tool.util.BigDecimalUtil;
import tool.util.OrderNoUtil;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Author: HBX
 * <p>Time: 2018/8/29 21:10
 */
@Service
public class GameBetService {
    @Resource
    private ShoppingCartMapper shoppingCartMapper;
    @Resource
    private GameBetMapper gameBetMapper;
    @Resource
    private UserAmountMapper userAmountMapper;
    @Resource
    private GameOrderMapper gameOrderMapper;

    public Map add(Long userId, Long gameBetId) {
        Map<String, Object> result = new HashMap<>();

        GameBet gameBet = gameBetMapper.selectByPrimaryKey(gameBetId);
        if (gameBet == null) {
            result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_PARAM_INSUFFICIENT);
            result.put(Constant.RESPONSE_CODE_MSG, "无效gameBetId");

            return result;
        }
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

    public List<GameBet> listDetail(Long userId) {

        return gameBetMapper.findShoppingCartGameBet(userId);
    }


    public Map saveBetOrder(Long userId, String gameBets, Double totalScore, int type) {
        Map result = new HashMap();
        String orderNo = OrderNoUtil.getSerialNumber();
        UserAmount userAmount = userAmountMapper.findByUserId(userId);
        if (userAmount == null) {
            result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "用户不存在");
            return result;
        }
        BigDecimal bigDecimal = new BigDecimal(totalScore);
        if (bigDecimal.compareTo(userAmount.getTotal()) > 0) {
            result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "积分不足");
            return result;
        }
        if (StringUtils.isBlank(gameBets)) {
            result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, Constant.OPERATION_FAIL);
            return result;
        }

        try {
            List ids = new ArrayList();
            JSONArray jsonArray = JSON.parseArray(gameBets);

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Long gameBetId = object.getLong("gameBetId");
                ids.add(gameBetId);
            }
            result.clear();
            result.put("ids", ids);
            int a = gameBetMapper.groupCountGameId(result);
            result.clear();
            if (a > 0 && type == 2) {
                result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
                result.put(Constant.RESPONSE_CODE_MSG, "通关模式单个一个赛事只允许投注一个竞猜项");
                return result;
            }
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Long gameBetId = object.getLong("gameBetId");
                BigDecimal score = object.getBigDecimal("score");

                GameOrder gameOrder = new GameOrder();
                gameOrder.setGameBetId(gameBetId);
                gameOrder.setOrderNo(orderNo);
                gameOrder.setScore(score);
                gameOrder.setType((byte) type);
                gameOrder.setUserId(userId);
                gameOrder.setState("10");

                gameOrderMapper.insert(gameOrder);
            }
            userAmount.setTotal(userAmount.getTotal().subtract(bigDecimal));
            userAmountMapper.updateByPrimaryKey(userAmount);

        } catch (Exception e) {
            e.printStackTrace();
            result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "系统异常");
            return result;
        }
        result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
        result.put(Constant.RESPONSE_CODE_MSG, Constant.OPERATION_SUCCESS);
        return result;
    }
}
