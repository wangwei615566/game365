package com.pos.api.controller;

import com.github.pagehelper.Page;
import com.pos.api.service.GoodsService;
import com.wz.cashloan.core.common.context.Constant;
import com.wz.cashloan.core.common.util.JsonUtil;
import com.wz.cashloan.core.common.util.RdPage;
import com.wz.cashloan.core.common.util.ServletUtils;
import com.wz.cashloan.core.common.web.controller.BaseController;
import com.wz.cashloan.core.model.Goods;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hbx on 2018/8/24.
 */
@Scope("prototype")
@Controller
@RequestMapping("/api")
public class GoodsController extends BaseController {
    @Resource
    private GoodsService goodsService;

    @RequestMapping("/goods/list.htm")
    public void list(@RequestParam(value = "searchParams", required = false) String searchParams,
                     @RequestParam(value = "current") int current,
                     @RequestParam(value = "pageSize") int pageSize)

    {
        Map<String, Object> params = JsonUtil.parse(searchParams, Map.class);
        Page<Goods> page = goodsService.listGoods(params, current, pageSize);
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page.getResult());
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
        result.put(Constant.RESPONSE_CODE_MSG, "查询成功");
        ServletUtils.writeToResponse(response, result);
    }


    @RequestMapping("/goods/saveOrder.htm")
    public void list(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "num") int num,
            @RequestParam(value = "amount") double amount,
            @RequestParam(value = "goodsId") Long goodsId)

    {
//        Map<String, Object> params = JsonUtil.parse(searchParams, Map.class);
//        Page<Goods> page = goodsService.listGoods(params, current, pageSize);
        Map<String, Object> result = new HashMap<>();
        int a = goodsService.saveGoodsOrder(userId, goodsId, num, amount);
        if (a > 0) {
            result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, Constant.OPERATION_SUCCESS);

        } else {
            result.put(Constant.RESPONSE_CODE_MSG, Constant.OPERATION_FAIL);
            if (a == -1) {
                result.put(Constant.RESPONSE_CODE_MSG, "商品不存在");
            }
            result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
        }
        ServletUtils.writeToResponse(response, result);
    }
}
