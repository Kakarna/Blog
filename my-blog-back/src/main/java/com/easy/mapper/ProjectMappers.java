package com.easy.mapper;

import com.easy.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 项目表
 * @date: 2025/08/10
 * @Author: Sena
 */
public interface ProjectMappers<T, P> extends BaseMapper<T, P> {

    /**
     * 根据Id查询
     * @param id 主键id
     * @return 实体对象
     */
    T selectById(@Param("id") Integer id);

    /**
     * 根据Id更新
     * @param t 实体对象
     * @param id 主键id
     * @return 受影响行数
     */
    Integer updateById(@Param("bean") T t, @Param("id") Integer id);

    /**
     * 根据Id删除
     * @param id 主键id
     * @return 受影响行数
     */
    Integer deleteById(@Param("id") Integer id);

    /**
     * 根据项目名查询
     * @param name 项目名
     * @return 实体对象
     */
    T selectByName(@Param("name") String name);

    /**
     * 根据项目名更新
     * @param t 实体对象
     * @param name 项目名
     * @return 受影响行数
     */
    Integer updateByName(@Param("bean") T t, @Param("name") String name);

    /**
     * 根据项目名删除
     * @param name 项目名
     * @return 受影响行数
     */
    Integer deleteByName(@Param("name") String name);

    int countAll();
}
