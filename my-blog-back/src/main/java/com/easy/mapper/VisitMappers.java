package com.easy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface VisitMappers {
    /**
     * 统计总访问量
     * 调用方: VisitServiceImpl/DashboardServiceImpl
     */
    Integer countAll();

    /**
     * 记录访问信息
     * 调用方: VisitServiceImpl
     */
    void insertVisit(@Param("ip") String ip, @Param("userAgent") String userAgent);
}
