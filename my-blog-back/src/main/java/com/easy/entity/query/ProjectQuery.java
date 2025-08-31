package com.easy.entity.query;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Description: 项目查询对象
 * @date: 2025/08/09
 * @Author: Sena
 */
@Setter
@Getter
public class ProjectQuery extends BaseQuery {

	private Integer id;
	private String name;
	private String nameFuzzy;

	private String description;
	private String descriptionFuzzy;

	private String type;
	private String typeFuzzy;

	private String readme;
	private String readmeFuzzy;

	private Date createTime;
	private String createTimeStart;
	private String createTimeEnd;

	private Date updateTime;
	private String updateTimeStart;
	private String updateTimeEnd;

	// --- Getter & Setter ---

}

