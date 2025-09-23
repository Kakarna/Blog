package com.easy.service.impl;

import com.easy.entity.po.EmailCode;
import com.easy.entity.query.SimplePage;
import com.easy.entity.vo.ResponseVO;
import com.easy.entity.vo.UserLoginVO;
import com.easy.entity.vo.UserRegisterVO;
import com.easy.enums.PageSize;
import com.easy.entity.vo.PaginationResultVO;
import com.easy.entity.po.User;
import com.easy.entity.query.UserQuery;
import com.easy.mapper.EmailCodeMappers;
import com.easy.mapper.UserMappers;
import com.easy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
/**
 * @Description: 用户表 Service
 * @date: 2025/07/29
 * @Author: Sena
 */

@Service("userService")
public class UserServiceImpl implements UserService{

	@Resource
	private UserMappers<User,UserQuery> userMapper;

	@Resource
	private EmailCodeMappers emailCodeMapper;

	@Resource
	private JavaMailSender mailSender;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * 获取公共账号
	 * 用于技术笔记和技术分区的公共展示
	 */
	@Override
	public User getPublicUser() {
		UserQuery query = new UserQuery();
		query.setIsPublic(1);  // 查询公共账号

		List<User> list = userMapper.selectList(query);
		if (list != null && !list.isEmpty()) {
			return list.get(0); // 返回第一条公共账号
		}
		return null; // 如果没有公共账号，返回null
	}

	/**
	 * 用户登录
	 * 前端API: loginUser - 用于用户登录
	 */
	@Override
	public ResponseVO login(UserLoginVO loginVO) {
		String account = loginVO.getUsername(); // 这里可以是用户名或邮箱
		String password = loginVO.getPassword();

		// 1. 尝试按用户名查找
		User user = this.getUserByUsername(account);

		// 2. 如果用户名没找到，再按邮箱查找
		if (user == null) {
			user = this.getUserByEmail(account);
		}

		if (user == null) {
			return ResponseVO.error("用户名或密码错误");
		}

		// 3. 验证密码
		if (!passwordEncoder.matches(password, user.getPassword())) {
			return ResponseVO.error("用户名或密码错误");
		}

		// 5. 返回用户信息（过滤掉 password 字段）
		user.setPassword(null);
		return ResponseVO.ok(user);
	}

	/**
	 * 发送邮箱验证码
	 * 前端API: sendEmailCode, sendUpdateEmailCode - 用于注册和修改邮箱
	 */
	@Override
	public ResponseVO sendEmailCode(String email, boolean checkUserEmailUnique) {
		return sendEmailCode(email, checkUserEmailUnique, null);
	}

