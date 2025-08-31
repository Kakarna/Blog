package com.easy.entity.query;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Description: 笔记分区查询对象
 * @date: 2025/08/01
 * @Author: Sena
 */
@Setter
@Getter
public class TechSectionQuery extends BaseQuery {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 分区名称
     */
    private String name;

    private String nameFuzzy;

    /**
     * 分区描述
     */
    private String description;

    private String descriptionFuzzy;

    /**
     * 创建时间
     */
    private Date createdAt;

    private String createdAtStart;

    private String createdAtEnd;

    /**
     * 更新时间
     */
    private Date updatedAt;

    private String updatedAtStart;

    private String updatedAtEnd;

}
