package com.wz.cashloan.core.mapper;

import com.wz.cashloan.core.common.mapper.RDBatisDao;
import com.wz.cashloan.core.model.CrawlLog;
@RDBatisDao
public interface CrawlLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CrawlLog record);

    int insertSelective(CrawlLog record);

    CrawlLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CrawlLog record);

    int updateByPrimaryKeyWithBLOBs(CrawlLog record);

    int updateByPrimaryKey(CrawlLog record);
}