	@Override
	public ResponseVO sendEmailCode(String email, boolean checkUserEmailUnique, Integer excludeUserId) {
		if (checkUserEmailUnique) {
			User existingUser = getUserByEmail(email);
			if (existingUser != null && (excludeUserId == null || !existingUser.getId().equals(excludeUserId))) {
				return ResponseVO.error("该邮箱已被绑定");
			}
		}
		
		// 对于新邮箱验证码发送，立即检查邮箱是否已被绑定
		if (!checkUserEmailUnique && excludeUserId != null) {
			// 这是修改邮箱场景，发送新邮箱验证码时需要检查邮箱是否已被其他用户绑定
			User existingUser = getUserByEmail(email);
			if (existingUser != null && !existingUser.getId().equals(excludeUserId)) {
				return ResponseVO.error("该邮箱已被其他用户绑定");
			}
		}

		LocalDate today = LocalDate.now();

		// 2. 统计今天全局发送次数
		Integer totalToday = emailCodeMapper.countSendToday(today);
		if (totalToday != null && totalToday >= 200) {
			return ResponseVO.error("今日验证码发送次数已达上限，请明天再试");
		}

		// 3. 查找该邮箱是否已有验证码记录
		EmailCode record = emailCodeMapper.selectByEmail(email);
		
		// 检查验证码是否已存在且未过期
		if (record != null && record.getExpireTime() != null && record.getExpireTime().after(new Date())) {
			return ResponseVO.error("七天内已发送过验证码，请检查您的邮箱或稍后再试");
		}
		
		if (record == null) {
			record = new EmailCode();
			record.setEmail(email);
			record.setSendCount(1);
			record.setCreateTime(new Date());
			record.setSendDate(today);
		} else {
			// 今天已经发送过
			if (today.equals(record.getSendDate())) {
				record.setSendCount(record.getSendCount() + 1);
			} else {
				record.setSendCount(1);
				record.setCreateTime(new Date());
				record.setSendDate(today);
			}
		}

		// 4. 生成验证码并设置7天过期
		String code = String.valueOf((int)((Math.random() * 9 + 1) * 100000));
		record.setCode(code);
		record.setExpireTime(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)));

		// 插入或更新
		if (record.getId() == null) {
			emailCodeMapper.insert(record);
		} else {
			emailCodeMapper.update(record);
		}

		// 5. 发送邮件
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("3841583132@qq.com"); // ✅ 必须和SMTP登录邮箱一致
			message.setTo(email);
			message.setSubject("注册验证码");
			message.setText("您的注册验证码是：" + code + "，有效期7天。");
			mailSender.send(message);
		} catch (Exception e) {
			return ResponseVO.error("邮件发送失败: " + e.getMessage());
		}

		return ResponseVO.ok("验证码已发送，请查收邮箱");
	}

	/**
	 * 用户注册
	 * 前端API: registerUser - 用于新用户注册
	 */
	@Override
	public ResponseVO register(UserRegisterVO registerVO) {
		// 1. 校验验证码
		EmailCode code = emailCodeMapper.selectByEmail(registerVO.getEmail());
		if (code == null) {
			return ResponseVO.error("请先获取验证码");
		}
		if (code.getExpireTime() == null || code.getExpireTime().before(new Date())) {
			return ResponseVO.error("验证码已过期");
		}
		if (!code.getCode().equals(registerVO.getCode())) {
			return ResponseVO.error("验证码错误");
		}

		// 2. 创建用户
		User user = new User();
		user.setUsername(registerVO.getUsername());
		user.setPassword(passwordEncoder.encode(registerVO.getPassword()));
		user.setEmail(registerVO.getEmail());
		user.setNickname(registerVO.getNickname());
		user.setAvatar("https://cdn.example.com/default-avatar.png");
		user.setRole("USER");
		user.setCreateTime(new Date());
		user.setUpdateTime(new Date());

		userMapper.insert(user);

		return ResponseVO.ok("注册成功");
	}

	/**
	 * 密码修改
	 * 前端API: updateUserById - 用于修改密码
	 */
	@Override
	public ResponseVO updatePassword(Integer id, String oldPassword, String newPassword) {
		// 1. 获取用户信息
		User user = userMapper.selectById(id);
		if (user == null) {
			return ResponseVO.error("用户不存在");
		}

		// 2. 验证旧密码
		if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
			return ResponseVO.error("当前密码错误");
		}

		// 3. 更新密码
		user.setPassword(passwordEncoder.encode(newPassword));
		user.setUpdateTime(new Date());
		userMapper.updateById(user, id);

		// 返回需要重新登录的提示信息
		ResponseVO<String> response = ResponseVO.ok("密码修改成功");
		response.setInfo("密码修改成功，请重新登录");
		return response;
	}

	/**
	 * 邮箱修改
	 * 前端API: updateUserById - 用于修改邮箱
	 */
	@Override
	public ResponseVO updateEmail(Integer id, String oldEmailCode, String newEmail, String newEmailCode) {
		// 1. 获取用户信息
		User user = userMapper.selectById(id);
		if (user == null) {
			return ResponseVO.error("用户不存在");
		}

		// 2. 验证旧邮箱验证码
		EmailCode oldCode = emailCodeMapper.selectByEmail(user.getEmail());
		if (oldCode == null || !oldCode.getCode().equals(oldEmailCode)) {
			return ResponseVO.error("当前邮箱验证码错误");
		}

		// 3. 验证新邮箱验证码
		EmailCode newCode = emailCodeMapper.selectByEmail(newEmail);
		if (newCode == null || !newCode.getCode().equals(newEmailCode)) {
			return ResponseVO.error("新邮箱验证码错误");
		}

		// 4. 检查新邮箱是否已被使用
		User existingUser = getUserByEmail(newEmail);
		if (existingUser != null && !existingUser.getId().equals(id)) {
			return ResponseVO.error("新邮箱已被其他用户使用");
		}

		// 5. 更新邮箱
		user.setEmail(newEmail);
		user.setUpdateTime(new Date());
		userMapper.updateById(user, id);

		return ResponseVO.ok("邮箱修改成功");
	}

	/**
	 * 根据条件查询用户列表
	 * 前端API: fetchUsers - 用于获取用户列表
	 */
	public List<User> findListByParam(UserQuery query) {
		return this.userMapper.selectList(query);
	}

	/**
	 * 根据条件统计用户数量
	 * 前端API: fetchUsers - 用于分页查询
	 */
	public Integer findCountByParam(UserQuery query) {
		return this.userMapper.selectCount(query);
	}

	/**
	 * 分页查询用户列表
	 * 前端API: fetchUsers - 用于分页展示用户列表
	 */
	public PaginationResultVO<User> findListByPage(UserQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<User> list = this.findListByParam(query);
		return new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
	}

	/**
	 * 根据ID获取用户详情
	 * 前端API: getUserById - 用于获取用户详情
	 */
	public User getUserById(Integer id) {
		User user = this.userMapper.selectById(id);
		if (user != null && user.getPassword() != null) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		return user;
	}

	/**
	 * 根据ID更新用户
	 * 前端API: updateUserById - 用于更新用户信息
	 */
	public Integer updateUserById(User bean, Integer id) {
		// 如果用户传了密码，需要加密
		if (bean.getPassword() != null && !bean.getPassword().isEmpty()) {
			bean.setPassword(passwordEncoder.encode(bean.getPassword()));
		}
		bean.setUpdateTime(new Date());
		return this.userMapper.updateById(bean, id);
	}

	/**
	 * 根据ID删除用户
	 * 前端API: deleteUserById - 用于删除用户
	 */
	public Integer deleteUserById(Integer id) {
		return this.userMapper.deleteById(id);
	}

	/**
	 * 根据用户名查询用户
	 * 用于登录验证
	 */
	public User getUserByUsername(String username) {
		return this.userMapper.selectByUsername(username);
	}

	/**
	 * 根据邮箱查询用户
	 * 用于登录验证和邮箱验证
	 */
	public User getUserByEmail(String email) {
		return this.userMapper.selectByEmail(email);
	}

	// ==================== 预留接口 ====================
	// 以下接口当前未被前端直接使用，预留用于后续功能扩展

	/**
	 * 新增用户
	 * 前端API: addUser - 用于管理员添加用户
	 */
	public Integer add(User user) {
		return this.userMapper.insert(user);
	}

	/**
	 * 批量新增用户
	 */
	public Integer addBatch(List<User> userList) {
		if (userList == null || userList.isEmpty()) {
			return 0;
		}
		return this.userMapper.insertBatch(userList);
	}

	/**
	 * 批量新增或修改用户
	 */
	public Integer addOrUpdateBatch(List<User> userList) {
		if (userList == null || userList.isEmpty()) {
			return 0;
		}
		return this.userMapper.insertOrUpdateBatch(userList);
	}

	/**
	 * 根据用户名更新用户
	 */
	public Integer updateUserByUsername(User bean, String username) {
		return this.userMapper.updateByUsername(bean,username);
	}

	/**
	 * 根据用户名删除用户
	 */
	public Integer deleteUserByUsername(String username) {
		return this.userMapper.deleteByUsername(username);
	}
}
