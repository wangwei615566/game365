package com.wz.manage.controller;

import com.github.pagehelper.Page;
import com.wz.cashloan.core.common.context.Constant;
import com.wz.cashloan.core.common.util.JsonUtil;
import com.wz.cashloan.core.common.util.RdPage;
import com.wz.cashloan.core.common.util.ServletUtils;
import com.wz.cashloan.core.model.GoodsOrder;
import com.wz.cashloan.core.service.GoodOrderService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@Scope("prototype")
public class ManageFoodOrderController extends ManageBaseController {

    @Resource
    private GoodOrderService goodOrderService;

    /**
     * 商品订单列表
     * @param searchParams 查询参数 json字符串
     * @param current 当前页
     * @param pageSize 每页页数
     * @throws Exception 异常
     */
    @RequestMapping(value = "/manage/foodOrder/search/list.htm")
    public void searchList(@RequestParam(value="searchParams",required=false) String searchParams,
                           @RequestParam(value = "current") int current,
                           @RequestParam(value = "pageSize") int pageSize) throws Exception {
        Map<String, Object> params = JsonUtil.parse(searchParams, Map.class);
        Page<Map<String,Object>> page = goodOrderService.pageList(params, current, pageSize);
        Map<String,Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page.getResult());
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
        result.put(Constant.RESPONSE_CODE_MSG, "查询成功");
        ServletUtils.writeToResponse(response, result);
    }

    /**
     * 修改单个商品订单
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/manage/foodOrder/search/save.htm")
    public void findById(@RequestParam(value="id") Long id,
                         @RequestParam(value="num") Integer num,
                         @RequestParam(value="amount") BigDecimal amount,
                         @RequestParam(value="couponAmt") BigDecimal couponAmt,
                         @RequestParam(value="shippingAddr") String shippingAddr,
                         @RequestParam(value="remark") String remark,
                         @RequestParam(value="state") Byte state) throws Exception {
        Map<String,Object> result = new HashMap<>();
        GoodsOrder goodsOrder = new GoodsOrder(id,num,amount,couponAmt,state,shippingAddr,remark);
        int i = goodOrderService.updateByPrimaryKeySelective(goodsOrder);
        if(i == 1){
            result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "成功");
        } else {
            result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "失败");
        }
        ServletUtils.writeToResponse(response, result);
    }

}
