package com.easy.entity.vo;

import com.easy.entity.po.ProjectFile;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @Description: 项目视图对象
 * @date: 2025/08/09
 * @Author: Sena
 */
@Data
public class ProjectVO {
    private Integer id;               // 项目ID
    private String name;           // 项目名称
    private String description;    // 项目简介
    private String type;           // 项目类型（个人项目/课程项目/开源项目）
    private String readme;         // 项目详细介绍（Markdown格式）
    private Date createdTime;  // 创建时间
    private Date updatedTime;  // 更新时间
    private List<ProjectFile> files;
    private Integer userId;
}
