package com.easy.service.impl;

import com.easy.mapper.VisitMappers;
import com.easy.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitServiceImpl implements VisitService {

    @Autowired
    private VisitMappers visitMapper;

    // ==================== 预留接口 ====================
    // 以下接口当前未被前端直接使用，预留用于后续功能扩展

    /**
     * 获取总访问量
     */
    @Override
    public Integer getVisits() {
        return visitMapper.countAll();
    }

    /**
     * 添加访问记录
     */
    @Override
    public void addVisit(String ip, String userAgent) {
        visitMapper.insertVisit(ip, userAgent);
    }
}
