package com.easy.entity.po;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import com.easy.enums.DateTimePatternEnum; 
import com.easy.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;;

/**
 * @Description: 用户表
 * @date: 2025/07/29
 * @Author: Sena
 */
@Setter
@Getter
public class User implements Serializable {
    /**
     * 主键ID
     */
	private Integer id;

    /**
     * 用户名
     */
	private String username;

    /**
     * 密码（加密）
     */
	private String password;

    /**
     * 邮箱
     */
	private String email;

    /**
     * 昵称
     */
	private String nickname;

    /**
     * 头像 URL
     */
	private String avatar;

    /**
     * 角色（user/admin）
     */
	private String role;

    /**
     * 注册时间
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

    @Override
	public String toString() {
		return "主键ID:" + (id == null ? " 空 " : id) + ",用户名:" + (username == null ? " 空 " : username) + ",密码（加密）:" + (password == null ? " 空 " : password) + ",邮箱:" + (email == null ? " 空 " : email) + ",昵称:" + (nickname == null ? " 空 " : nickname) + ",头像 URL:" + (avatar == null ? " 空 " : avatar) + ",角色（user/admin）:" + (role == null ? " 空 " : role) + ",注册时间:" + (createTime == null ? " 空 " : DateUtils.format(createTime,DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + ",更新时间:" + (updateTime == null ? " 空 " : DateUtils.format(updateTime,DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
	}
}