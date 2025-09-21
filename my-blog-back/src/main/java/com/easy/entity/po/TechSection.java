package com.easy.entity.po;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
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
    @Setter
    private Integer id;

    @Setter
    private String name;

    @Setter
    private String description;

    /** 创建人 ID，对应 user.id */
    @Setter
    private Integer userId;

    @Setter
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonIgnore
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    public void setUpdateAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "主键ID:" + (id == null ? " 空 " : id)
                + ", 分区名称:" + (name == null ? " 空 " : name)
                + ", 分区描述:" + (description == null ? " 空 " : description)
                + ", 创建人ID:" + (userId == null ? " 空 " : userId)
                + ", 创建时间:" + (createdAt == null ? " 空 " : DateUtils.format(createdAt, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))
                + ", 更新时间:" + (updatedAt == null ? " 空 " : DateUtils.format(updatedAt, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
    }
}


