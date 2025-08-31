package com.easy.controller;

import com.easy.entity.po.User;
import com.easy.entity.query.UserQuery;
import com.easy.entity.vo.ResponseVO;
import com.easy.entity.vo.UserLoginVO;
import com.easy.entity.vo.UserRegisterVO;
import com.easy.service.UserService;
import com.easy.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 用户表 Controller
 * @date: 2025/07/29
 * @Author: Sena
 */

@RestController
@RequestMapping("/user")
public class UserController extends ABaseController {

	@Resource
	private UserService userService;
	@Autowired
	private JwtUtils jwtUtils;

	/**
	 * 备注: 目前使用到的接口
	 */

	@PostMapping("login")
	public ResponseVO<String> login(@RequestBody UserLoginVO loginVO) {
		ResponseVO<User> response = userService.login(loginVO);
		if (response.getCode() == 200) {
			User user = response.getData();
			// Java 8 版本兼容写法
			Map<String, Object> claims = new HashMap<>();
			claims.put("nickname", user.getNickname());
			claims.put("id", user.getId());
			claims.put("username", user.getUsername());
			claims.put("role", user.getRole());

			String token = jwtUtils.generateToken(claims, 7 * 24 * 60 * 60 * 1000L); // 7 天有效期
			return ResponseVO.ok(token);
		}
		return ResponseVO.error("用户名或密码错误");
	}

	@PostMapping("register")
	public ResponseVO<Void> register(@RequestBody UserRegisterVO registerVO) {
		return userService.register(registerVO);
	}



	/*
      备注: 预留拓展接口
     */

	@RequestMapping("loadDataList")
	public ResponseVO loadDataList(UserQuery query) {
		return getSuccessResponseVO(userService.findListByPage(query));
	}
	/**
	 * 新增
	 */

	@RequestMapping("add")
	public ResponseVO add(User user) {
		this.userService.add(user);
		return getSuccessResponseVO(null);
	}

    /**
     * 批量新增
     */

	@RequestMapping("addBatch")
	public ResponseVO addBatch(@RequestBody List<User> userList) {
		this.userService.addBatch(userList);
		return getSuccessResponseVO(null);
	}

    /**
     * 批量新增或修改
     */

	@RequestMapping("addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<User> userList) {
		this.userService.addOrUpdateBatch(userList);
		return getSuccessResponseVO(null);
	}

    /**
     * 根据Id查询
     * @return
     */

	@RequestMapping("getUserById")
	public ResponseVO getUserById(Integer id) {
		return getSuccessResponseVO(this.userService.getUserById(id));
	}

    /**
     * 根据Id更新
     * @return
     */

	@RequestMapping("updateUserById")
	public ResponseVO updateUserById(User bean, Integer id) {
		this.userService.updateUserById(bean,id);
		return getSuccessResponseVO(null);
	}

    /**
     * 根据Id删除
     * @return
     */

	@RequestMapping("deleteUserById")
	public ResponseVO deleteUserById(Integer id) {
		this.userService.deleteUserById(id);
		return getSuccessResponseVO(null);
	}

    /**
     * 根据Username查询
     * @return
     */

	@RequestMapping("getUserByUsername")
	public ResponseVO getUserByUsername(String username) {
		return getSuccessResponseVO(this.userService.getUserByUsername(username));
	}

    /**
     * 根据Username更新
     * @return
     */

	@RequestMapping("updateUserByUsername")
	public ResponseVO updateUserByUsername(User bean, String username) {
		this.userService.updateUserByUsername(bean,username);
		return getSuccessResponseVO(null);
	}

    /**
     * 根据Username删除
     * @return
     */

	@RequestMapping("deleteUserByUsername")
	public ResponseVO deleteUserByUsername(String username) {
		this.userService.deleteUserByUsername(username);
		return getSuccessResponseVO(null);
	}

}
