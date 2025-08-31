package com.easy.entity.po;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.easy.enums.DateTimePatternEnum;
import com.easy.utils.DateUtils;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 学习记录表
 * @date: 2025/08/04
 * @Author: Sena
 */
@Getter
public class StudyRecord implements Serializable {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 所属用户ID
     */
    private Integer userId;

    /**
     * 学习日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date studyDate;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonIgnore
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setStudyDate(Date studyDate) {
        this.studyDate = studyDate;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "主键ID:" + (id == null ? " 空 " : id) +
                ",标题:" + (title == null ? " 空 " : title) +
                ",内容:" + (content == null ? " 空 " : content) +
                ",用户ID:" + (userId == null ? " 空 " : userId) +
                ",学习日期:" + (studyDate == null ? " 空 " : DateUtils.format(studyDate, DateTimePatternEnum.YYYY_MM_DD.getPattern())) +
                ",创建时间:" + (createTime == null ? " 空 " : DateUtils.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) +
                ",更新时间:" + (updateTime == null ? " 空 " : DateUtils.format(updateTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
    }
}
