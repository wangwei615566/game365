package com.pos.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pos.api.model.SmsModel;
import com.wz.cashloan.core.common.context.Constant;
import com.wz.cashloan.core.common.context.Global;
import com.wz.cashloan.core.common.util.HttpUtil;
import com.wz.cashloan.core.common.util.StringUtil;
import com.wz.cashloan.core.mapper.SmsMapper;
import com.wz.cashloan.core.mapper.SmsTplMapper;
import com.wz.cashloan.core.mapper.UserMapper;
import com.wz.cashloan.core.model.Sms;
import com.wz.cashloan.core.model.SmsTpl;
import com.wz.cashloan.core.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Author: HBX
 * <p>Time: 2018/8/23 23:41
 */
@Service
public class SmsService {
    private static final Logger logger = Logger.getLogger(SmsService.class);
    @Resource
    private UserMapper userMapper;
    @Resource
    private SmsMapper smsMapper;
    @Resource
    private SmsTplMapper smsTplMapper;

    /**
     * 发送验证码
     *
     * @param loginName
     * @param type
     * @return
     */
    public Map sendSms(String loginName, String type) {
        Map result = new HashMap();
        if (StringUtil.isBlank(loginName) || StringUtil.isBlank(type)) {
            result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_PARAM_INSUFFICIENT);
            result.put(Constant.RESPONSE_CODE_MSG, "参数不能为空!");
            return result;
        }
        if (!StringUtil.isPhone(loginName)) {
            result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_PARAM_INSUFFICIENT);
            result.put(Constant.RESPONSE_CODE_MSG, "手机号码格式有误!");
            return result;
        }

        String mostTimes = Global.getValue("sms_day_most_times");
        int mostTime = JSONObject.parseObject(mostTimes).getIntValue(type);

        Map<String, Object> data = new HashMap<>();
        data.put("phone", loginName);
        data.put("smsType", type);

        int times = smsMapper.countDayTime(data);

        if (times > mostTime) {
            result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_PARAM_INSUFFICIENT);
            result.put(Constant.RESPONSE_CODE_MSG, "获取短信验证码过于频繁，请明日再试!");
            return result;
        }

        if (type.equals(SmsModel.SMS_TYPE_REGISTER)) {
            User user = userMapper.selectByLoginName(loginName);
            if (user != null) {
                result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_PARAM_INSUFFICIENT);
                result.put(Constant.RESPONSE_CODE_MSG, "用户已存在!");
                return result;
            }
        }

        if (type.equals(SmsModel.SMS_TYPE_FINDREG)) {
            User user = userMapper.selectByLoginName(loginName);
            if (user == null) {
                result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_PARAM_INSUFFICIENT);
                result.put(Constant.RESPONSE_CODE_MSG, "用户不存在!");
                return result;
            }
        }

        result = doSend(loginName, type);
