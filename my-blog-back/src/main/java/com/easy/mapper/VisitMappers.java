package com.easy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface VisitMappers {
    Integer countAll();
    void insertVisit(@Param("ip") String ip, @Param("userAgent") String userAgent);
}
