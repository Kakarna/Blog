package com.easy.mapper;

import com.easy.entity.po.ProjectFile;
import com.easy.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 项目文件表
 * @date: 2025/08/09
 * @Author: Sena
 */
public interface ProjectFileMappers<T, P> extends BaseMapper {

    /**
     * 根据Id查询
     */
    T selectById(@Param("id") Integer id);

    /**
     * 根据Id更新
     */
    Integer updateById(@Param("bean") T t, @Param("id") Integer id);

    /**
     * 根据Id删除
     */
    Integer deleteById(@Param("id") Integer id);


    /**
     * 根据projectId查询
     */
    List<ProjectFile> selectListByProjectId(@Param("projectId") Integer projectId);

    /**
     * 根据projectId删除
     */
    Integer deleteByProjectId(@Param("projectId") Integer projectId);

    /**
     * 根据项目ID和路径查询
     */
    List<ProjectFile> selectByProjectIdAndPath(@Param("projectId") Integer projectId, @Param("path") String path);


    /**
     * 根据项目ID和路径更新
     */
    Integer updateByProjectIdAndPath(@Param("bean") T t, @Param("projectId") Integer projectId, @Param("path") String path);

    /**
     * 根据项目ID和路径删除
     */
    Integer deleteByProjectIdAndPath(@Param("projectId") Integer projectId, @Param("path") String path);
}
