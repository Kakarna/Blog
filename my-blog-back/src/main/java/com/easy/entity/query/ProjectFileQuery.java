package com.easy.entity.query;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Description: 项目文件查询对象
 * @date: 2025/08/09
 * @Author: Sena
 */
public class ProjectFileQuery extends BaseQuery {

    @Setter
    @Getter
    private Integer id;
    @Setter
    @Getter
    private Integer projectId;

    @Setter
    @Getter
    private String path;
    @Setter
    @Getter
    private String pathFuzzy;

    @Setter
    @Getter
    private Boolean isDir;

    @Setter
    @Getter
    private String content;
    @Setter
    @Getter
    private String contentFuzzy;

    @Setter
    @Getter
    private Date createTime;
    @Setter
    @Getter
    private String createTimeStart;
    @Getter
    @Setter
    private String createTimeEnd;

    @Setter
    @Getter
    private Date updateTime;
    @Setter
    @Getter
    private String updateTimeStart;
    @Getter
    @Setter
    private String updateTimeEnd;

    // --- Getter & Setter ---

}
