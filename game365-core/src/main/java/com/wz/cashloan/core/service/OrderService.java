package com.wz.cashloan.core.service;

import com.wz.cashloan.core.mapper.GameOrderMapper;
import com.wz.cashloan.core.mapper.GameProcessMapper;
import com.wz.cashloan.core.mapper.UserAmountBillMapper;
import com.wz.cashloan.core.model.GameOrder;
import com.wz.cashloan.core.model.GameProcess;
import com.wz.cashloan.core.model.UserAmountBill;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Author: HBX
 * <p>Time: 2018/9/8 20:47
 */
@Service
public class OrderService {
    @Resource
    private GameOrderMapper gameOrderMapper;
    @Resource
    private GameProcessMapper gameProcessMapper;
    @Resource
    private UserAmountBillMapper userAmountBillMapper;

    public void clearing() {
        List<Map> mapList = gameOrderMapper.pendingOrder();
        for (int i = 0; i < mapList.size(); i++) {
            Map<String, Object> queryMap = new HashMap<>();

            Map map = mapList.get(i);
            Long orderId = Long.valueOf(String.valueOf(map.get("orderId")));

            Long gameId = Long.valueOf(String.valueOf(map.get("gameId")));
            GameOrder gameOrder = gameOrderMapper.selectByPrimaryKey(orderId);
            String name = String.valueOf(map.get("name"));
            String team = String.valueOf(map.get("team"));
            String gameState = String.valueOf(map.get("gameState"));
            double odds = Double.parseDouble(String.valueOf(map.get("adds")));
            double score = Double.parseDouble(String.valueOf(map.get("score")));
            String leftTeam = String.valueOf(map.get("leftTeam"));
            String rightTeam = String.valueOf(map.get("rightTeam"));
            int leftScore = Integer.parseInt(String.valueOf(map.get("leftScore")));
            int rightScore = Integer.parseInt(String.valueOf(map.get("rightScore")));
            //比赛胜利方
            int res = 1;
            double clearingScore = 0.0;
            String winTeam = "";
            if (name.contains("获得比赛胜利") && "2".equals(gameState)) {

                if (leftScore > rightScore) {
                    winTeam = leftTeam;
                } else if (leftScore < rightScore) {
                    winTeam = rightTeam;
                } else if (leftScore == rightScore) {
                    winTeam = "平局";
                }
                //押中

                if (winTeam.equals(team)) {
                    clearingScore = odds * score;
                    res = 0;
                }
                gameOrder.setState("20");
                gameOrder.setClearingScore(BigDecimal.valueOf(clearingScore));
                gameOrder.setResult(res);

                gameOrderMapper.updateByPrimaryKeySelective(gameOrder);

            }

            if (name.contains("第一局比赛获胜队伍")) {
                queryMap.put("gameId", gameId);
                queryMap.put("round", "1");
                GameProcess gameProcess = gameProcessMapper.findByMap(queryMap);
                queryMap.clear();
                if (gameProcess != null) {
                    updateOrder(gameOrder, gameProcess, leftTeam, rightTeam, team, odds, score);
                }
            }


            if (name.contains("第二局比赛获胜队伍")) {
                queryMap.put("gameId", gameId);
                queryMap.put("round", "2");
                GameProcess gameProcess = gameProcessMapper.findByMap(queryMap);
                if (gameProcess != null) {
                    updateOrder(gameOrder, gameProcess, leftTeam, rightTeam, team, odds, score);
                }
            }

            if (name.contains("第三局比赛获胜队伍")) {
                queryMap.put("gameId", gameId);
                queryMap.put("round", "3");
                GameProcess gameProcess = gameProcessMapper.findByMap(queryMap);
                if (gameProcess != null) {
                    updateOrder(gameOrder, gameProcess, leftTeam, rightTeam, team, odds, score);
                }
            }
        }


        List<Map> mapList1 = gameOrderMapper.notOverOrderNo();
        for (int i = 0; i < mapList1.size(); i++) {
            Map map = mapList1.get(i);
            Long userId = Long.valueOf(String.valueOf(map.get("userId")));
            String orderNo = String.valueOf(map.get("orderNo"));
            UserAmountBill userAmountBill = new UserAmountBill();

            Double score = gameOrderMapper.calcScore(orderNo);

            userAmountBill.setGameOrderNo(orderNo);
            userAmountBill.setTotal(BigDecimal.valueOf(score));
            userAmountBill.setUserId(userId);

            userAmountBillMapper.insert(userAmountBill);

        }
    }

    private void updateOrder(GameOrder gameOrder, GameProcess gameProcess, String leftTeam, String rightTeam, String team, Double odds, Double score) {
        int res = 1;
        double clearingScore = 0.0;
        String winTeam = "";
        int leftScore = gameProcess.getLeftScore();
        int rightScore = gameProcess.getRightScore();
        if (leftScore > rightScore) {
            winTeam = leftTeam;
        } else if (leftScore < rightScore) {
            winTeam = rightTeam;
        }
//                    else if (leftScore == rightScore) {
//                        winTeam = "平局";
//                    }
        //押中

        if (winTeam.equals(team)) {
            clearingScore = odds * score;
            res = 0;
        }
        gameOrder.setState("20");
        gameOrder.setClearingScore(BigDecimal.valueOf(clearingScore));
        gameOrder.setResult(res);

        gameOrderMapper.updateByPrimaryKeySelective(gameOrder);
    }
}
