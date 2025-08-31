package com.easy.entity.vo;

import lombok.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 项目文件视图对象
 * @date: 2025/08/09
 * @Author: Sena
 */
@Data
public class ProjectFileVO {
    private Integer id;               // 文件ID
    private Integer projectId;        // 所属项目ID
    private String path;              // 文件路径（相对项目根目录）
    private Boolean isDir;            // 是否为文件夹
    private String content;           // 文件内容（如果是文件，则有内容；文件夹无内容）
    private Date createTime;         // 创建时间
    private Date updateTime;         // 更新时间

    private List<ProjectFileVO> children = new ArrayList<>(); // 子文件/文件夹
}
