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

    /**
     * 查询学习记录列表
     * 前端API: fetchPublicStudyRecords, fetchPrivateStudyRecords - 用于获取公共和私人学习记录
     */
    @Override
    public List<StudyRecord> findListByParam(StudyRecordQuery query) {
        return this.studyRecordMappers.selectList(query);
    }

    /**
     * 统计符合条件的学习记录数量
     * 前端API: fetchPublicStudyRecords, fetchPrivateStudyRecords - 用于分页查询
     */
    @Override
    public Integer findCountByParam(StudyRecordQuery query) {
        return this.studyRecordMappers.selectCount(query);
    }

    /**
     * 分页查询学习记录
     * 前端API: fetchPublicStudyRecords, fetchPrivateStudyRecords - 用于分页展示学习记录
     */
    @Override
    public PaginationResultVO<StudyRecord> findListByPage(StudyRecordQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<StudyRecord> list = this.findListByParam(query);
        return new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
    }

    /**
     * 新增学习记录
     * 前端API: addStudyRecord - 用于添加新的学习记录
     */
    @Override
    public Integer add(StudyRecord studyRecord) {
        return this.studyRecordMappers.insert(studyRecord);
    }

    /**
     * 根据ID获取学习记录
     * 用于编辑前获取记录详情
     */
    @Override
    public StudyRecord getStudyRecordById(Integer id) {
        return this.studyRecordMappers.selectById(id);
    }

    /**
     * 更新学习记录
     * 前端API: updateStudyRecord - 用于更新已有学习记录
     */
    @Override
    public Integer updateStudyRecordById(StudyRecord bean, Integer id) {
        return this.studyRecordMappers.updateById(bean, id);
    }

    /**
     * 删除学习记录
     * 前端API: deleteStudyRecord - 用于删除学习记录
     */
    @Override
    public Integer deleteStudyRecordById(Integer id) {
        return this.studyRecordMappers.deleteById(id);
    }

    // ==================== 预留接口 ====================
    // 以下接口当前未被前端直接使用，预留用于后续功能扩展

    /**
     * 批量新增学习记录
     */
    @Override
    public Integer addBatch(List<StudyRecord> studyRecordList) {
        if (studyRecordList == null || studyRecordList.isEmpty()) {
            return 0;
        }
        return this.studyRecordMappers.insertBatch(studyRecordList);
    }

    /**
     * 批量新增或更新学习记录
     */
    @Override
    public Integer addOrUpdateBatch(List<StudyRecord> studyRecordList) {
        if (studyRecordList == null || studyRecordList.isEmpty()) {
            return 0;
        }
        return this.studyRecordMappers.insertOrUpdateBatch(studyRecordList);
    }
}
