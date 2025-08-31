package com.easy.service.impl;

import com.easy.entity.po.StudyRecord;
import com.easy.entity.query.SimplePage;
import com.easy.entity.query.StudyRecordQuery;
import com.easy.entity.vo.PaginationResultVO;
import com.easy.mapper.StudyRecordMappers;
import com.easy.service.StudyRecordService;
import com.easy.entity.vo.ResponseVO;
import com.easy.enums.PageSize;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 学习记录 Service 实现
 * @date: 2025/08/04
 * @Author: Sena
 */
@Service("studyRecordService")
public class StudyRecordServiceImpl implements StudyRecordService {

    @Resource
    private StudyRecordMappers<StudyRecord, StudyRecordQuery> studyRecordMappers;

    @Override
    public List<StudyRecord> findListByParam(StudyRecordQuery query) {
        return this.studyRecordMappers.selectList(query);
    }

    @Override
    public Integer findCountByParam(StudyRecordQuery query) {
        return this.studyRecordMappers.selectCount(query);
    }

    @Override
    public PaginationResultVO<StudyRecord> findListByPage(StudyRecordQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<StudyRecord> list = this.findListByParam(query);
        return new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
    }

    @Override
    public Integer add(StudyRecord studyRecord) {
        return this.studyRecordMappers.insert(studyRecord);
    }

    @Override
    public Integer addBatch(List<StudyRecord> studyRecordList) {
        if (studyRecordList == null || studyRecordList.isEmpty()) {
            return 0;
        }
        return this.studyRecordMappers.insertBatch(studyRecordList);
    }

    @Override
    public Integer addOrUpdateBatch(List<StudyRecord> studyRecordList) {
        if (studyRecordList == null || studyRecordList.isEmpty()) {
            return 0;
        }
        return this.studyRecordMappers.insertOrUpdateBatch(studyRecordList);
    }

    @Override
    public StudyRecord getStudyRecordById(Integer id) {
        return this.studyRecordMappers.selectById(id);
    }

    @Override
    public Integer updateStudyRecordById(StudyRecord bean, Integer id) {
        return this.studyRecordMappers.updateById(bean, id);
    }

    @Override
    public Integer deleteStudyRecordById(Integer id) {
        return this.studyRecordMappers.deleteById(id);
    }

}
