package com.easy.service.impl;

import com.easy.mapper.VisitMappers;
import com.easy.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitServiceImpl implements VisitService {

    @Autowired
    private VisitMappers visitMapper;

    @Override
    public Integer getVisits() {
        return visitMapper.countAll();
    }

    @Override
    public void addVisit(String ip, String userAgent) {
        visitMapper.insertVisit(ip, userAgent);
    }
}
