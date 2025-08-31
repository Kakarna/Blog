package com.easy.entity.vo;

import lombok.Data;

@Data
public class TechNoteAddVO {
    private String sectionName;   // 分区ID（必须传）
    private String title;        // 笔记标题
    private String content;      // 笔记内容
}
