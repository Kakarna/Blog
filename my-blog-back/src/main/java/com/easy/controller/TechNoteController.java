package com.easy.controller;

/**
 * 技术笔记管理控制器
 * 提供笔记的CRUD操作
 * 包含公共笔记和私人笔记管理接口
 * 注意：私人笔记操作需要登录认证
 */
import com.easy.entity.po.TechNote;
import com.easy.entity.po.TechSection;
import com.easy.entity.query.TechNoteQuery;
import com.easy.entity.vo.ResponseVO;
import com.easy.entity.vo.TechNoteAddVO;
import com.easy.entity.vo.TechNoteVO;
import com.easy.service.TechNoteService;
import com.easy.service.TechSectionService;
import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Description: 技术笔记 Controller
 * @date: 2025/08/02
 * @Author: Sena
 */
@RestController
@RequestMapping("/techNote")
public class TechNoteController extends ABaseController {

    @Resource
    private TechNoteService techNoteService;
    @Resource
    private TechSectionService techSectionService;


    /**
     * 备注: 目前使用到的接口
     */

    /**
     * 统计所有笔记数量
     * 前端API: 用于仪表板展示
     */
    @GetMapping("/countAll")
    public ResponseVO countAll() {
        return ResponseVO.ok(techNoteService.countAll());
    }
    /**
     * 获取私人笔记
     * 前端API: getPrivateNotes - 用于获取用户私人笔记
     */
    @GetMapping("getPrivateNotes")
    public ResponseVO getPrivateNotes(TechNoteVO queryVO) {

        if (queryVO.getUserId() == null) {
            return ResponseVO.error("用户ID不能为空");
        }
        
        TechNoteQuery query = new TechNoteQuery();
        query.setPageNo(queryVO.getPageNo());
        query.setPageSize(queryVO.getPageSize());
        query.setUserId(queryVO.getUserId());
        
        return getSuccessResponseVO(techNoteService.getPrivateNotes(query, queryVO.getUserId()));
    }
    /**
     * 获取公共笔记
     * 前端API: getTechNoteList - 用于获取公共笔记列表
     */
    @GetMapping("getPublicNotes")
    public ResponseVO getPublicNotes(TechNoteVO queryVO) {
        TechNoteQuery query = new TechNoteQuery();
        query.setPageNo(queryVO.getPageNo());
        query.setPageSize(queryVO.getPageSize());
        return getSuccessResponseVO(techNoteService.getPublicNotes(query));
    }


    /**
     * 根据分区加载笔记列表
     * 前端API: getTechNoteList - 用于按分区获取笔记列表
     */
    @RequestMapping("loadDataListBySection")
    public ResponseVO loadDataList(@RequestBody TechNoteVO queryVO) {
        TechNoteQuery query = new TechNoteQuery();
        query.setPageNo(queryVO.getPageNo());
        query.setPageSize(queryVO.getPageSize());

        if (queryVO.getSectionName() != null && !queryVO.getSectionName().isEmpty()) {
            TechSection section = techSectionService.getByName(queryVO.getSectionName());
            if (section == null) {
                return ResponseVO.error("未找到对应分区: " + queryVO.getSectionName());
            }
            query.setSectionId(section.getId());
        } else if (queryVO.getSectionId() != null) {
            query.setSectionId(queryVO.getSectionId());
        }

        return getSuccessResponseVO(techNoteService.findListByPage(query));
    }
    /**
     * 根据ID获取笔记详情
     * 前端API: getTechNoteById - 用于获取笔记详情
     */
    @GetMapping("getById")
    public ResponseVO<TechNoteVO> getById(@RequestParam("id") Integer id) {
        TechNote note = techNoteService.getById(id);
        if (note == null) {
            return ResponseVO.error("笔记不存在");
        }
        
        TechNoteVO vo = new TechNoteVO();
        vo.setTitle(note.getTitle());
        vo.setContent(note.getContent());
        vo.setSectionId(note.getSectionId());
        vo.setCreatedTime(LocalDateTime.now());
        vo.setUpdatedTime(LocalDateTime.now());
        return ResponseVO.ok(vo);
    }

    /**
     * 添加笔记
     * 前端API: addTechNote - 用于添加新笔记
     */
    @PostMapping("add")
    public ResponseVO<TechNote> add(@RequestBody TechNoteAddVO addVO,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseVO.error("未登录或身份验证失败");
        }

        String role = (String) claims.get("role");
        Integer userId = (Integer) claims.get("id");

        try {
            TechSection section = techSectionService.getByName(addVO.getSectionName());
            if (section == null) {
                return ResponseVO.error("未找到对应的分区: " + addVO.getSectionName());
            }

            if (!"admin".equals(role) && !section.getUserId().equals(userId)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return ResponseVO.error("无权限操作该分区的笔记");
            }

            TechNote techNote = new TechNote();
            techNote.setSectionId(section.getId());
            if (addVO.getUserId() == null) {
                return ResponseVO.error("用户ID不能为空");
            }
            techNote.setUserId(addVO.getUserId());
            techNote.setTitle(addVO.getTitle());
            techNote.setContent(addVO.getContent());

            techNoteService.add(techNote);
            return ResponseVO.ok(techNote);

        } catch (Exception e) {
            return ResponseVO.error("添加笔记失败: " + e.getMessage());
        }
    }

    /**
     * 更新笔记
     * 前端API: updateTechNote - 用于更新已有笔记
     */
    @PostMapping("update")
    public ResponseVO update(@RequestBody TechNote updateData,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseVO.error("未登录或身份验证失败");
        }

        String role = (String) claims.get("role");
        Integer userId = (Integer) claims.get("id");

        Integer id = updateData.getId();
        if (id == null) return ResponseVO.error("缺少笔记ID");

        TechNote note = techNoteService.getById(id);
        if (note == null) return ResponseVO.error("笔记不存在");

        TechSection section = techSectionService.getById(note.getSectionId());

        if (!"admin".equals(role) && !section.getUserId().equals(userId)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseVO.error("无权限修改该笔记");
        }

        techNoteService.updateById(updateData, id);
        return ResponseVO.ok("修改成功");
    }

    /**
     * 删除笔记
     * 前端API: deleteTechNoteById - 用于删除笔记
     */
    @PostMapping("delete")
    public ResponseVO deleteById(@RequestBody Map<String, Integer> param,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseVO.error("未登录或身份验证失败");
        }

        String role = (String) claims.get("role");
        Integer userId = (Integer) claims.get("id");

        Integer id = param.get("id");
        if (id == null) return ResponseVO.error("缺少笔记ID");

        TechNote note = techNoteService.getById(id);
        if (note == null) return ResponseVO.error("笔记不存在");

        TechSection section = techSectionService.getById(note.getSectionId());

        if (!"admin".equals(role) && !section.getUserId().equals(userId)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseVO.error("无权限删除该笔记");
        }

        techNoteService.deleteById(id);
        return ResponseVO.ok("删除成功");
    }

}
