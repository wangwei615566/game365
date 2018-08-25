package com.pos.api.service;

import com.pos.api.model.SmsModel;
import com.wz.cashloan.core.common.context.Constant;
import com.wz.cashloan.core.common.util.MD5;
import com.wz.cashloan.core.common.util.StringUtil;
import com.wz.cashloan.core.mapper.UserAmountMapper;
import com.wz.cashloan.core.mapper.UserMapper;
import com.wz.cashloan.core.model.User;
import com.wz.cashloan.core.model.UserAmount;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
}
