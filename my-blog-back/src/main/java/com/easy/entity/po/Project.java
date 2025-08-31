package com.easy.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @Description: 项目实体类
 * @date: 2025/08/09
 * @Author: Sena
 */
@Setter
@Getter
public class Project {

	/**
	 * 项目ID，主键，自增
	 */
	private Integer id;

	/**
	 * 项目名称
	 */
	private String name;

	/**
	 * 项目简介
	 */
	private String description;

	/**
	 * 项目类型（个人项目/课程项目/开源项目等）
	 */
	private String type;

	/**
	 * 项目详细介绍（Markdown格式）
	 */
	private String readme;

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


	private List<ProjectFile> files;
	// --- Getter & Setter ---

}

