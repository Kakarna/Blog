package com.easy.mapper;

import com.easy.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * @Description: 技术笔记 Mapper 接口
 * @date: 2025/08/02
 * @Author: Sena
 */
public interface TechNoteMappers<T, P> extends BaseMapper {


    /**
     * 查询笔记总数
     * 调用方: DashboardServiceImpl/TechNoteServiceImpl
     */
    Integer countAll();

    /**
     * 统计最近 N 天的笔记数量
     * 调用方: DashboardServiceImpl
     */
    Integer countRecent(int days);


    /**
     * 按分类统计笔记数量
     * 调用方: DashboardServiceImpl
     */
    List<Map<String, Object>> countByCategory();


    /**
     * 按天统计笔记数量
     * 调用方: DashboardServiceImpl
     */
    List<Map<String, Object>> countByDay(@Param("days") int days);


    /**
     * 根据ID查询笔记
     * 调用方: TechNoteServiceImpl
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

    /**
     * 自定义插入方法，包含所有字段
     */
    @Insert("INSERT INTO tech_notes (section_id, title, content, user_id) " +
            "VALUES (#{sectionId}, #{title}, #{content}, #{userId})")
    Integer insertWithUserId(@Param("sectionId") Integer sectionId,
                           @Param("title") String title,
                           @Param("content") String content,
                           @Param("userId") Integer userId);

    /**
     * 根据用户ID查询笔记
     * 调用方: TechNoteServiceImpl
     */
    List<T> findByUserId(@Param("userId") Integer userId);
}
