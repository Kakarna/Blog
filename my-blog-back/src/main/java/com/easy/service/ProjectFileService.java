package com.easy.service;

import com.easy.entity.po.ProjectFile;
import com.easy.entity.query.ProjectFileQuery;
import com.easy.entity.vo.PaginationResultVO;

import java.util.List;

public interface ProjectFileService {

    /**
     * 根据条件查询列表
     * 调用方: ProjectFileController
     */
    List<ProjectFile> findListByParam(ProjectFileQuery query);

    /**
     * 根据条件查询数量
     * 调用方: ProjectFileController
     */
    Integer findCountByParam(ProjectFileQuery query);

    /**
     * 分页查询
     * 调用方: ProjectFileController
     */
    PaginationResultVO<ProjectFile> findListByPage(ProjectFileQuery query);

    /**
     * 添加项目文件
     * 调用方: ProjectServiceImpl.addProjectWithFiles
     */
    Integer add(ProjectFile projectFile);

    // ==================== 预留接口 ====================
    Integer addBatch(List<ProjectFile> projectFileList);

    Integer addOrUpdateBatch(List<ProjectFile> projectFileList);

    ProjectFile getProjectFileById(Integer id);

    Integer updateProjectFileById(ProjectFile projectFile, Integer id);

    Integer deleteProjectFileById(Integer id);

    /**
     * 根据项目ID删除所有文件
     * 调用方: ProjectController
     */
    Integer deleteProjectFileByProjectId(Integer id);

    /**
     * 根据项目ID获取文件列表
     * 调用方: ProjectFileController
     */
    List<ProjectFile> getProjectFileByProjectId(Integer id);

    /**
     * 根据项目ID和路径获取文件
     * 调用方: ProjectFileController
     */
    List<ProjectFile> getProjectFileByProjectIdAndPath(Integer projectId, String path);

    Integer updateProjectFileByProjectIdAndPath(ProjectFile projectFile, Integer projectId, String path);

    Integer deleteProjectFileByProjectIdAndPath(Integer projectId, String path);
}
