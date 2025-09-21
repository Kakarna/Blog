package com.easy.entity.query;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Description: 技术笔记查询对象
 * @date: 2025/08/02
 * @Author: Sena
 */
@Setter
@Getter
public class TechNoteQuery extends BaseQuery {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 分区ID
     */
    private Integer sectionId;

    private String sectionIdFuzzy;

    /**
     * 笔记标题
     */
    private String title;

    private String titleFuzzy;

    /**
     * 笔记内容
     */
    private String content;

    private String contentFuzzy;

    /**
     * 创建时间
     */
    private Date createdTime;

    private String createdTimeStart;

    private String createdTimeEnd;

    /**
     * 更新时间
     */
    private Date updatedTime;

    private String updatedTimeStart;

    private String updatedTimeEnd;

    /**
     * 用户ID
     */
    private Integer userId;

    // --- getter / setter ---

}
