package com.easy.controller;

/**
 * 项目文件管理控制器
 * 提供项目文件树形结构展示和文件内容获取
 * 注意：删除操作需要管理员权限
 */
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
     * 根据项目id返回文件树形结构
     * 前端API: getProjectFiles - 用于获取项目文件树
     */
    @GetMapping("{projectId}/files")
    public ResponseVO<List<ProjectFileVO>> getProjectFiles(@PathVariable Integer projectId) {
        List<ProjectFile> files = projectFileService.getProjectFileByProjectId(projectId);
        List<ProjectFileVO> tree = ProjectFileTreeBuilder.build(files); // 构造成树形结构
        return getSuccessResponseVO(tree);
    }

    /**
     * 根据项目id和文件路径查询文件内容
     * 前端API: getFileContent - 用于获取单个文件内容
     */
    @GetMapping("{projectId}/file")
    public ResponseVO<String> getFileContent(@PathVariable Integer projectId,
                                             @RequestParam String path) {
        List<ProjectFile> files = projectFileService.getProjectFileByProjectIdAndPath(projectId, path);
        String content = (files != null && !files.isEmpty()) ? files.get(0).getContent() : "";
        return getSuccessResponseVO(content);
    }

    /**
     * 根据ProjectId删除文件
     * 前端API: deleteProjectFiles - 用于删除项目相关的所有文件
     */
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

    // ==================== 预留接口 ====================
    // 以下接口当前未被前端直接使用，预留用于后续功能扩展

    /**
     * 新增文件
     */
    @PostMapping("add")
    public ResponseVO add(@RequestBody ProjectFile projectFile) {
        projectFileService.add(projectFile);
        return getSuccessResponseVO(null);
    }

    /**
     * 根据ID查询文件
     */
    @GetMapping("get/{id}")
    public ResponseVO getById(@PathVariable Integer id) {
        ProjectFile file = projectFileService.getProjectFileById(id);
        return getSuccessResponseVO(file);
    }

    /**
     * 根据ID更新文件信息
     */
    @PutMapping("update/{id}")
    public ResponseVO updateById(@PathVariable Integer id, @RequestBody ProjectFile projectFile) {
        projectFileService.updateProjectFileById(projectFile, id);
        return getSuccessResponseVO(null);
    }

    /**
     * 根据ID删除文件
     */
    @DeleteMapping("delete/{id}")
    public ResponseVO deleteById(@PathVariable Integer id) {
        projectFileService.deleteProjectFileById(id);
        return getSuccessResponseVO(null);
    }

    /**
     * 批量新增文件
     */
    @RequestMapping("addBatch")
    public ResponseVO addBatch(@RequestBody List<ProjectFile> projectFileList) {
        projectFileService.addBatch(projectFileList);
        return getSuccessResponseVO(null);
    }

    /**
     * 批量新增或修改文件
     */
    @RequestMapping("addOrUpdateBatch")
    public ResponseVO addOrUpdateBatch(@RequestBody List<ProjectFile> projectFileList) {
        projectFileService.addOrUpdateBatch(projectFileList);
        return getSuccessResponseVO(null);
    }

    /**
     * 根据ID查询文件
     */
    @RequestMapping("getProjectFileById")
    public ResponseVO getProjectFileById(Integer id) {
        return getSuccessResponseVO(projectFileService.getProjectFileById(id));
    }

    /**
     * 根据ID更新文件
     */
    @RequestMapping("updateProjectFileById")
    public ResponseVO updateProjectFileById(ProjectFile projectFile, Integer id) {
        projectFileService.updateProjectFileById(projectFile, id);
        return getSuccessResponseVO(null);
    }

    /**
     * 根据ID删除文件
     */
    @RequestMapping("deleteProjectFileById")
    public ResponseVO deleteProjectFileById(Integer id) {
        projectFileService.deleteProjectFileById(id);
        return getSuccessResponseVO(null);
    }

    /**
     * 根据项目ID和路径查询文件
     */
    @RequestMapping("getProjectFileByProjectIdAndPath")
    public ResponseVO getProjectFileByProjectIdAndPath(Integer projectId, String path) {
        return getSuccessResponseVO(projectFileService.getProjectFileByProjectIdAndPath(projectId, path));
    }

    /**
     * 根据项目ID和路径更新文件
     */
    @RequestMapping("updateProjectFileByProjectIdAndPath")
    public ResponseVO updateProjectFileByProjectIdAndPath(ProjectFile projectFile, Integer projectId, String path) {
        projectFileService.updateProjectFileByProjectIdAndPath(projectFile, projectId, path);
        return getSuccessResponseVO(null);
    }

    /**
     * 根据项目ID和路径删除文件
     */
    @RequestMapping("deleteProjectFileByProjectIdAndPath")
    public ResponseVO deleteProjectFileByProjectIdAndPath(Integer projectId, String path) {
        projectFileService.deleteProjectFileByProjectIdAndPath(projectId, path);
        return getSuccessResponseVO(null);
    }
}
