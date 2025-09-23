package com.easy.controller;

/**
 * 用户管理控制器
 * 提供用户登录、注册验证码发送等功能
 * 注意：部分接口需要管理员权限
 */
import com.easy.entity.po.User;
import com.easy.entity.query.UserQuery;
import com.easy.entity.vo.ResponseVO;
import com.easy.entity.vo.UserLoginVO;
import com.easy.entity.vo.UserRegisterVO;
import com.easy.service.UserService;
import com.easy.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

	/**
	 * 用户登录
	 * 前端API: login - 用于用户登录认证
	 */
	@PostMapping("login")
	public ResponseVO<String> login(@RequestBody UserLoginVO loginVO) {
		ResponseVO<User> response = userService.login(loginVO);
		if (response.getCode() == 200) {
			User user = response.getData();
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

	/**
	 * 发送注册验证码
	 * 前端API: sendRegisterCode - 用于用户注册时发送验证码
	 */
	@PostMapping("sendCodeForRegister")
	public ResponseVO sendCodeForRegister(@RequestParam String email) {
		return userService.sendEmailCode(email, true); // 校验邮箱唯一
	}

	/**
	 * 发送更新验证码
	 * 前端API: sendUpdateCode - 用于用户更新信息时发送验证码
	 */
	@PostMapping("sendCodeForUpdate")
	public ResponseVO sendCodeForUpdate(@RequestParam String email, @RequestParam(required = false) Integer userId) {
		return userService.sendEmailCode(email, false, userId); // 不校验邮箱唯一，但可以排除特定用户
	}

	/**
	 * 用户注册
	 * 前端API: register - 用于新用户注册
	 */
	@PostMapping("register")
	public ResponseVO register(@RequestBody UserRegisterVO registerVO) {
		return userService.register(registerVO);
	}

	/**
	 * 根据ID查询用户
	 * 调用方: TechNoteController/TechSectionController
	 */
	@RequestMapping("getUserById")
	public ResponseVO getUserById(Integer id) {
		return getSuccessResponseVO(this.userService.getUserById(id));
	}



	/**
	 * 根据ID更新用户
	 * 调用方: TechNoteController/TechSectionController/Setting页面
	 */
	@PostMapping("updateUserById")
	public ResponseVO updateUserById(@RequestBody Map<String, Object> params) {
		Integer id = (Integer) params.get("id");
		String updateType = (String) params.get("updateType");
		
		if ("password".equals(updateType)) {
			// 密码修改逻辑
			String oldPassword = (String) params.get("oldPassword");
			String newPassword = (String) params.get("newPassword");
			return userService.updatePassword(id, oldPassword, newPassword);
		} else if ("email".equals(updateType)) {
			// 邮箱修改逻辑
			String oldEmailCode = (String) params.get("oldEmailCode");
			String newEmail = (String) params.get("newEmail");
			String newEmailCode = (String) params.get("newEmailCode");
			return userService.updateEmail(id, oldEmailCode, newEmail, newEmailCode);
		} else {
			// 基本信息更新
			User bean = new User();
			bean.setNickname((String) params.get("nickname"));
			bean.setSignature((String) params.get("signature"));
			bean.setId(id);
			this.userService.updateUserById(bean, id);
			return getSuccessResponseVO(null);
		}
	}



	// ==================== 预留接口 ====================
	@RequestMapping("loadDataList")
	public ResponseVO loadDataList(UserQuery query) {
		return getSuccessResponseVO(userService.findListByPage(query));
	}

	@RequestMapping("add")
	public ResponseVO add(User user) {
		this.userService.add(user);
		return getSuccessResponseVO(null);
	}

	@RequestMapping("addBatch")
	public ResponseVO addBatch(@RequestBody List<User> userList) {
		this.userService.addBatch(userList);
		return getSuccessResponseVO(null);
	}

	@RequestMapping("addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<User> userList) {
		this.userService.addOrUpdateBatch(userList);
		return getSuccessResponseVO(null);
	}

	@RequestMapping("deleteUserById")
	public ResponseVO deleteUserById(Integer id) {
		this.userService.deleteUserById(id);
		return getSuccessResponseVO(null);
	}

	@RequestMapping("getUserByUsername")
	public ResponseVO getUserByUsername(String username) {
		return getSuccessResponseVO(this.userService.getUserByUsername(username));
	}

	@RequestMapping("updateUserByUsername")
	public ResponseVO updateUserByUsername(User bean, String username) {
		this.userService.updateUserByUsername(bean,username);
		return getSuccessResponseVO(null);
	}

	@RequestMapping("deleteUserByUsername")
	public ResponseVO deleteUserByUsername(String username) {
		this.userService.deleteUserByUsername(username);
		return getSuccessResponseVO(null);
	}

}
