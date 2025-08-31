package com.easy.entity.query;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Description: 学习记录查询对象
 * @date: 2025/08/04
 * @Author: Sena
 */
@Setter
@Getter
public class StudyRecordQuery extends BaseQuery {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 标题
     */
    private String title;

    private String titleFuzzy;

    /**
     * 内容
     */
    private String content;

    private String contentFuzzy;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 学习日期
     */
    private Date studyDate;

    private String studyDateStart;

    private String studyDateEnd;

    /**
     * 创建时间
     */
    private Date createTime;

    private String createTimeStart;

    private String createTimeEnd;

    /**
     * 更新时间
     */
    private Date updateTime;

    private String updateTimeStart;

    private String updateTimeEnd;

    /**
     * 排序字段，例如 studyDate, createTime 等
     */
    private String sortField;

    /**
     * 排序顺序，asc 或 desc
     */
    private String sortOrder;


    // -------------- setter / getter --------------

}
