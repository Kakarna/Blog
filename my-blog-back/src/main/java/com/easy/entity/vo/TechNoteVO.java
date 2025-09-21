// TechNoteAddVO.java
package com.easy.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TechNoteVO {
    private String sectionName;
    private Integer sectionId;
    private Integer userId;
    private Integer pageNo;
    private Integer pageSize;
    private String title;        // 笔记标题
    private String content;      // 笔记内容
    private LocalDateTime createdTime; // 创建时间
    private LocalDateTime updatedTime;

}
