package com.easy.controller;

import com.easy.entity.po.ProjectFile;
import com.easy.entity.query.ProjectFileQuery;
import com.easy.entity.vo.ProjectFileVO;
import com.easy.entity.vo.ResponseVO;
import com.easy.service.ProjectFileService;
import com.easy.utils.ProjectFileTreeBuilder;
import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description: 项目文件 Controller
 * @date: 2025/08/09
 * @Author: Sena
 */

@RestController
@RequestMapping("/projectFile")
public class ProjectFileController extends ABaseController {

    @Resource
    private ProjectFileService projectFileService;

    /**
     * 备注: 目前使用到的接口
     */

    //根据项目id{projectId}返回文件树形结构
    @GetMapping("{projectId}/files")
    public ResponseVO<List<ProjectFileVO>> getProjectFiles(@PathVariable Integer projectId) {
        List<ProjectFile> files = projectFileService.getProjectFileByProjectId(projectId);
        List<ProjectFileVO> tree = ProjectFileTreeBuilder.build(files); // 构造成树形结构
        return getSuccessResponseVO(tree);
    }


    //根据根据项目id{projectId}和文件路径查询文件内容
    @GetMapping("{projectId}/file")
    public ResponseVO<String> getFileContent(@PathVariable Integer projectId,
                                             @RequestParam String path) {
        List<ProjectFile> files = projectFileService.getProjectFileByProjectIdAndPath(projectId, path);
        String content = (files != null && !files.isEmpty()) ? files.get(0).getContent() : "";
        return getSuccessResponseVO(content);
    }

    // 根据ProjectId删除文件
    @DeleteMapping("deleteByProjectFile/{projectId}")
    public ResponseVO deleteByProjectId(@PathVariable Integer projectId, HttpServletRequest request, HttpServletResponse response) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null || !"admin".equals(claims.get("role"))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            System.out.println("File：claims: " + claims);
            System.out.println("File：role: '" + claims.get("role") + "'");
            return ResponseVO.error("无权限访问");
        }

        projectFileService.deleteProjectFileByProjectId(projectId);
        return getSuccessResponseVO(null);
    }



    /**
     * 备注: 预留拓展接口
     */

    // 新增文件
    @PostMapping("add")
    public ResponseVO add(@RequestBody ProjectFile projectFile) {
        projectFileService.add(projectFile);
        return getSuccessResponseVO(null);
    }

    // 根据ID查询文件
    @GetMapping("get/{id}")
    public ResponseVO getById(@PathVariable Integer id) {
        ProjectFile file = projectFileService.getProjectFileById(id);
        return getSuccessResponseVO(file);
    }

    // 根据ID更新文件信息
    @PutMapping("update/{id}")
    public ResponseVO updateById(@PathVariable Integer id, @RequestBody ProjectFile projectFile) {
        projectFileService.updateProjectFileById(projectFile, id);
        return getSuccessResponseVO(null);
    }

    // 根据ID删除文件
    @DeleteMapping("delete/{id}")
    public ResponseVO deleteById(@PathVariable Integer id) {
        projectFileService.deleteProjectFileById(id);
        return getSuccessResponseVO(null);
    }


    @RequestMapping("addBatch")
    public ResponseVO addBatch(@RequestBody List<ProjectFile> projectFileList) {
        projectFileService.addBatch(projectFileList);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("addOrUpdateBatch")
    public ResponseVO addOrUpdateBatch(@RequestBody List<ProjectFile> projectFileList) {
        projectFileService.addOrUpdateBatch(projectFileList);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("getProjectFileById")
    public ResponseVO getProjectFileById(Integer id) {
        return getSuccessResponseVO(projectFileService.getProjectFileById(id));
    }

    @RequestMapping("updateProjectFileById")
    public ResponseVO updateProjectFileById(ProjectFile projectFile, Integer id) {
        projectFileService.updateProjectFileById(projectFile, id);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("deleteProjectFileById")
    public ResponseVO deleteProjectFileById(Integer id) {
        projectFileService.deleteProjectFileById(id);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("getProjectFileByProjectIdAndPath")
    public ResponseVO getProjectFileByProjectIdAndPath(Integer projectId, String path) {
        return getSuccessResponseVO(projectFileService.getProjectFileByProjectIdAndPath(projectId, path));
    }

    @RequestMapping("updateProjectFileByProjectIdAndPath")
    public ResponseVO updateProjectFileByProjectIdAndPath(ProjectFile projectFile, Integer projectId, String path) {
        projectFileService.updateProjectFileByProjectIdAndPath(projectFile, projectId, path);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("deleteProjectFileByProjectIdAndPath")
    public ResponseVO deleteProjectFileByProjectIdAndPath(Integer projectId, String path) {
        projectFileService.deleteProjectFileByProjectIdAndPath(projectId, path);
        return getSuccessResponseVO(null);
    }
}
