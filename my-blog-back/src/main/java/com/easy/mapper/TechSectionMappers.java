package com.easy.mapper;

import com.easy.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @Description: 笔记分区 Mapper 接口
 * @date: 2025/08/01
 * @Author: Sena
 */
public interface TechSectionMappers<T, P> extends BaseMapper {

    /**
     * 根据ID查询分区
     */
    T selectById(@Param("id") Integer id);

    /**
     * 根据ID更新分区
     */
    Integer updateById(@Param("bean") T t, @Param("id") Integer id);

    /**
     * 根据ID删除分区
     */
    Integer deleteById(@Param("id") Integer id);

    /**
     * 根据名称查询分区
     */
    T selectByName(@Param("name") String name);

    /**
     * 根据名称更新分区
     */
    Integer updateByName(@Param("bean") T t, @Param("name") String name);

    /**
     * 根据名称删除分区
     */
    Integer deleteByName(@Param("name") String name);

}
