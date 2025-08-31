package com.easy.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Description: 项目文件实体类
 * @date: 2025/08/09
 * @Author: Sena
 */
@Setter
@Getter
public class ProjectFile {

    /**
     * 文件ID，主键，自增
     */
    private Integer id;

    /**
     * 所属项目ID，外键关联projects表
     */
    private Integer projectId;

    /**
     * 文件或目录的相对路径（相对于项目根目录）
     */
    private String path;

    /**
     * 是否是目录（true：目录，false：文件）
     */
    private Boolean isDir;

    /**
     * 文件内容（目录无内容，为null或空）
     */
    private String content;

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

    // --- Getter & Setter ---

}

