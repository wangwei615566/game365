package com.pos.api.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pos.api.model.SmsModel;
import com.wz.cashloan.core.common.context.Constant;
import com.wz.cashloan.core.common.util.MD5;
import com.wz.cashloan.core.common.util.StringUtil;
import com.wz.cashloan.core.mapper.GameOrderMapper;
import com.wz.cashloan.core.mapper.GoodsOrderMapper;
import com.wz.cashloan.core.mapper.UserAmountBillMapper;
import com.wz.cashloan.core.mapper.UserAmountMapper;
import com.wz.cashloan.core.mapper.UserCashLogMapper;
import com.wz.cashloan.core.mapper.UserMapper;
import com.wz.cashloan.core.mapper.UserShippingAddrMapper;
import com.wz.cashloan.core.model.GoodsOrder;
import com.wz.cashloan.core.model.User;
import com.wz.cashloan.core.model.UserAmount;
import com.wz.cashloan.core.model.UserAmountBill;
import com.wz.cashloan.core.model.UserCashLog;
import com.wz.cashloan.core.model.UserShippingAddr;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Author: HBX
 * <p>Time: 2018/8/23 23:12
 */
@Service
public class ApiUserService {
    private static final Logger logger = Logger.getLogger(ApiUserService.class);

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserAmountMapper userAmountMapper;
    @Resource
    private ApiSmsService apiSmsService;
    @Resource
    private UserCashLogMapper userCashLogMapper;
    @Resource
    private GoodsOrderMapper goodsOrderMapper;
    @Resource
    private UserShippingAddrMapper userShippingAddrMapper;
    @Resource
    private GameOrderMapper gameOrderMapper;
    @Resource
    private UserAmountBillMapper userAmountBillMapper;

