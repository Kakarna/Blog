package com.easy.mapper;

import com.easy.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @Description: 技术笔记 Mapper 接口
 * @date: 2025/08/02
 * @Author: Sena
 */
public interface TechNoteMappers<T, P> extends BaseMapper {

    /**
     * 根据ID查询笔记
     */
    T selectById(@Param("id") Integer id);

    /**
     * 根据ID更新笔记
     */
    Integer updateById(@Param("bean") T t, @Param("id") Integer id);

    /**
     * 根据ID删除笔记
     */
    Integer deleteById(@Param("id") Integer id);

    /**
     * 根据标题查询笔记
     */
    T selectByTitle(@Param("title") String title);

    /**
     * 根据标题更新笔记
     */
    Integer updateByTitle(@Param("bean") T t, @Param("title") String title);

    /**
     * 根据标题删除笔记
     */
    Integer deleteByTitle(@Param("title") String title);

}
