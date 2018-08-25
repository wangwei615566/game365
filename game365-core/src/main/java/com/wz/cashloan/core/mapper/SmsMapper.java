package com.wz.cashloan.core.mapper;


import com.wz.cashloan.core.common.mapper.BaseMapper;
import com.wz.cashloan.core.common.mapper.RDBatisDao;
import com.wz.cashloan.core.model.Sms;

import java.util.Map;

/**
 * 短信记录Dao
 */
@RDBatisDao
public interface SmsMapper extends BaseMapper<Sms, Long> {

    /**
     * 查询最新一条短信记录
     *
     * @param data
     * @return
     */
    Sms findTimeMsg(Map<String, Object> data);

    /**
     * 查询某号码某种类型当天已发送次数
     *
     * @param data
     * @return
     */
    int countDayTime(Map<String, Object> data);

    /**
     * 同一个ip或电话一小时内发送的短信量
     *
     * @param data
     * @return
     */
    int hourOrPhoneCount(Map<String, Object> data);

}
