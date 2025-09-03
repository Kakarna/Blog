package com.easy.service.impl;

import com.easy.entity.po.Project;
import com.easy.entity.po.ProjectFile;
import com.easy.entity.query.ProjectQuery;
import com.easy.entity.query.SimplePage;
import com.easy.entity.vo.PaginationResultVO;
import com.easy.entity.vo.ProjectVO;
import com.easy.entity.vo.ResponseVO;
import com.easy.enums.PageSize;
import com.easy.mapper.ProjectFileMappers;
import com.easy.mapper.ProjectMappers;
import com.easy.service.ProjectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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


	@Override
	public Integer countAll() {
		return projectMappers.countAll();
	}

	public void addProjectWithFiles(ProjectVO projectVO) {
		Project project = new Project();
		project.setName(projectVO.getName());
		project.setDescription(projectVO.getDescription());
		project.setType(projectVO.getType());
		project.setReadme(projectVO.getReadme());
		project.setCreateTime(new Date());
		project.setUpdateTime(new Date());
		projectMappers.insert(project);

		Integer projectId = project.getId();  // 插入后获取ID

		List<ProjectFile> files = projectVO.getFiles();
		if (files != null) {
			for (ProjectFile fileVO : files) {
				ProjectFile file = new ProjectFile();
				file.setProjectId(projectId);
				file.setPath(fileVO.getPath());
				file.setContent(fileVO.getContent());
				file.setIsDir(fileVO.getIsDir());  // 这里赋值isDir
				projectFileMappers.insert(file);
			}
		}
	}




	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<Project> findListByParam(ProjectQuery query) {
		return projectMappers.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(ProjectQuery query) {
		return projectMappers.selectCount(query);
	}

	/**
	 * 分页查询
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
	 * 新增
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
	 * 批量新增
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
	 * 批量新增或修改
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
	 * 根据Id查询
	 */
	@Override
	public Project getProjectById(Integer id) {
		return projectMappers.selectById(id);
	}

	/**
	 * 根据Id更新
	 */
	@Override
	public Integer updateProjectById(Project project, Integer id) {
		project.setUpdateTime(new Date());
		return projectMappers.updateById(project, id);
	}

	/**
	 * 根据Id删除
	 */
	@Override
	public Integer deleteProjectById(Integer id) {
		return projectMappers.deleteById(id);
	}

	/**
	 * 根据Name查询
	 */
	@Override
	public Project getProjectByName(String name) {
		return projectMappers.selectByName(name);
	}

	/**
	 * 根据Name更新
	 */
	@Override
	public Integer updateProjectByName(Project project, String name) {
		project.setUpdateTime(new Date());
		return projectMappers.updateByName(project, name);
	}

	/**
	 * 根据Name删除
	 */
	@Override
	public Integer deleteProjectByName(String name) {
		return projectMappers.deleteByName(name);
	}
}
