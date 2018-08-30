package com.pos.api.controller;

import com.pos.api.service.GameBetService;
import com.wz.cashloan.core.common.context.Constant;
import com.wz.cashloan.core.common.util.RdPage;
import com.wz.cashloan.core.common.util.ServletUtils;
import com.wz.cashloan.core.common.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Author: HBX
 * <p>Time: 2018/8/29 21:04
 */
@Scope("prototype")
@Controller
@RequestMapping("/api")
public class GameBetController extends BaseController {

    @Autowired
    private GameBetService gameBetService;

    @RequestMapping("/gameBet/add.htm")
    public void add(@RequestParam(value = "userId") Long userId,
                    @RequestParam(value = "gameBetId") Long gameBetId)

    {

        ServletUtils.writeToResponse(response, gameBetService.add(userId, gameBetId));
    }

    @RequestMapping("/gameBet/remove.htm")
    public void remove(@RequestParam(value = "userId") Long userId,
                       @RequestParam(value = "gameBetId") Long gameBetId)

    {
        ServletUtils.writeToResponse(response, gameBetService.remove(userId, gameBetId));
    }

    @RequestMapping("/gameBet/listDetail.htm")
    public void listDetail(@RequestParam(value = "userId") Long userId)

    {
        Map result = new HashMap();
        result.put(Constant.RESPONSE_DATA, gameBetService.listDetail(userId));
        result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
        result.put(Constant.RESPONSE_CODE_MSG, "查询成功");
        ServletUtils.writeToResponse(response, result);
    }


    @RequestMapping("/gameBet/saveBetOrder.htm")
    public void saveBetOrder(@RequestParam(value = "userId") Long userId,
                             @RequestParam(value = "totalScore") Double totalScore,
                             @RequestParam(value = "type") int type,
                             @RequestParam(value = "gameBets") String gameBets
    )

    {

        ServletUtils.writeToResponse(response, gameBetService.saveBetOrder(userId,gameBets,totalScore,type));
    }

}
