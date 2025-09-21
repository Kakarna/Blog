package com.easy.service;

import com.easy.entity.po.Project;
import com.easy.entity.po.ProjectFile;
import com.easy.entity.query.ProjectQuery;
import com.easy.entity.vo.PaginationResultVO;
import com.easy.entity.vo.ProjectVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProjectService {

    /**
     * 统计所有项目数量
     * 调用方: DashboardController/ProjectController
     */
    Integer countAll();

    /**
     * 添加项目及关联文件
     * 调用方: ProjectController
     */
    void addProjectWithFiles(ProjectVO projectVO, List<ProjectFile> files, Integer maxFileSizeKB);

    /**
     * 根据条件查询项目列表
     * 调用方: ProjectController
     */
    List<Project> findListByParam(ProjectQuery query);

    /**
     * 根据条件统计项目数量
     * 调用方: ProjectController
     */
    Integer findCountByParam(ProjectQuery query);

    /**
     * 分页查询项目列表
     * 调用方: ProjectController/SearchController
     */
    PaginationResultVO<Project> findListByPage(ProjectQuery query);

    /**
     * 添加项目
     * 调用方: ProjectController/ProjectServiceImpl
     */
    Integer add(Project project);

    // ==================== 预留接口 ====================
    Integer addBatch(List<Project> projectList);

    Integer addOrUpdateBatch(List<Project> projectList);

    Project getProjectById(Integer id);

    Integer updateProjectById(Project project, Integer id);

    Integer deleteProjectById(Integer id);

    Project getProjectByName(String name);

    Integer updateProjectByName(Project project, String name);

    Integer deleteProjectByName(String name);

    /**
     * 获取用户私有项目
     * 调用方: ProjectController
     */
    PaginationResultVO<Project> getPrivateProjects(ProjectQuery query, Integer userId);
}
