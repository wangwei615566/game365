package com.pos.api.controller;

import com.github.pagehelper.Page;
import com.pos.api.service.ApiSmsService;
import com.pos.api.service.ApiUserService;
import com.wz.cashloan.core.common.context.Constant;
import com.wz.cashloan.core.common.util.RdPage;
import com.wz.cashloan.core.common.util.ServletUtils;
import com.wz.cashloan.core.common.web.controller.BaseController;
import com.wz.cashloan.core.model.GoodsOrder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Author: HBX
 * <p>Time: 2018/8/24 21:30
 */
@Scope("prototype")
@Controller
@RequestMapping("/api")
public class UserController extends BaseController {
    @Resource
    private ApiUserService apiUserService;
    @Resource
    private ApiSmsService apiSmsService;

    /**
     * 注册
     *
     * @param loginName
     * @param loginPwd
     * @param code
     */
    @RequestMapping("/user/register.htm")
    public void list(@RequestParam(value = "loginName") String loginName,
                     @RequestParam(value = "loginPwd") String loginPwd,
                     @RequestParam(value = "code") String code)

    {
        Map<String, Object> result = new HashMap<>();
        result = apiUserService.register(loginName, loginPwd, code);
        ServletUtils.writeToResponse(response, result);
    }

    /**
     * 登录
     *
     * @param loginName
     * @param loginPwd
     */
    @RequestMapping("/user/login.htm")
    public void login(@RequestParam(value = "loginName") String loginName,
                      @RequestParam(value = "loginPwd") String loginPwd)

    {
        Map<String, Object> result = new HashMap<>();
        result = apiUserService.login(loginName, loginPwd);
        ServletUtils.writeToResponse(response, result);
    }

    /**
     * 重置密码
     *
     * @param loginName
     * @param loginPwd
     */
    @RequestMapping("/user/resetPassword.htm")
    public void resetPassword(@RequestParam(value = "loginName") String loginName,
                              @RequestParam(value = "loginPwd") String loginPwd)

    {
        Map<String, Object> result = new HashMap<>();
        result = apiUserService.resetPassword(loginName, loginPwd);
        ServletUtils.writeToResponse(response, result);
    }

    /**
     * 获取短息验证码
     *
     * @param loginName
     * @param type
     */
    @RequestMapping("/user/sendSms.htm")
    public void sendSms(@RequestParam(value = "loginName") String loginName,
                        @RequestParam(value = "type") String type)

    {
        Map<String, Object> result = new HashMap<>();
        result = apiSmsService.sendSms(loginName, type);
        ServletUtils.writeToResponse(response, result);
    }

    /**
     * 忘记密码
     *
     * @param loginName
     * @param code
     * @param type
     */
    @RequestMapping("/user/forgetPwd.htm")
    public void sendSms(@RequestParam(value = "loginName") String loginName,
                        @RequestParam(value = "code") String code,
                        @RequestParam(value = "type") String type)

    {
        Map<String, Object> result = new HashMap<>();
        result = apiSmsService.checkCode(loginName, type, code);
        ServletUtils.writeToResponse(response, result);
    }

    /**
     * 红包提现申请
     *
     * @param userId
     * @param code
     * @param amount
     * @param receiver
     * @param alipayAccount
     */
    @RequestMapping("/user/applyCash.htm")
    public void applyCash(@RequestParam(value = "userId") Long userId,
                          @RequestParam(value = "code") String code,
                          @RequestParam(value = "amount") Double amount,
                          @RequestParam(value = "receiver") String receiver,
                          @RequestParam(value = "alipayAccount") String alipayAccount)

    {
        Map<String, Object> result = new HashMap<>();
        result = apiUserService.applyCash(userId, code, receiver, alipayAccount, amount);
        ServletUtils.writeToResponse(response, result);
    }

    /**
     * 获取购买请求列表
     *
     * @param userId
     * @param current
     * @param pageSize
     */
    @RequestMapping("/user/goodsOrder.htm")
    public void goodsOrder(@RequestParam(value = "userId") Long userId,
                           @RequestParam(value = "current") int current,
                           @RequestParam(value = "pageSize") int pageSize) {
        Map<String, Object> result = new HashMap<>();
        Page<GoodsOrder> page = apiUserService.goodsOrder(userId, current, pageSize);
        result.put(Constant.RESPONSE_DATA, page.getResult());
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
        result.put(Constant.RESPONSE_CODE_MSG, "查询成功");
        ServletUtils.writeToResponse(response, result);
    }

    /**
     * 更新收货地址
     *
     * @param userId
     * @param province
     * @param city
     * @param area
     * @param name
     * @param detailAddr
     * @param mobile
     */
    @RequestMapping("/user/saveShippingAddr.htm")
    public void goodsOrder(@RequestParam(value = "userId") Long userId,
                           @RequestParam(value = "province") String province,
                           @RequestParam(value = "city") String city,
                           @RequestParam(value = "area") String area,
                           @RequestParam(value = "name") String name,
                           @RequestParam(value = "detailAddr") String detailAddr,
                           @RequestParam(value = "mobile") String mobile) {
        Map<String, Object> result = new HashMap<>();
        result = apiUserService.saveShippingAddr(userId, province, city, area, detailAddr, mobile, name);
        ServletUtils.writeToResponse(response, result);
    }

    /**
     * @param userId
     */
    @RequestMapping("/user/center.htm")
    public void goodsOrder(@RequestParam(value = "userId") Long userId
    ) {
        Map<String, Object> result = new HashMap<>();
        result = apiUserService.center(userId);
        ServletUtils.writeToResponse(response, result);
    }
}
