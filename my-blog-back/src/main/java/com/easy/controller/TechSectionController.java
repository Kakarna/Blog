package com.easy.controller;

import com.easy.entity.po.TechNote;
import com.easy.entity.po.TechSection;
import com.easy.entity.po.User;
import com.easy.entity.query.TechSectionQuery;
import com.easy.entity.vo.ResponseVO;
import com.easy.service.TechNoteService;
import com.easy.service.TechSectionService;
import com.easy.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description: 技术分区表 Controller
 * @date: 2025/08/01
 * @Author: Sena
 */
@RestController
@RequestMapping("/techSection")
public class TechSectionController extends ABaseController {

    @Resource
    private TechSectionService techSectionService;

    @Resource
    private UserService userService;

    @Resource
    private TechNoteService techNoteService;
    /**
     * 备注: 目前使用到的接口
     */

    /**
     * 获取公共分区
     * 前端API: getPublicSections - 用于获取公共分区列表
     * 被DashboardController间接调用
     */
    @GetMapping("publicUser")
    public ResponseVO<List<TechSection>> getPublicSections() {
        User publicUser = userService.getPublicUser();
        if (publicUser == null) {
            return ResponseVO.error("暂无公共账号数据");
        }

        List<TechSection> sectionList = techSectionService.findByUserId(publicUser.getId());
        return ResponseVO.ok(sectionList);
    }

    /**
     * 获取私人分区
     * 前端API: getPrivateSections - 用于获取用户私人分区列表
     * 被DashboardController间接调用
     */
    @GetMapping("privateUser")
    public ResponseVO<List<TechSection>> privateUser(HttpServletRequest request, HttpServletResponse response) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ResponseVO.error("未登录或token无效");
        }
        Integer userId = (Integer) claims.get("id");
        List<TechSection> sectionList = techSectionService.findByUserId(userId);
        return ResponseVO.ok(sectionList);
    }


    /**
     * 添加分区
     * 前端API: addSection - 用于添加新分区
     * 被TechNoteController间接调用
     */
    @PostMapping("add")
    public ResponseVO<String> add(@RequestBody TechSection techSection,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseVO.error("未登录或身份验证失败");
        }

        String role = (String) claims.get("role");
        Integer userId = (Integer) claims.get("id");

        if ("admin".equals(role)) {
            if (techSection.getUserId() == null) {
                techSection.setUserId(userId);
            }
        } else if ("user".equals(role)) {
            techSection.setUserId(userId);
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseVO.error("无权限访问");
        }

        if (techSection.getName() == null || techSection.getName().trim().isEmpty()) {
            return ResponseVO.error("分区名称不能为空");
        }

        techSectionService.add(techSection);
        return ResponseVO.ok("添加成功");
    }



    /**
     * 更新分区
     * 前端API: updateSection - 用于更新分区信息
     * 被TechNoteController间接调用
     */
    @PutMapping("update/{id}")
    public ResponseVO<String> update(@PathVariable Integer id, @RequestBody TechSection techSection,
                                     HttpServletRequest request, HttpServletResponse response) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseVO.error("未登录或身份验证失败");
        }

        String role = (String) claims.get("role");
        Integer userId = (Integer) claims.get("id");

        TechSection existing = techSectionService.getById(id);
        if (existing == null) {
            return ResponseVO.error("分区不存在");
        }

        if (!"admin".equals(role) && !userId.equals(existing.getUserId())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseVO.error("无权限访问");
        }

        techSectionService.updateById(techSection, id);
        return ResponseVO.ok("修改成功");
    }

    /**
     * 删除分区
     * 前端API: deleteSection - 用于删除分区
     * 被TechNoteController间接调用
     */
    @DeleteMapping("delete/{id}")
    public ResponseVO<String> delete(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseVO.error("未登录或身份验证失败");
        }

        String role = (String) claims.get("role");
        Integer userId = (Integer) claims.get("id");

        TechSection existing = techSectionService.getById(id);
        if (existing == null) {
            return ResponseVO.error("分区不存在");
        }

        if (!"admin".equals(role) && !userId.equals(existing.getUserId())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseVO.error("无权限访问");
        }

        techSectionService.deleteById(id);
        return ResponseVO.ok("删除成功");
    }





    // ==================== 预留接口 ====================
    // 以下接口当前未被前端直接或间接使用，预留用于后续功能扩展

    /**
     * 批量新增分区
     * 【预留接口】
     */
    @PostMapping("/addBatch")
    public ResponseVO<Void> addBatch(@RequestBody List<TechSection> sectionList) {
        techSectionService.addBatch(sectionList);
        return getSuccessResponseVO(null);
    }

    /**
     * 批量新增或修改分区
     * 【预留接口】
     */
    @PostMapping("/addOrUpdateBatch")
    public ResponseVO<Void> addOrUpdateBatch(@RequestBody List<TechSection> sectionList) {
        techSectionService.addOrUpdateBatch(sectionList);
        return getSuccessResponseVO(null);
    }

    /**
     * 根据Id查询分区
     * 【预留接口】
     */
    @GetMapping("/getById")
    public ResponseVO<TechSection> getById(@RequestParam Integer id) {
        return getSuccessResponseVO(techSectionService.getById(id));
    }

    /**
     * 根据名称查询分区
     * 【预留接口】
     */
    @GetMapping("/getByName")
    public ResponseVO<TechSection> getByName(@RequestParam String name) {
        return getSuccessResponseVO(techSectionService.getByName(name));
    }

    /**
     * 根据名称更新分区
     * 【预留接口】
     */
    @PostMapping("/updateByName")
    public ResponseVO<Void> updateByName(@RequestBody TechSection section, @RequestParam String name) {
        techSectionService.updateByName(section, name);
        return getSuccessResponseVO(null);
    }

    /**
     * 根据名称删除分区
     * 【预留接口】
     */
    @DeleteMapping("/deleteByName")
    public ResponseVO<Void> deleteByName(@RequestParam String name) {
        techSectionService.deleteByName(name);
        return getSuccessResponseVO(null);
    }
}
