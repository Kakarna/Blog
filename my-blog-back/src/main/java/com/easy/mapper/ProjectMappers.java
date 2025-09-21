package com.easy.mapper;

import com.easy.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 项目表
 * @date: 2025/08/10
 * @Author: Sena
 */
public interface ProjectMappers<T, P> extends BaseMapper<T, P> {

    /**
     * 统计所有项目数量
     * 调用方: ProjectServiceImpl/DashboardServiceImpl
     */
    int countAll();

    /**
     * 根据用户ID查询项目
     * 调用方: ProjectServiceImpl
     */
    List<T> findByUserId(@Param("userId") Integer userId);

    /**
     * 根据Id查询
     * 调用方: ProjectServiceImpl
     */
    T selectById(@Param("id") Integer id);

    /**
     * 根据Id删除
     * 调用方: ProjectServiceImpl
     */
    Integer deleteById(@Param("id") Integer id);

    // ==================== 预留接口 ====================
    // 以下接口当前未被使用

    /**
     * 根据Id更新
     * 【预留接口】
     */
    Integer updateById(@Param("bean") T t, @Param("id") Integer id);

    /**
     * 根据项目名查询
     * 【预留接口】
     */
    T selectByName(@Param("name") String name);

    /**
     * 根据项目名更新
     * 【预留接口】
     */
    Integer updateByName(@Param("bean") T t, @Param("name") String name);

    /**
     * 根据项目名删除
     * 【预留接口】
     */
    Integer deleteByName(@Param("name") String name);
}
