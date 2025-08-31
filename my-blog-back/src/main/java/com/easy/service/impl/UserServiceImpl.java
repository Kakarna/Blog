package com.easy.service.impl;

import com.easy.entity.query.SimplePage;
import com.easy.entity.vo.ResponseVO;
import com.easy.entity.vo.UserLoginVO;
import com.easy.entity.vo.UserRegisterVO;
import com.easy.enums.PageSize;
import com.easy.entity.vo.PaginationResultVO;
import com.easy.entity.po.User;
import com.easy.entity.query.UserQuery;
import com.easy.mapper.UserMappers;
import com.easy.service.UserService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
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
	private UserMappers<User,UserQuery> userMappers;

	// UserServiceImpl.java
	@Override
	public ResponseVO login(UserLoginVO loginVO) {
		String username = loginVO.getUsername();
		String password = loginVO.getPassword();

		User user = this.getUserByUsername(username);
		if (user == null || !user.getPassword().equals(password)) {
			return ResponseVO.error("用户名或密码错误");
		}

		// 可扩展：生成 token，设置 session，记录登录日志等
		return ResponseVO.ok(user);  // 返回用户信息（可选过滤掉 password 字段）
	}

	@Override
	public ResponseVO register(UserRegisterVO registerVO) {
		if (getUserByUsername(registerVO.getUsername()) != null) {
			return ResponseVO.error("用户名已存在");
		}

		User user = new User();
		user.setUsername(registerVO.getUsername());
		user.setPassword(registerVO.getPassword());
		user.setEmail(registerVO.getEmail());
		user.setNickname(registerVO.getNickname());
		user.setRole("USER");
		user.setCreateTime(new Date());
		user.setUpdateTime(new Date());

		this.userMappers.insert(user);
		return ResponseVO.ok(null);
	}



	/**
     * 根据条件查询列表
     */
	public List<User> findListByParam(UserQuery query) {
		return this.userMappers.selectList(query);
	}

    /**
     * 根据条件查询数量
     */
	public Integer findCountByParam(UserQuery query) {
		return this.userMappers.selectCount(query);
	}

    /**
     * 分页查询
     */
	public PaginationResultVO<User> findListByPage(UserQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<User> list = this.findListByParam(query);
		PaginationResultVO<User> result = new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

    /**
     * 新增
     */
	public Integer add(User user) {
		return this.userMappers.insert(user);
	}

    /**
     * 批量新增
     */
	public Integer addBatch(List<User> userList) {
		if (userList == null || userList.isEmpty()) {
			return 0;
		}
		return this.userMappers.insertBatch(userList);
	}

    /**
     * 批量新增或修改
     */
	public Integer addOrUpdateBatch(List<User> userList) {
		if (userList == null || userList.isEmpty()) {
			return 0;
		}
		return this.userMappers.insertOrUpdateBatch(userList);
	}

    /**
     * 根据Id查询
     * @return
     */
	public User getUserById(Integer id) {
		return this.userMappers.selectById(id);
	}

    /**
     * 根据Id更新
     * @return
     */
	public Integer updateUserById(User bean, Integer id) {
		return this.userMappers.updateById(bean,id);
	}

    /**
     * 根据Id删除
     * @return
     */
	public Integer deleteUserById(Integer id) {
		return this.userMappers.deleteById(id);
	}

    /**
     * 根据Username查询
     * @return
     */
	public User getUserByUsername(String username) {
		return this.userMappers.selectByUsername(username);
	}

    /**
     * 根据Username更新
     * @return
     */
	public Integer updateUserByUsername(User bean, String username) {
		return this.userMappers.updateByUsername(bean,username);
	}

    /**
     * 根据Username删除
     * @return
     */
	public Integer deleteUserByUsername(String username) {
		return this.userMappers.deleteByUsername(username);
	}

}