    public Map register(String loginName, String loginPwd, String code) {
        Map result = new HashMap();
        try {
            User user = userMapper.selectByLoginName(loginName);
            if (user != null) {
                result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_PARAM_INSUFFICIENT);
                result.put(Constant.RESPONSE_CODE_MSG, "用户已存在!");
                return result;
            }

            if (StringUtil.isAnyBlank(loginName, loginPwd, code) || loginPwd.length() < 6 || loginName.length() != 11) {
                result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
                result.put(Constant.RESPONSE_CODE, "参数有误");
                return result;
            }
            result = apiSmsService.checkCode(loginName, SmsModel.SMS_TYPE_REGISTER, code);
            if (!"200".equals(result.get(Constant.RESPONSE_CODE).toString())) {
                return result;
            }
            loginPwd = MD5.md5(loginPwd);
            user = new User();
            user.setLoginPwd(loginPwd);
            user.setLoginName(loginName);

            userMapper.insert(user);

            UserAmount userAmount = new UserAmount();

            userAmount.setState((byte) 1);
            userAmount.setUserId(user.getId());
            userAmount.setTotal(BigDecimal.valueOf(0));

            userAmountMapper.insert(userAmount);
            result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "注册成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("注册失败", e);
            result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_PARAM_INSUFFICIENT);
            result.put(Constant.RESPONSE_CODE_MSG, "注册失败");
            return result;
        }
        return result;
    }

    public Map login(String loginName, String loginPwd) {
        Map result = new HashMap();
        try {
            User user = userMapper.selectByLoginName(loginName);
            if (user == null) {
                result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_PARAM_INSUFFICIENT);
                result.put(Constant.RESPONSE_CODE_MSG, "用户不存在");
                return result;
            }
            if (!user.getLoginPwd().equals(MD5.md5(loginPwd))) {
                result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_PARAM_INSUFFICIENT);
                result.put(Constant.RESPONSE_CODE_MSG, "密码错误");
                return result;
            }
            result.put("userId", user.getId());
            result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "登录成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("登录失败", e);
            result.put(Constant.RESPONSE_CODE, Constant.OTHER_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "登录失败");
            return result;
        }

        return result;
    }

    public Map resetPassword(String loginName, String newPwd) {
        Map result = new HashMap();
        if (StringUtil.isAnyBlank(loginName, newPwd) || newPwd.length() < 6 || loginName.length() != 11) {
            result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE, "参数有误");
            return result;
        }
        try {
            User user = userMapper.selectByLoginName(loginName);
            if (user == null) {
                result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_PARAM_INSUFFICIENT);
                result.put(Constant.RESPONSE_CODE_MSG, "用户不存在!");
                return result;
            }
            user.setLoginPwd(MD5.md5(newPwd));
            user.setLoginpwdModify(new Date());

            userMapper.updateByPrimaryKey(user);

            result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "密码修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("修改密码失败", e);
            result.put(Constant.RESPONSE_CODE, Constant.OTHER_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "系统异常");
            return result;
        }
        return result;
    }

    public Map applyCash(Long userId, String code, String receiver, String alipayAccount, Double amount) {
        Map result = new HashMap();

        if (StringUtil.isAnyBlank(receiver, alipayAccount) || amount < 100 || amount > 50000) {
            result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "参数有误");
            return result;
        }
        try {
            User user = userMapper.selectByPrimaryKey(userId);
            if (user == null) {
                result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
                result.put(Constant.RESPONSE_CODE_MSG, "操作失败,用户不存在");
                return result;
            }
            UserAmount userAmount = userAmountMapper.findByUserId(userId);
            if (userAmount == null) {
                result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
                result.put(Constant.RESPONSE_CODE_MSG, "操作失败,用户账户不存在");
                return result;
            }
            //核验验证码
            result = apiSmsService.checkCode(user.getLoginName(), SmsModel.SMS_TYPE_OPERATOR, code);
            if (!"200".equals(result.get(Constant.RESPONSE_CODE))) {
                return result;
            }

            List<UserCashLog> userCashLogList = userCashLogMapper.selectByUserIdAndToday(userId);
            if (userCashLogList.size() >= 3) {
                result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
                result.put(Constant.RESPONSE_CODE_MSG, "超过当日申请次数");
                return result;
            }

            BigDecimal current = userAmount.getTotal();
            BigDecimal cash = BigDecimal.valueOf(amount);
            int a = current.compareTo(cash);
            if (a == -1) {
                result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
                result.put(Constant.RESPONSE_CODE_MSG, "余额不足");
                return result;
            }

            UserCashLog userCashLog = new UserCashLog();
            userCashLog.setAccountName(receiver);
            userCashLog.setAccountNo(alipayAccount);
            userCashLog.setAmount(cash);
            userCashLog.setState((byte) 2);
            userCashLog.setUserId(userId);
            userCashLogMapper.insert(userCashLog);


            userAmount.setTotal(current.subtract(cash));

            userAmountMapper.updateByPrimaryKeySelective(userAmount);


            result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "操作成功");


        } catch (Exception e) {
            e.printStackTrace();
            logger.error("申请提现失败", e);
            result.put(Constant.RESPONSE_CODE, Constant.OTHER_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "系统异常");
            return result;
        }
        return result;
    }

    public Page<GoodsOrder> goodsOrder(Long userId, int current, int pageSize) {
        PageHelper.startPage(current, pageSize);

        List<GoodsOrder> goodsOrderList = goodsOrderMapper.selectByUserId(userId);
        return (Page<GoodsOrder>) goodsOrderList;
    }

    public Map saveShippingAddr(Long userId, String province, String city, String area, String detailAddr, String mobile, String name) {
        Map result = new HashMap();
        try {
            User user = userMapper.selectByPrimaryKey(userId);
            if (user == null) {
                result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
                result.put(Constant.RESPONSE_CODE_MSG, "操作失败,用户不存在");
                return result;
            }
            UserShippingAddr userShippingAddr = userShippingAddrMapper.selectByUserId(userId);
            if (userShippingAddr != null) {
                userShippingAddr.setMobile(mobile);
                userShippingAddr.setName(name);
                userShippingAddr.setArea(area);
                userShippingAddr.setCity(city);
                userShippingAddr.setDetailAddr(detailAddr);
                userShippingAddr.setProvince(province);
//                userShippingAddr.setUserId(userId);
                userShippingAddrMapper.updateByPrimaryKeySelective(userShippingAddr);
            } else {
                userShippingAddr = new UserShippingAddr();
                userShippingAddr.setArea(area);
                userShippingAddr.setCity(city);
                userShippingAddr.setDetailAddr(detailAddr);
                userShippingAddr.setProvince(province);
                userShippingAddr.setUserId(userId);
                userShippingAddr.setMobile(mobile);
                userShippingAddr.setName(name);
                userShippingAddrMapper.insert(userShippingAddr);
            }


            result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "操作成功");


        } catch (Exception e) {
            e.printStackTrace();
            logger.error("保存失败", e);
            result.put(Constant.RESPONSE_CODE, Constant.OTHER_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "系统异常");
            return result;
        }
        return result;
    }


    public JSONObject myGoingOrder(Long userId, int current, int pageSize) {
        JSONObject res = new JSONObject();
        PageHelper.startPage(current, pageSize);
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("userId", userId);
        queryMap.put("overState", "10");
        List<String> list = gameOrderMapper.selectOrderByStateAndUserId(queryMap);

        Page<String> stringPage = (Page<String>) list;
        List<String> stringList = stringPage.getResult();
        JSONArray data = new JSONArray();
//        Page<JSONObject> jsonObjectPage = new Page<>(stringPage.getPageNum(),stringPage.getPageSize());
        for (int i = 0; i < stringList.size(); i++) {
            JSONObject object = new JSONObject();
            String orderNo = stringList.get(i);
            List<Map> mapList = gameOrderMapper.selectMapByOrderNo(orderNo);
            if (mapList.size() > 0) {
                Map objectMap = mapList.get(0);
                String createTime = String.valueOf(objectMap.get("create_time"));
                if ("1".equals(String.valueOf(objectMap.get("type")))) {
                    object.put("type", "单注");
                } else {
                    object.put("type", mapList.size() + "串1");
                }

                JSONArray jsonArray = new JSONArray();
                Double petScore = 0.0;
                Double maxScore = 0.0;
                Double odds = 0.0;
                for (int j = 0; j < mapList.size(); j++) {
                    Map map = mapList.get(j);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("team", map.get("team"));
                    jsonObject.put("betName", map.get("name"));
                    String state = "";
                    if (map.get("result") == null) {
                        state = "进行中";
                    } else {
                        if ("1".equals(map.get("result"))) {
                            state = "输";
                        } else {
                            state = "赢";
                        }
                    }
                    jsonObject.put("state", state);
                    jsonObject.put("odds", map.get("odds"));
                    jsonArray.add(jsonObject);

                    petScore += Double.parseDouble(String.valueOf(map.get("score")));
                    odds += Double.parseDouble(String.valueOf(map.get("odds")));
                }

                maxScore = petScore * odds;

                object.put("petScore", petScore);
                object.put("maxScore", maxScore);
                object.put("createTime", createTime);
                object.put("gamePets", jsonArray);

                data.add(object);
            }

        }
        JSONObject page = new JSONObject();
        page.put("pageNum", stringPage.getPageNum());
        page.put("pageSize", stringPage.getPageSize());
        page.put("total", stringPage.getTotal());
        page.put("pages", stringPage.getPages());
        res.put("data", data);
        res.put("page", page);
        return res;

    }

    public JSONObject myFinishOrder(Long userId, int current, int pageSize) {
        JSONObject res = new JSONObject();
        PageHelper.startPage(current, pageSize);
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("userId", userId);
        queryMap.put("overState", "20");
        List<String> list = gameOrderMapper.selectOrderByStateAndUserId(queryMap);

        Page<String> stringPage = (Page<String>) list;
        List<String> stringList = stringPage.getResult();
        JSONArray data = new JSONArray();
//        Page<JSONObject> jsonObjectPage = new Page<>(stringPage.getPageNum(),stringPage.getPageSize());
        for (int i = 0; i < stringList.size(); i++) {
            JSONObject object = new JSONObject();
            String orderNo = stringList.get(i);
            List<Map> mapList = gameOrderMapper.selectMapByOrderNo(orderNo);

            UserAmountBill userAmountBill = userAmountBillMapper.findByOrderNo(orderNo);
            if (mapList.size() > 0) {
                Map objectMap = mapList.get(0);
                String createTime = String.valueOf(objectMap.get("create_time"));
                if ("1".equals(String.valueOf(objectMap.get("type")))) {
                    object.put("type", "单注");
                } else {
                    object.put("type", mapList.size() + "串1");
                }

                JSONArray jsonArray = new JSONArray();
                Double petScore = 0.0;

                for (int j = 0; j < mapList.size(); j++) {
                    Map map = mapList.get(j);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("team", map.get("team"));
                    jsonObject.put("betName", map.get("name"));
                    String state = "";
                    if (map.get("result") == null) {
                        state = "进行中";
                    } else {
                        if (map.get("result") == 1) {
                            state = "输";
                        } else {
                            state = "赢";
                        }
                    }
                    jsonObject.put("state", state);
                    jsonObject.put("odds", map.get("odds"));
                    jsonArray.add(jsonObject);

                    petScore += Double.parseDouble(String.valueOf(map.get("score")));
                }

                object.put("petScore", petScore);
                object.put("realScore", userAmountBill.getTotal());
                object.put("createTime", createTime);
                object.put("gamePets", jsonArray);

                data.add(object);
            }

        }
        JSONObject page = new JSONObject();
        page.put("pageNum", stringPage.getPageNum());
        page.put("pageSize", stringPage.getPageSize());
        page.put("total", stringPage.getTotal());
        page.put("pages", stringPage.getPages());
        res.put("data", data);
        res.put("page", page);
        return res;

    }

    public Map center(Long userId){
        Map<String,Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA,userMapper.myCenter(userId));
        result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
        result.put(Constant.RESPONSE_CODE_MSG, "请求成功");
        return result;
    }
}
