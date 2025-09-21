package com.easy.entity.po;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import com.easy.enums.DateTimePatternEnum;
import com.easy.utils.DateUtils;

/**
 * @Description: 技术笔记表
 * @date: 2025/08/02
 * @Author: Sena
 */
@Setter
@Getter
public class TechNote implements Serializable {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 分区ID（关联tech_sections表）
     */
    private Integer sectionId;

    /**
     * 笔记标题
     */
    private String title;

    /**
     * 笔记内容，支持富文本或markdown
     */
    private String content;

    /**
     * 关联用户ID
     */
    private Integer userId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;


    @Override
    public String toString() {
        return "主键ID:" + (id == null ? " 空 " : id)
                + ",分区ID:" + (sectionId == null ? " 空 " : sectionId)
                + ",用户ID:" + (userId == null ? " 空 " : userId)
                + ",标题:" + (title == null ? " 空 " : title)
                + ",内容:" + (content == null ? " 空 " : content)
                + ",创建时间:" + (createdTime == null ? " 空 " : DateUtils.format(createdTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))
                + ",更新时间:" + (updatedTime == null ? " 空 " : DateUtils.format(updatedTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
    }
}
