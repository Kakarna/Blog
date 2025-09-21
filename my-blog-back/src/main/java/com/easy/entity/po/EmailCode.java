package com.easy.entity.po;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class EmailCode {
    private Long id;
    private String email;
    private String code;
    private Date expireTime;
    private Date createTime;
    // 新增字段
    private LocalDate sendDate;  // 发信日期
    private Integer sendCount;   // 当日发送次数
}
