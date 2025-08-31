package com.easy.entity.vo;

import lombok.Data;
import java.util.Date;

@Data
public class StudyRecordVO {
    private String title;
    private String content;
    private Date studyDate;
    private String username;
    private Integer pageNo;
    private Integer pageSize;
    private String sortField;  // 新增排序字段，例如"studyDate"
    private String sortOrder;  // 新增排序顺序，例如"asc"或"desc"
}
