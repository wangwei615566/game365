package com.pos.api.service;

import com.pos.api.model.SmsModel;
import com.wz.cashloan.core.common.context.Constant;
import com.wz.cashloan.core.mapper.UserMapper;
import com.wz.cashloan.core.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Author: HBX
 * <p>Time: 2018/8/23 23:41
 */
@Service
public class SmsService {
    @Resource
    private UserMapper userMapper;

    /**
     * 发送验证码
     *
     * @param loginName
     * @param type
     * @return
     */
    public Map sendSms(String loginName, String type) {
        Map result = new HashMap();

        if (type.equals(SmsModel.SMS_TYPE_REGISTER)) {
            User user = userMapper.selectByLoginName(loginName);
            if (user != null) {
                result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_PARAM_INSUFFICIENT);
                result.put(Constant.RESPONSE_CODE_MSG, "用户已存在!");
                return result;
            }
        }
        doSend(loginName, type);
        result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
        result.put(Constant.RESPONSE_CODE_MSG, "发送成功");
        return result;
    }

    /**
     * 验证码核验
     *
     * @param login
     * @param type
     * @param code
     * @return
     */
    public Map checKCode(String login, String type, String code) {
        Map result = new HashMap();
        result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
        result.put(Constant.RESPONSE_CODE_MSG, Constant.OPERATION_SUCCESS);
        return result;
    }

    private void doSend(String phone, String type) {

    }
}
