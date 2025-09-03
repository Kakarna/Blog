package com.easy.service;

public interface VisitService {
    Integer getVisits();   // 获取访问量总数
    void addVisit(String ip, String userAgent);  // 记录一次访问
}
