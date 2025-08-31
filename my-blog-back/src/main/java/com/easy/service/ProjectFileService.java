package com.easy.service;

import com.easy.entity.po.ProjectFile;
import com.easy.entity.query.ProjectFileQuery;
import com.easy.entity.vo.PaginationResultVO;

import java.util.List;

public interface ProjectFileService {

    List<ProjectFile> findListByParam(ProjectFileQuery query);

    Integer findCountByParam(ProjectFileQuery query);

    PaginationResultVO<ProjectFile> findListByPage(ProjectFileQuery query);

    Integer add(ProjectFile projectFile);

    Integer addBatch(List<ProjectFile> projectFileList);

    Integer addOrUpdateBatch(List<ProjectFile> projectFileList);

    ProjectFile getProjectFileById(Integer id);

    Integer updateProjectFileById(ProjectFile projectFile, Integer id);

    Integer deleteProjectFileById(Integer id);

    Integer deleteProjectFileByProjectId(Integer id);

    List<ProjectFile> getProjectFileByProjectId(Integer id);

    List<ProjectFile> getProjectFileByProjectIdAndPath(Integer projectId, String path);

    Integer updateProjectFileByProjectIdAndPath(ProjectFile projectFile, Integer projectId, String path);

    Integer deleteProjectFileByProjectIdAndPath(Integer projectId, String path);
}
