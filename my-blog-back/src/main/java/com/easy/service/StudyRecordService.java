package com.easy.service;

import com.easy.entity.po.StudyRecord;
import com.easy.entity.query.StudyRecordQuery;
import com.easy.entity.vo.PaginationResultVO;
import com.easy.entity.vo.ResponseVO;

import java.util.List;

/**
 * @Description: 学习记录 Service
 * @date: 2025/08/04
 * @Author: Sena
 */
public interface StudyRecordService {

    /**
     * 根据条件查询列表
     */
    List<StudyRecord> findListByParam(StudyRecordQuery query);

    /**
     * 根据条件查询数量
     */
    Integer findCountByParam(StudyRecordQuery query);

    /**
     * 分页查询
     */
    PaginationResultVO<StudyRecord> findListByPage(StudyRecordQuery query);

    /**
     * 新增
     */
    Integer add(StudyRecord studyRecord);

    /**
     * 批量新增
     */
    Integer addBatch(List<StudyRecord> studyRecordList);

    /**
     * 批量新增或修改
     */
    Integer addOrUpdateBatch(List<StudyRecord> studyRecordList);

    /**
     * 根据Id查询
     */
    StudyRecord getStudyRecordById(Integer id);

    /**
     * 根据Id更新
     */
    Integer updateStudyRecordById(StudyRecord bean, Integer id);

    /**
     * 根据Id删除
     */
    Integer deleteStudyRecordById(Integer id);

}
