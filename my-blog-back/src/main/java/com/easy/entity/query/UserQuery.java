package com.easy.entity.query;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Description: 用户表查询对象
 * @date: 2025/07/29
 * @Author: Sena
 */
@Setter
@Getter
public class UserQuery extends BaseQuery {
    /**
     * 主键ID
     */
	private Integer id;

    /**
     * 用户名
     */
	private String username;

	private String usernameFuzzy;

    /**
     * 密码（加密）
     */
	private String password;

	private String passwordFuzzy;

    /**
     * 邮箱
     */
	private String email;

	private String emailFuzzy;

    /**
     * 昵称
     */
	private String nickname;

	private String nicknameFuzzy;

    /**
     * 头像 URL
     */
	private String avatar;

	private String avatarFuzzy;

    /**
     * 角色（user/admin）
     */
	private String role;

	private String roleFuzzy;

    /**
     * 注册时间
     */
	private Date createTime;

	private String createTimeStart;

	private String createTimeEnd;

    /**
     * 更新时间
     */
	private Date updateTime;

	private String updateTimeStart;

	private String updateTimeEnd;

}