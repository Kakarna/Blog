package com.easy.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通用 Mapper 接口
 *
 * @param <T> 实体类型
 * @param <P> 查询参数类型
 */
public interface BaseMapper<T, P> {

    /**
     * 插入
     *
     * @param t 实体
     * @return 插入行数
     */
    Integer insert(@Param("bean") T t);

    /**
     * 插入或更新
     *
     * @param t 实体
     * @return 插入或更新行数
     */
    Integer insertOrUpdate(@Param("bean") T t);

    /**
     * 批量插入
     *
     * @param list 实体列表
     * @return 插入行数
     */
    Integer insertBatch(@Param("list") List<T> list);

    /**
     * 批量插入或更新
     *
     * @param list 实体列表
     * @return 操作行数
     */
    Integer insertOrUpdateBatch(@Param("list") List<T> list);

    /**
     * 根据参数查询集合
     *
     * @param p 查询参数
     * @return 实体列表
     */
    List<T> selectList(@Param("query") P p);

    /**
     * 根据参数查询数量
     *
     * @param p 查询参数
     * @return 数量Integer
     */
    Integer selectCount(@Param("query") P p);
}
