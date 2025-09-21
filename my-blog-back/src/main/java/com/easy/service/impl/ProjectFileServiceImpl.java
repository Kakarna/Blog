package com.easy.service.impl;

import com.easy.entity.po.ProjectFile;
import com.easy.entity.query.ProjectFileQuery;
import com.easy.entity.query.SimplePage;
import com.easy.entity.vo.PaginationResultVO;
import com.easy.mapper.ProjectFileMappers;
import com.easy.service.ProjectFileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Date;

/**
 * @Description: 项目文件表 Service
 * @date: 2025/08/09
 * @Author: Sena
 */
@Service("projectFileService")
public class ProjectFileServiceImpl implements ProjectFileService {

    @Resource
    private ProjectFileMappers<ProjectFile, ProjectFileQuery> projectFileMappers;

    /**
     * 根据项目Id查询文件列表
     * 调用方: ProjectFileController
     */
    @Override
    public List<ProjectFile> getProjectFileByProjectId(Integer projectId){
        return projectFileMappers.selectListByProjectId(projectId);
    }

    /**
     * 根据项目ID和路径查询文件内容
     * 调用方: ProjectFileController
     */
    @Override
    public List<ProjectFile> getProjectFileByProjectIdAndPath(Integer projectId, String path) {
        return projectFileMappers.selectByProjectIdAndPath(projectId, path);
    }

    /**
     * 根据ProjectId删除项目所有文件
     * 调用方: ProjectController
     */
    @Override
    public Integer deleteProjectFileByProjectId(Integer projectId) {
        return projectFileMappers.deleteByProjectId(projectId);
    }

    /**
     * 根据条件查询列表
     * 调用方: ProjectFileServiceImpl.findListByPage
     */
    @Override
    public List<ProjectFile> findListByParam(ProjectFileQuery query) {
        return projectFileMappers.selectList(query);
    }

    /**
     * 根据条件查询数量
     * 调用方: ProjectFileServiceImpl.findListByPage
     */
    @Override
    public Integer findCountByParam(ProjectFileQuery query) {
        return projectFileMappers.selectCount(query);
    }

    /**
     * 分页查询
     * 调用方: ProjectFileController
     */
    @Override
    public PaginationResultVO<ProjectFile> findListByPage(ProjectFileQuery query) {
        Integer count = findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? 15 : query.getPageSize();
        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<ProjectFile> list = findListByParam(query);
        return new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
    }

    /**
     * 新增项目文件
     * 调用方: ProjectServiceImpl.addProjectWithFiles
     */
    @Override
    public Integer add(ProjectFile projectFile) {
        Date now = new Date();
        projectFile.setCreateTime(now);
        projectFile.setUpdateTime(now);
        return projectFileMappers.insert(projectFile);
    }

    // ==================== 预留接口 ====================
    // 以下接口当前未被使用

    /**
     * 批量新增项目文件
     * 【预留接口】
     */
    @Override
    public Integer addBatch(List<ProjectFile> projectFileList) {
        if (projectFileList == null || projectFileList.isEmpty()) {
            return 0;
        }
        Date now = new Date();
        for (ProjectFile pf : projectFileList) {
            pf.setCreateTime(now);
            pf.setUpdateTime(now);
        }
        return projectFileMappers.insertBatch(projectFileList);
    }

    /**
     * 批量新增或修改项目文件
     * 【预留接口】
     */
    @Override
    public Integer addOrUpdateBatch(List<ProjectFile> projectFileList) {
        if (projectFileList == null || projectFileList.isEmpty()) {
            return 0;
        }
        Date now = new Date();
        for (ProjectFile pf : projectFileList) {
            pf.setUpdateTime(now);
            if (pf.getCreateTime() == null) {
                pf.setCreateTime(now);
            }
        }
        return projectFileMappers.insertOrUpdateBatch(projectFileList);
    }

    /**
     * 根据Id查询项目文件
     * 【预留接口】
     */
    @Override
    public ProjectFile getProjectFileById(Integer id) {
        return projectFileMappers.selectById(id);
    }

    /**
     * 根据Id更新项目文件
     * 【预留接口】
     */
    @Override
    public Integer updateProjectFileById(ProjectFile projectFile, Integer id) {
        projectFile.setUpdateTime(new Date());
        return projectFileMappers.updateById(projectFile, id);
    }

    /**
     * 根据Id删除项目文件
     * 【预留接口】
     */
    @Override
    public Integer deleteProjectFileById(Integer id) {
        return projectFileMappers.deleteById(id);
    }

    /**
     * 根据项目ID和路径更新文件
     * 【预留接口】
     */
    @Override
    public Integer updateProjectFileByProjectIdAndPath(ProjectFile projectFile, Integer projectId, String path) {
        projectFile.setUpdateTime(new Date());
        return projectFileMappers.updateByProjectIdAndPath(projectFile, projectId, path);
    }

    /**
     * 根据项目ID和路径删除文件
     * 【预留接口】
     */
    @Override
    public Integer deleteProjectFileByProjectIdAndPath(Integer projectId, String path) {
        return projectFileMappers.deleteByProjectIdAndPath(projectId, path);
    }
}
