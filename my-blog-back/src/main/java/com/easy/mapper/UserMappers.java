package com.easy.mapper;


import com.easy.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;

/**
 * @Description: 用户表
 * @date: 2025/07/29
 * @Author: Sena
 */
public interface UserMappers<T, P> extends BaseMapper {

    /**
     * 根据Id查询
     * 调用方: UserServiceImpl/TechNoteServiceImpl/TechSectionServiceImpl
     */
	T selectById(@Param("id") Integer id);

    // ==================== 预留接口 ====================
    // 以下接口当前未被使用

    /**
     * 根据Id更新
     * 【预留接口】
     */
	Integer updateById(@Param("bean") T t, @Param("id") Integer id);

    /**
     * 根据Id删除
     * 【预留接口】
     */
	Integer deleteById(@Param("id") Integer id);

    /**
     * 根据Username查询
     * 调用方: UserServiceImpl
     */
	T selectByUsername(@Param("username") String username);

    /**
     * 根据Username更新
     * 【预留接口】
     */
	Integer updateByUsername(@Param("bean") T t, @Param("username") String username);

    /**
     * 根据Username删除
     * 【预留接口】
     */
	Integer deleteByUsername(@Param("username") String username);

    /**
     * 根据Email查询
     * 调用方: UserServiceImpl
     */
    T selectByEmail(@Param("email") String email);

}