//        result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
//        result.put(Constant.RESPONSE_CODE_MSG, "发送成功");
        return result;
    }

    /**
     * 验证码核验
     *
     * @param loginName
     * @param type
     * @param code
     * @return
     */
    public Map checkCode(String loginName, String type, String code) {
        Map result = new HashMap();
        Map<String, Object> queryMap = new HashMap<>();
        result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
        result.put(Constant.RESPONSE_CODE_MSG, "验证失败");
        User user = userMapper.selectByLoginName(loginName);
        if (user == null) {
            result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "用户不存在!");
            return result;
        }
        if ("dev".equals(Global.getValue("app_environment")) && "0000".equals(code)) {
            result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "验证成功");
            return result;
        }

        if (StringUtil.isBlank(loginName) || StringUtil.isBlank(type) || StringUtil.isBlank(code) || !StringUtil.isPhone(loginName)) {
            result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "手机号码或验证码错误");
            return result;
        }


        queryMap.put("phone", loginName);
        queryMap.put("smsType", type);
        Sms sms = smsMapper.findTimeMsg(queryMap);
        queryMap.clear();
        if (sms != null) {

//            String mostTimes = Global.getValue("sms_day_most_times");
//            int mostTime = JSONObject.parseObject(mostTimes).getIntValue("verifyTime");
//
//            data = new HashMap<>();
//            data.put("verifyTime", sms.getVerifyTime() + 1);
//            data.put("id", sms.getId());
//            smsMapper.updateSelective(data);
//
//            if (StringUtil.equals("00", sms.getState()) || StringUtil.equals("20", sms.getState()) || sms.getVerifyTime()>mostTime) {
//                return 0;
//            }
            long timeLimit = Long.parseLong(Global.getValue("sms_time_limit"));

            Date d1 = sms.getSendTime();
            Date d2 = new Date();
            long diff = d2.getTime() - d1.getTime();
            if (diff > timeLimit * 60 * 1000) {
                result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
                result.put(Constant.RESPONSE_CODE_MSG, "验证码已过期");
                return result;
            }
            if (code.equals(sms.getCode())) {
                result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
                result.put(Constant.RESPONSE_CODE_MSG, Constant.OPERATION_SUCCESS);
                return result;
            }
        }
        return result;


    }

    private Map<String, Object> doSend(String phone, String type) {


        Map result = new HashMap();
        try {
            String sms_url = Global.getValue("juhe_sms_url");
            String sms_key = Global.getValue("juhe_sms_key");
            int vcode = (int) (Math.random() * 9000) + 1000;
            if ("dev".equals(Global.getValue("app_environment"))) {
                result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
                result.put(Constant.RESPONSE_CODE_MSG, "发送成功");
                return result;
            }


            Map<String, Object> search = new HashMap<>();
            search.put("type", type);
            search.put("state", "10");
            SmsTpl smsTpl = smsTplMapper.findSelective(search);


            Sms sms = new Sms();
            String orderNo = "";
            String state = "00";
            String content = "";
            String resp = "";
            if (smsTpl != null && StringUtils.isNotBlank(sms_key) && StringUtils.isNotBlank(sms_url)) {
                content = smsTpl.getTpl().replace("#code#", String.valueOf(vcode));

                String modelId = smsTpl.getNumber();
                String value = URLEncoder.encode("#code#=" + vcode);
                sms_url = sms_url + "?mobile=" + phone + "&tpl_id=" + modelId + "&tpl_value=" + value + "&key=" + sms_key;
                resp = HttpUtil.doGet(sms_url);
                logger.info("手机号：" + phone + "发送短信响应：" + resp);
                if (StringUtils.isNotBlank(resp) && resp.contains("error_code")) {
                    JSONObject jsonObject = JSON.parseObject(resp);

                    if ("0".equals(jsonObject.getString("error_code"))) {
                        JSONObject data = jsonObject.getJSONObject("result");
                        orderNo = data.getString("sid");
                        state = "10";
                    }
                }
                //http://v.juhe.cn/sms/send?mobile=18871173257&tpl_id=96789&tpl_value=%23code%23%3D654654&key=77a87eebad097e167fb10675fe3d7127


            }
            sms.setPhone(phone);
            sms.setSendTime(new Date());
            sms.setContent(content);
            sms.setRespTime(new Date());
            sms.setResp(resp);
            sms.setSmsType(type);
            sms.setCode(String.valueOf(vcode));
            sms.setOrderNo(orderNo);
            sms.setState(state); //发送失败是00  发送成功是10 和 20  其中未使用验证码是10  已经使用验证过是20
            sms.setVerifyTime(0);
            smsMapper.save(sms);

            if ("10".equals(state)) {
                result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
                result.put(Constant.RESPONSE_CODE_MSG, "发送成功");
            } else {
                result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
                result.put(Constant.RESPONSE_CODE_MSG, "发送失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发送失败", e);
            result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_PARAM_INSUFFICIENT);
            result.put(Constant.RESPONSE_CODE_MSG, "发送失败");
            return result;
        }
        return result;
    }
}
