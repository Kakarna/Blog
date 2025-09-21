package com.easy.service.impl;

import com.easy.entity.po.Project;
import com.easy.entity.po.ProjectFile;
import com.easy.entity.query.ProjectQuery;
import com.easy.entity.query.SimplePage;
import com.easy.entity.vo.PaginationResultVO;
import com.easy.entity.vo.ProjectVO;
import com.easy.enums.PageSize;
import com.easy.mapper.ProjectFileMappers;
import com.easy.mapper.ProjectMappers;
import com.easy.service.ProjectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * @Description: 项目表 Service
 * @date: 2025/08/09
 * @Author: Sena
 */
@Service("projectService")
public class ProjectServiceImpl implements ProjectService {

	@Resource
	private ProjectMappers<Project, ProjectQuery> projectMappers;
	@Resource
	private ProjectFileMappers<ProjectFile,ProjectQuery> projectFileMappers;

	/**
	 * 统计所有项目数量
	 * 调用方: DashboardController/ProjectController
	 */
	@Override
	public Integer countAll() {
		return projectMappers.countAll();
	}

	/**
	 * 添加项目及其关联文件
	 * 调用方: ProjectController
	 */
	public void addProjectWithFiles(ProjectVO projectVO, List<ProjectFile> files, Integer maxFileSizeKB) {
		// 添加空值检查
		if (projectVO == null) {
			throw new IllegalArgumentException("projectVO 不能为 null");
		}

		if (files == null) {
			files = new ArrayList<>(); // 使用空列表而不是 null
		}

		if (maxFileSizeKB == null) {
			maxFileSizeKB = 1024; // 默认值
		}

		// 1. 保存项目基础信息
		Project project = new Project();
		project.setName(projectVO.getName());
		project.setDescription(projectVO.getDescription());
		project.setType(projectVO.getType());
		project.setReadme(projectVO.getReadme());
		project.setCreateTime(new Date());
		project.setUpdateTime(new Date());
		project.setUserId(projectVO.getUserId());
		projectMappers.insert(project);

		Integer projectId = project.getId();

		// 2. 保存文件
		for (ProjectFile pf : files) {
			pf.setProjectId(projectId);
			// 确保 isDir 有值
			if (pf.getIsDir() == null) {
				pf.setIsDir(false);
			}

			// 处理内容
			if (pf.getContent() != null) {
				// 仅保留小文件的内容，超过限制的已经是 null
				if (pf.getContent().getBytes(StandardCharsets.UTF_8).length > maxFileSizeKB * 1024L) {
					pf.setContent(null);
				}
			}

			projectFileMappers.insert(pf);
		}
	}

	/**
	 * 根据条件查询列表
	 * 调用方: ProjectController/SearchController
	 */
	@Override
	public List<Project> findListByParam(ProjectQuery query) {
		return projectMappers.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 * 调用方: ProjectController/SearchController
	 */
	@Override
	public Integer findCountByParam(ProjectQuery query) {
		return projectMappers.selectCount(query);
	}

	/**
	 * 分页查询
	 * 调用方: ProjectController/SearchController
	 */
	@Override
	public PaginationResultVO<Project> findListByPage(ProjectQuery query) {
		Integer count = findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<Project> list = findListByParam(query);
		return new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
	}

	/**
	 * 新增项目
	 * 调用方: ProjectServiceImpl.addProjectWithFiles
	 */
	@Override
	public Integer add(Project project) {
		project.setName(project.getName());
		project.setDescription(project.getDescription());
		project.setType(project.getType());
		project.setReadme(project.getReadme());
		project.setCreateTime(new Date());
		project.setUpdateTime(new Date());
		return projectMappers.insert(project);
	}

	/**
	 * 根据Id查询项目
	 * 调用方: ProjectController
	 */
	@Override
	public Project getProjectById(Integer id) {
		return projectMappers.selectById(id);
	}

	/**
	 * 根据Id删除项目
	 * 调用方: ProjectController
	 */
	@Override
	public Integer deleteProjectById(Integer id) {
		return projectMappers.deleteById(id);
	}

	/**
	 * 获取用户私有项目
	 * 调用方: ProjectController
	 */
	@Override
	public PaginationResultVO<Project> getPrivateProjects(ProjectQuery query, Integer userId) {
		if (userId == null) {
			throw new IllegalArgumentException("用户ID不能为空");
		}
		
		// 设置默认分页参数，避免空指针异常
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		Integer pageNo = query.getPageNo() == null ? 1 : query.getPageNo();
		
		List<Project> projects = projectMappers.findByUserId(userId);
		int totalCount = projects.size();
		int totalPage = (int) Math.ceil((double)totalCount / pageSize);
		
		return new PaginationResultVO<>(totalCount, pageSize, pageNo, totalPage, projects);
	}

	// ==================== 预留接口 ====================
	// 以下接口当前未被使用

	/**
	 * 批量新增项目
	 * 【预留接口】
	 */
	@Override
	public Integer addBatch(List<Project> projectList) {
		if (projectList == null || projectList.isEmpty()) {
			return 0;
		}
		// 设置时间
		Date now = new Date();
		for (Project p : projectList) {
			p.setCreateTime(now);
			p.setUpdateTime(now);
		}
		return projectMappers.insertBatch(projectList);
	}

	/**
	 * 批量新增或修改项目
	 * 【预留接口】
	 */
	@Override
	public Integer addOrUpdateBatch(List<Project> projectList) {
		if (projectList == null || projectList.isEmpty()) {
			return 0;
		}
		Date now = new Date();
		for (Project p : projectList) {
			p.setUpdateTime(now);
			if (p.getCreateTime() == null) {
				p.setCreateTime(now);
			}
		}
		return projectMappers.insertOrUpdateBatch(projectList);
	}

	/**
	 * 根据Id更新项目
	 * 【预留接口】
	 */
	@Override
	public Integer updateProjectById(Project project, Integer id) {
		project.setUpdateTime(new Date());
		return projectMappers.updateById(project, id);
	}

	/**
	 * 根据Name查询项目
	 * 【预留接口】
	 */
	@Override
	public Project getProjectByName(String name) {
		return projectMappers.selectByName(name);
	}

	/**
	 * 根据Name更新项目
	 * 【预留接口】
	 */
	@Override
	public Integer updateProjectByName(Project project, String name) {
		project.setUpdateTime(new Date());
		return projectMappers.updateByName(project, name);
	}

	/**
	 * 根据Name删除项目
	 * 【预留接口】
	 */
	@Override
	public Integer deleteProjectByName(String name) {
		return projectMappers.deleteByName(name);
	}
}
