package com.pos.api.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pos.api.model.SmsModel;
import com.wz.cashloan.core.common.context.Constant;
import com.wz.cashloan.core.common.util.MD5;
import com.wz.cashloan.core.common.util.StringUtil;
import com.wz.cashloan.core.mapper.GoodsOrderMapper;
import com.wz.cashloan.core.mapper.UserAmountMapper;
import com.wz.cashloan.core.mapper.UserCashLogMapper;
import com.wz.cashloan.core.mapper.UserMapper;
import com.wz.cashloan.core.mapper.UserShippingAddrMapper;
import com.wz.cashloan.core.model.GoodsOrder;
import com.wz.cashloan.core.model.User;
import com.wz.cashloan.core.model.UserAmount;
import com.wz.cashloan.core.model.UserCashLog;
import com.wz.cashloan.core.model.UserShippingAddr;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import tool.util.BigDecimalUtil;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Author: HBX
 * <p>Time: 2018/8/23 23:12
 */
@Service
public class UserService {
    private static final Logger logger = Logger.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserAmountMapper userAmountMapper;
    @Resource
    private SmsService smsService;
    @Resource
    private UserCashLogMapper userCashLogMapper;
    @Resource
    private GoodsOrderMapper goodsOrderMapper;
    @Resource
    private UserShippingAddrMapper userShippingAddrMapper;

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
            result = smsService.checkCode(loginName, SmsModel.SMS_TYPE_REGISTER, code);
            if (Constant.FAIL_CODE_VALUE == result.get(Constant.RESPONSE_CODE)) {
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
            result.put(Constant.RESPONSE_CODE, "参数有误");
            return result;
        }
        try {
            User user = userMapper.selectByPrimaryKey(userId);
            if (user == null) {
                result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
                result.put(Constant.RESPONSE_CODE, "操作失败,用户不存在");
                return result;
            }
            UserAmount userAmount = userAmountMapper.findByUserId(userId);
            if (userAmount == null) {
                result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
                result.put(Constant.RESPONSE_CODE, "操作失败,用户账户不存在");
                return result;
            }
            //核验验证码
            result = smsService.checkCode(user.getLoginName(), SmsModel.SMS_TYPE_REGISTER, code);
            if (Constant.FAIL_CODE_VALUE == result.get(Constant.RESPONSE_CODE)) {
                return result;
            }

            List<UserCashLog> userCashLogList = userCashLogMapper.selectByUserIdAndToday(userId);
            if (userCashLogList.size() >= 3) {
                result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
                result.put(Constant.RESPONSE_CODE, "超过当日申请次数");
                return result;
            }

            BigDecimal current = userAmount.getTotal();
            BigDecimal cash = BigDecimal.valueOf(amount);
            int a = current.compareTo(cash);
            if (a == -1) {
                result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
                result.put(Constant.RESPONSE_CODE, "余额不足");
                return result;
            }

            UserCashLog userCashLog = new UserCashLog();
            userCashLog.setAccountName(receiver);
            userCashLog.setAccountNo(alipayAccount);
            userCashLog.setAmount(cash);
            userCashLog.setState((byte) 2);

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
                result.put(Constant.RESPONSE_CODE, "操作失败,用户不存在");
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

}
