package com.easy.service;

import com.easy.entity.vo.PaginationResultVO;
import com.easy.entity.po.User;
import com.easy.entity.query.UserQuery;
import com.easy.entity.vo.ResponseVO;
import com.easy.entity.vo.UserLoginVO;
import com.easy.entity.vo.UserRegisterVO;

import java.util.List;
/**
 * @Description: 用户表 Service
 * @date: 2025/07/29
 * @Author: Sena
 */
public interface UserService {

    /**
     * 目前用到的
     */

    //获取公共账号 @return 返回 is_public=1 的用户，如果不存在则返回 null
    User getPublicUser();

    //登录接口
    ResponseVO login(UserLoginVO loginVO);

    //邮箱验证码接口
    ResponseVO sendEmailCode(String email,boolean checkUserEmailUnique);
    ResponseVO sendEmailCode(String email,boolean checkUserEmailUnique,Integer excludeUserId);

    //注册接口
    ResponseVO register(UserRegisterVO registerVO);

    //密码修改接口
    ResponseVO updatePassword(Integer id, String oldPassword, String newPassword);

    //邮箱修改接口
    ResponseVO updateEmail(Integer id, String oldEmailCode, String newEmail, String newEmailCode);


    /*
      预留
     */

    /**
     * 根据条件查询列表
     */
		List<User> findListByParam(UserQuery query);

    /**
     * 根据条件查询数量
     */
		Integer findCountByParam(UserQuery query);

    /**
     * 分页查询
     */
		PaginationResultVO<User> findListByPage(UserQuery query);

    /**
     * 新增
     */
	Integer add(User user);

    /**
     * 批量新增
     */
	Integer addBatch(List<User> userlist);

    /**
     * 批量新增或修改
     */
	Integer addOrUpdateBatch(List<User> userlist);

    /**
     * 根据Id查询
     * @return
     */
	User getUserById(Integer id);

    /**
     * 根据Id更新
     * @return
     */
	Integer updateUserById(User bean, Integer id);

    /**
     * 根据Id删除
     * @return
     */
	Integer deleteUserById(Integer id);

    /**
     * 根据Username查询
     * @return
     */
	User getUserByUsername(String username);

    /**
     * 根据Username更新
     * @return
     */
	Integer updateUserByUsername(User bean, String username);

    /**
     * 根据Username删除
     * @return
     */
	Integer deleteUserByUsername(String username);

}
