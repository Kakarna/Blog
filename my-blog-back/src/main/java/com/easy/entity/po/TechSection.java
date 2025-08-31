package com.easy.entity.po;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import com.easy.enums.DateTimePatternEnum;
import com.easy.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @Description: 笔记分区表
 * @date: 2025/08/01
 * @Author: Sena
 */
@Getter
public class TechSection implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 分区名称
     */
    private String name;

    /**
     * 分区描述
     */
    private String description;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonIgnore
    private Date createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdateAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "主键ID:" + (id == null ? " 空 " : id)
                + ", 分区名称:" + (name == null ? " 空 " : name)
                + ", 分区描述:" + (description == null ? " 空 " : description)
                + ", 创建时间:" + (createdAt == null ? " 空 " : DateUtils.format(createdAt, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))
                + ", 更新时间:" + (updatedAt == null ? " 空 " : DateUtils.format(updatedAt, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
    }
}

