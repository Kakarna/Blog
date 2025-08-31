package com.easy.service;

import com.easy.entity.po.Project;
import com.easy.entity.query.ProjectQuery;
import com.easy.entity.vo.PaginationResultVO;
import com.easy.entity.vo.ProjectVO;

import java.util.List;

public interface ProjectService {

    void addProjectWithFiles(ProjectVO projectVO);

    List<Project> findListByParam(ProjectQuery query);

    Integer findCountByParam(ProjectQuery query);

    PaginationResultVO<Project> findListByPage(ProjectQuery query);

    Integer add(Project project);

    Integer addBatch(List<Project> projectList);

    Integer addOrUpdateBatch(List<Project> projectList);

    Project getProjectById(Integer id);

    Integer updateProjectById(Project project, Integer id);

    Integer deleteProjectById(Integer id);

    Project getProjectByName(String name);

    Integer updateProjectByName(Project project, String name);

    Integer deleteProjectByName(String name);
}
