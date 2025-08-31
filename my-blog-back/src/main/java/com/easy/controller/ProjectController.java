package com.easy.controller;

import com.easy.entity.po.Project;
import com.easy.entity.query.ProjectQuery;
import com.easy.entity.vo.ProjectVO;
import com.easy.entity.vo.ResponseVO;
import com.easy.service.ProjectService;
import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description: 项目表 Controller
 * @date: 2025/08/09
 * @Author: Sena
 */

@RestController
@RequestMapping("/project")
public class ProjectController extends ABaseController {

    @Resource
    private ProjectService projectService;


    /**
     * 备注: 目前使用到的接口
     */
    //分页查询
    @RequestMapping("loadDataList")
    public ResponseVO loadDataList(ProjectQuery query) {
        return getSuccessResponseVO(projectService.findListByPage(query));
    }
    //添加项目
    @RequestMapping("addWithFiles")
    public ResponseVO add(@RequestBody ProjectVO projectVO, HttpServletRequest request, HttpServletResponse response) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null || !"admin".equals(claims.get("role"))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseVO.error("无权限访问");
        }
        projectService.addProjectWithFiles(projectVO);
        return getSuccessResponseVO(null);
    }

    // 删除项目（按ID）
    @DeleteMapping("delete/{id}")
    public ResponseVO deleteById(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null || !"admin".equals(claims.get("role"))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            System.out.println("claims: " + claims);
            System.out.println("role: '" + claims.get("role") + "'");
            return ResponseVO.error("无权限访问");
        }

        projectService.deleteProjectById(id);
        return getSuccessResponseVO(null);
    }


    /**
     * 备注: 预留拓展接口
     */

    // 根据ID查询项目
    @GetMapping("get/{id}")
    public ResponseVO getById(@PathVariable Integer id) {
        Project project = projectService.getProjectById(id);
        return getSuccessResponseVO(project);
    }

    // 更新项目（按ID）
    @PutMapping("update/{id}")
    public ResponseVO updateById(@PathVariable Integer id, @RequestBody Project project) {
        projectService.updateProjectById(project, id);
        return getSuccessResponseVO(null);
    }


    // 根据项目名查询
    @GetMapping("getByName")
    public ResponseVO getByName(@RequestParam String name) {
        Project project = projectService.getProjectByName(name);
        return getSuccessResponseVO(project);
    }

    // 根据项目名更新
    @PutMapping("updateByName")
    public ResponseVO updateByName(@RequestParam String name, @RequestBody Project project) {
        projectService.updateProjectByName(project, name);
        return getSuccessResponseVO(null);
    }

    // 根据项目名删除
    @DeleteMapping("/deleteByName")
    public ResponseVO deleteByName(@RequestParam String name) {
        projectService.deleteProjectByName(name);
        return getSuccessResponseVO(null);
    }


    @RequestMapping("addBatch")
    public ResponseVO addBatch(@RequestBody List<Project> projectList) {
        projectService.addBatch(projectList);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("addOrUpdateBatch")
    public ResponseVO addOrUpdateBatch(@RequestBody List<Project> projectList) {
        projectService.addOrUpdateBatch(projectList);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("getProjectById")
    public ResponseVO getProjectById(Integer id) {
        return getSuccessResponseVO(projectService.getProjectById(id));
    }

    @RequestMapping("updateProjectById")
    public ResponseVO updateProjectById(Project project, Integer id) {
        projectService.updateProjectById(project, id);
        return getSuccessResponseVO(null);
    }


    @RequestMapping("deleteProjectById")
    public ResponseVO deleteProjectById(Integer id) {
        projectService.deleteProjectById(id);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("getProjectByName")
    public ResponseVO getProjectByName(String name) {
        return getSuccessResponseVO(projectService.getProjectByName(name));
    }

    @RequestMapping("updateProjectByName")
    public ResponseVO updateProjectByName(Project project, String name) {
        projectService.updateProjectByName(project, name);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("deleteProjectByName")
    public ResponseVO deleteProjectByName(String name) {
        projectService.deleteProjectByName(name);
        return getSuccessResponseVO(null);
    }
}
