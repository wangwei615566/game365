package com.wz.cashloan.core.service;

import com.wz.cashloan.crawl.bean.Match;

import java.util.List;

/**
 * <p>Author: HBX
 * <p>Time: 2018/8/22 22:00
 */
public interface CrawlService {
    /**
     * 更新比赛数据
     * @param matchList
     */
    void saveAndUpdateMatch();
}
