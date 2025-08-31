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
     * @return
     */
	T selectById(@Param("id") Integer id);

    /**
     * 根据Id更新
     * @return
     */
	Integer updateById(@Param("bean") T t, @Param("id") Integer id);

    /**
     * 根据Id删除
     * @return
     */
	Integer deleteById(@Param("id") Integer id);

    /**
     * 根据Username查询
     * @return
     */
	T selectByUsername(@Param("username") String username);

    /**
     * 根据Username更新
     * @return
     */
	Integer updateByUsername(@Param("bean") T t, @Param("username") String username);

    /**
     * 根据Username删除
     * @return
     */
	Integer deleteByUsername(@Param("username") String username);

}