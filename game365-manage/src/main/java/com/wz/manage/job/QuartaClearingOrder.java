package com.wz.manage.job;

import com.wz.cashloan.core.service.CrawlService;
import com.wz.cashloan.core.service.OrderService;
import com.wz.manage.domain.QuartzInfo;
import com.wz.manage.domain.QuartzLog;
import com.wz.manage.service.QuartzInfoService;
import com.wz.manage.service.QuartzLogService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import tool.util.BeanUtil;
import tool.util.DateUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Author: HBX
 * <p>Time: 2018/9/9 2:10
 */
@Component
@Lazy(value = false)
public class QuartaClearingOrder implements Job {
    private static final Logger logger = Logger.getLogger(QuartaClearingOrder.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        OrderService orderService = (OrderService) BeanUtil.getBean("orderService");

        QuartzInfoService quartzInfoService = (QuartzInfoService) BeanUtil.getBean("quartzInfoService");
        QuartzLogService quartzLogService = (QuartzLogService) BeanUtil.getBean("quartzLogService");

        QuartzLog ql = new QuartzLog();
        QuartzInfo qi = quartzInfoService.findByCode("doClearingOrder");
        Map<String, Object> qiData = new HashMap<>();
        qiData.put("id", qi.getId());

        ql.setQuartzId(qi.getId());
        ql.setStartTime(DateUtil.getNow());
        try {
            logger.info("进入结算订单。。。。。");
            orderService.clearing();
            ql.setTime(DateUtil.getNow().getTime() - ql.getStartTime().getTime());
            ql.setResult("10");
            ql.setRemark("执行成功");
            qiData.put("succeed", qi.getSucceed() + 1);

            logger.info("定时结算订单任务结束...");
        } catch (Exception e) {
            ql.setTime(DateUtil.getNow().getTime() - ql.getStartTime().getTime());
            ql.setResult("20");
            ql.setRemark("执行失败");
            qiData.put("succeed", qi.getFail() + 1);
            logger.error(e.getMessage(), e);
        } finally {
            quartzLogService.save(ql);
            quartzInfoService.update(qiData);
        }

    }
}
