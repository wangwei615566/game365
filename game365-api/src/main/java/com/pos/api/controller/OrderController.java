package com.pos.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.pos.api.service.UserService;
import com.wz.cashloan.core.common.context.Constant;
import com.wz.cashloan.core.common.util.RdPage;
import com.wz.cashloan.core.common.util.ServletUtils;
import com.wz.cashloan.core.common.web.controller.BaseController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hbx on 2018/9/11.
 */
@Scope("prototype")
@Controller
@RequestMapping("/api")
public class OrderController extends BaseController {
    @Resource
    private UserService userService;

    /**
     * 我的订单 - 进行中
     *
     * @param userId
     */
    @RequestMapping("/order/going.htm")
    public void going(@RequestParam(value = "userId") Long userId,
                      @RequestParam(value = "current") int current,
                      @RequestParam(value = "pageSize") int pageSize)

    {
        Map<String, Object> result = new HashMap<>();
        JSONObject page = userService.myGoingOrder(userId, current, pageSize);
        result.put(Constant.RESPONSE_DATA, page.getJSONArray("data"));
        result.put(Constant.RESPONSE_DATA_PAGE, page.getJSONObject("page"));
        result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
        result.put(Constant.RESPONSE_CODE_MSG, "查询成功");
        ServletUtils.writeToResponse(response, result);
    }

    /**
     * 我的订单 - 已结算
     *
     * @param userId
     */
    @RequestMapping("/order/finish.htm")
    public void finish(@RequestParam(value = "userId") Long userId,
                       @RequestParam(value = "current") int current,
                       @RequestParam(value = "pageSize") int pageSize)

    {
        Map<String, Object> result = new HashMap<>();
//        Page<JSONObject> page = userService.myGoingOrder(userId, current, pageSize);
//        result.put(Constant.RESPONSE_DATA, page.getResult());
//        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
        result.put(Constant.RESPONSE_CODE_MSG, "查询成功");
        ServletUtils.writeToResponse(response, result);
    }
}
