package com.easy.entity.vo;

import lombok.Data;

@Data
public class TechSectionVO {
    private Integer id;
    private String name;
    private String description;
    private String createdAtStr; // 前端友好格式
}

