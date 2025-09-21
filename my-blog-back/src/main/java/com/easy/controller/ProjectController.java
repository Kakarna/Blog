package com.easy.controller;

/**
 * 项目管理控制器
 * 提供项目的CRUD操作及文件上传功能
 * 包含公共项目和私人项目管理接口
 * 注意：部分接口需要管理员权限
 */
import com.easy.entity.po.Project;
import com.easy.entity.po.ProjectFile;
import com.easy.entity.query.ProjectQuery;
import com.easy.entity.vo.ProjectVO;
import com.easy.entity.vo.ResponseVO;
import com.easy.service.ProjectService;
import com.easy.service.UserService;
import com.easy.utils.MultipartFileFromFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;

import java.io.*;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

    @Resource
    private UserService userService;

    /**
     * 获取项目总数
     * 前端API: 用于统计项目总数
     */
    @GetMapping("/count")
    public ResponseVO<Integer> getProjectCount() {
        int count = projectService.countAll();
        return ResponseVO.ok(count);
    }

    /**
     * 公共项目分页查询（所有人可见）
     * 前端API: getPublicProjects - 用于获取公共项目列表
     */
    @GetMapping("publicProjects")
    public ResponseVO getPublicProjects(ProjectQuery query) {
        query.setUserId(userService.getPublicUser().getId()); // 不限制 userId，查询所有公共项目
        return getSuccessResponseVO(projectService.findListByPage(query));
    }

    /**
     * 当前用户的私人项目分页查询（需要登录）
     * 前端API: getMyProjects - 用于获取当前用户或指定用户的项目列表
     */
    @GetMapping("privateProjects")
    public ResponseVO getPrivateProjects(ProjectQuery query) {

        return getSuccessResponseVO(projectService.getPrivateProjects(query,query.getUserId()));
    }

    /**
     * 添加项目及其关联文件
     * 前端API: addProjectWithFiles - 用于创建新项目并上传项目文件
     */
    @PostMapping("/addWithFiles")
    public ResponseVO addWithFiles(
            @RequestPart(value = "project", required = false) ProjectVO project,
            @RequestParam(value = "chunk", required = false) MultipartFile chunk,
            @RequestParam(value = "chunkIndex", required = false) Integer chunkIndex,
            @RequestParam(value = "totalChunks", required = false) Integer totalChunks,
            @RequestParam(value = "fileName", required = false) String fileName,
            @RequestParam(value = "maxFileSizeKB", required = false) Integer maxFileSizeKB,
            @RequestParam(value = "uploadId", required = false) String uploadId,
            @RequestParam(value = "merge", required = false) String mergeFlag,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            System.out.println("Received request with parameters:");
            System.out.println("project: " + project);
            System.out.println("fileName: " + fileName);
            System.out.println("mergeFlag: " + mergeFlag);
            System.out.println("totalChunks: " + totalChunks);
            System.out.println("chunkIndex: " + chunkIndex);

            if (fileName == null) return ResponseVO.error("fileName 不能为空");

            // 使用 uploadId 或 fileName 生成唯一临时目录
            if (uploadId == null || uploadId.isEmpty()) {
                uploadId = fileName.replaceAll("\\W", "_");
            }

            File tempDir = new File(System.getProperty("java.io.tmpdir"), "upload_chunks_" + uploadId);
            if (!tempDir.exists()) tempDir.mkdirs();

            // 分片上传
            if (chunk != null && chunkIndex != null) {
                File partFile = new File(tempDir, "chunk_" + chunkIndex);
                chunk.transferTo(partFile);
                System.out.println("分片 " + chunkIndex + " 上传成功");
                return ResponseVO.ok("分片上传成功");
            }

            // 合并分片
            if ("true".equalsIgnoreCase(mergeFlag)) {
                // 检查必要的参数
                if (totalChunks == null) {
                    // 尝试从临时目录中计算分片数量
                    File[] chunkFiles = tempDir.listFiles((dir, name) -> name.startsWith("chunk_"));
                    if (chunkFiles != null && chunkFiles.length > 0) {
                        totalChunks = chunkFiles.length;
                        System.out.println("自动检测到分片数量: " + totalChunks);
                    } else {
                        return ResponseVO.error("无法确定分片数量，请提供 totalChunks 参数");
                    }
                }

                File zipFile = new File(tempDir, fileName);
                mergeChunks(tempDir, totalChunks, zipFile);

                System.out.println("Zip 文件已合并完成: " + zipFile.getAbsolutePath());

                if (project == null) {
                    project = new ProjectVO();
                    System.out.println("未上传 project JSON，使用空对象");
                }

                // === 从 token 获取 userId ===
                Claims claims = (Claims) request.getAttribute("claims");
                if (claims == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return ResponseVO.error("未登录或 token 无效");
                }
                Integer userId = (Integer) claims.get("id");
                System.out.println(userId);
                project.setUserId(userId);

                if (maxFileSizeKB == null) maxFileSizeKB = 1024; // 默认 1MB

                List<ProjectFile> projectFiles = unzipToProjectFiles(zipFile, maxFileSizeKB);

                System.out.println("解压出 " + projectFiles.size() + " 个文件");

                // 保存到数据库
                projectService.addProjectWithFiles(project, projectFiles, maxFileSizeKB);

                // 删除临时目录
                deleteDir(tempDir);

                return ResponseVO.ok("上传成功");
            }

            return ResponseVO.error("参数不足，无法处理请求");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.error("上传失败: " + e.getMessage());
        }
    }

    /**
     * 删除项目（按ID）
     * 前端API: deleteProject - 用于删除项目
     */
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
     * 根据ID查询项目
     * 前端API: getProjectDetail - 用于获取项目详情
     */
    @GetMapping("get/{id}")
    public ResponseVO getById(@PathVariable Integer id) {
        Project project = projectService.getProjectById(id);
        return getSuccessResponseVO(project);
    }

    // 分片合并
    private void mergeChunks(File tempDir, int totalChunks, File zipFile) throws IOException {
        // 先检查所有分片是否存在
        for (int i = 0; i < totalChunks; i++) {
            File part = new File(tempDir, "chunk_" + i);
            if (!part.exists()) {
                throw new FileNotFoundException("缺少分片: " + part.getName() + "，期望 " + totalChunks + " 个分片，但缺少第 " + i + " 个");
            }
        }

        try (FileOutputStream fos = new FileOutputStream(zipFile)) {
            for (int i = 0; i < totalChunks; i++) {
                File part = new File(tempDir, "chunk_" + i);
                try (FileInputStream fis = new FileInputStream(part)) {
                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = fis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                }
                System.out.println("已合并分片: " + i);
            }
        }
    }

    // 删除临时目录
    private void deleteDir(File dir) {
        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) deleteDir(f);
        }
        dir.delete();
    }

    // 解压 ZIP 文件生成 ProjectFile
    public static List<ProjectFile> unzipToProjectFiles(File zipFile, Integer maxFileSizeKB) throws IOException {
        List<ProjectFile> files = new ArrayList<>();

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                ProjectFile pf = new ProjectFile();
                pf.setPath(entry.getName()); // 使用 path 保存相对路径
                pf.setIsDir(entry.isDirectory());

                if (!entry.isDirectory()) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = zis.read(buffer)) != -1) {
                        baos.write(buffer, 0, len);
                    }

                    byte[] fileBytes = baos.toByteArray();
                    if (fileBytes.length <= maxFileSizeKB * 1024L) {
                        pf.setContent(new String(fileBytes, StandardCharsets.UTF_8));
                    } else {
                        pf.setContent(null); // 超过限制不保存内容
                    }
                }

                files.add(pf);
                zis.closeEntry();
            }
        }

        return files;
    }

    // 判断是否是文本文件
    private boolean isTextFile(String fileName) {
        return fileName != null && fileName.matches(".*\\.(md|markdown|json|js|ts|java|py|vue|jsx?|tsx?|xml|html|css|scss|yml|yaml|properties|txt|mdx)$");
    }

    // ==================== 预留接口 ====================
    // 以下接口当前未被前端直接使用，预留用于后续功能扩展

    /**
     * 更新项目（按ID）
     */
    @PutMapping("update/{id}")
    public ResponseVO updateById(@PathVariable Integer id, @RequestBody Project project) {
        projectService.updateProjectById(project, id);
        return getSuccessResponseVO(null);
    }

    /**
     * 根据项目名查询
     */
    @GetMapping("getByName")
    public ResponseVO getByName(@RequestParam String name) {
        Project project = projectService.getProjectByName(name);
        return getSuccessResponseVO(project);
    }

    /**
     * 根据项目名更新
     */
    @PutMapping("updateByName")
    public ResponseVO updateByName(@RequestParam String name, @RequestBody Project project) {
        projectService.updateProjectByName(project, name);
        return getSuccessResponseVO(null);
    }

    /**
     * 根据项目名删除
     */
    @DeleteMapping("/deleteByName")
    public ResponseVO deleteByName(@RequestParam String name) {
        projectService.deleteProjectByName(name);
        return getSuccessResponseVO(null);
    }

    /**
     * 批量新增项目
     */
    @RequestMapping("addBatch")
    public ResponseVO addBatch(@RequestBody List<Project> projectList) {
        projectService.addBatch(projectList);
        return getSuccessResponseVO(null);
    }

    /**
     * 批量新增或修改项目
     */
    @RequestMapping("addOrUpdateBatch")
    public ResponseVO addOrUpdateBatch(@RequestBody List<Project> projectList) {
        projectService.addOrUpdateBatch(projectList);
        return getSuccessResponseVO(null);
    }

    /**
     * 根据ID查询项目
     */
    @RequestMapping("getProjectById")
    public ResponseVO getProjectById(Integer id) {
        return getSuccessResponseVO(projectService.getProjectById(id));
    }

    /**
     * 根据ID更新项目
     */
    @RequestMapping("updateProjectById")
    public ResponseVO updateProjectById(Project project, Integer id) {
        projectService.updateProjectById(project, id);
        return getSuccessResponseVO(null);
    }

    /**
     * 根据ID删除项目
     */
    @RequestMapping("deleteProjectById")
    public ResponseVO deleteProjectById(Integer id) {
        projectService.deleteProjectById(id);
        return getSuccessResponseVO(null);
    }

    /**
     * 根据名称查询项目
     */
    @RequestMapping("getProjectByName")
    public ResponseVO getProjectByName(String name) {
        return getSuccessResponseVO(projectService.getProjectByName(name));
    }

    /**
     * 根据名称更新项目
     */
    @RequestMapping("updateProjectByName")
    public ResponseVO updateProjectByName(Project project, String name) {
        projectService.updateProjectByName(project, name);
        return getSuccessResponseVO(null);
    }

    /**
     * 根据名称删除项目
     */
    @RequestMapping("deleteProjectByName")
    public ResponseVO deleteProjectByName(String name) {
        projectService.deleteProjectByName(name);
        return getSuccessResponseVO(null);
    }
}
