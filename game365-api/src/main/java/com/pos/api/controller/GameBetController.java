package com.pos.api.controller;

import com.pos.api.service.GameBetService;
import com.wz.cashloan.core.common.util.ServletUtils;
import com.wz.cashloan.core.common.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        ServletUtils.writeToResponse(response, gameBetService.listDetail(userId));
    }
}
