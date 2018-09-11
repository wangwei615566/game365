package com.pos.api.controller;

import com.wz.cashloan.core.common.util.ServletUtils;
import com.wz.cashloan.core.common.web.controller.BaseController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hbx on 2018/9/11.
 */
@Scope("prototype")
@Controller
@RequestMapping("/api")
public class OrderController extends BaseController {
    /**
     * 我的订单 - 进行中
     *
     * @param userId
     */
    @RequestMapping("/order/going.htm")
    public void going(@RequestParam(value = "userId") Long userId)

    {
        Map<String, Object> result = new HashMap<>();
//        result = userService.register(loginName, loginPwd, code);
        ServletUtils.writeToResponse(response, result);
    }

    /**
     * 我的订单 - 已结算
     *
     * @param userId
     */
    @RequestMapping("/order/finish.htm")
    public void finish(@RequestParam(value = "userId") Long userId)

    {
        Map<String, Object> result = new HashMap<>();
//        result = userService.register(loginName, loginPwd, code);
        ServletUtils.writeToResponse(response, result);
    }
}
