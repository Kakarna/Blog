package com.easy.controller;

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

    @GetMapping("/countAll")
    public ResponseVO countAll() {
        return ResponseVO.ok(techNoteService.countAll());
    }

    //添加笔记
    @PostMapping("add")
    public ResponseVO<TechNote> add(@RequestBody TechNoteAddVO addVO, HttpServletRequest request, HttpServletResponse response) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null || !"admin".equals(claims.get("role"))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseVO.error("无权限访问");
        }
        try {
            // 1. 通过 sectionName 查询 sectionId
            TechSection section = techSectionService.getByName(addVO.getSectionName());
            if (section == null) {
                return ResponseVO.error("未找到对应的分区: " + addVO.getSectionName());
            }

            // 2. 构造 TechNote 对象
            TechNote techNote = new TechNote();
            techNote.setSectionId(section.getId());
            techNote.setTitle(addVO.getTitle());
            techNote.setContent(addVO.getContent());

            // 3. 保存
            techNoteService.add(techNote);
            return ResponseVO.ok(techNote);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.error("添加笔记失败: " + e.getMessage());
        }
    }

    //分页查询
    @RequestMapping("loadDataListBySection")
    public ResponseVO loadDataList(@RequestBody TechNoteVO queryVO) {
        // 先转换成业务查询对象
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

        // 调用业务层查询
        return getSuccessResponseVO(techNoteService.findListByPage(query));
    }
    //根据笔记id查询笔记完整内容
    @GetMapping("getById")
    public ResponseVO<TechNoteVO> getById(@RequestParam("id") Integer id) {
        TechNote note = techNoteService.getById(id);
        if (note == null) {
            return ResponseVO.error("笔记不存在");
        }
        // 转换为 VO 对象
        TechNoteVO vo = new TechNoteVO();
        vo.setTitle(note.getTitle());
        vo.setContent(note.getContent());
        vo.setSectionId(note.getSectionId());
        vo.setCreatedTime(LocalDateTime.now());
        vo.setUpdatedTime(LocalDateTime.now());
        // 其他字段依次设置...
        return ResponseVO.ok(vo);

    }

    //修改笔记
    @PostMapping("update")
    public ResponseVO update(@RequestBody TechNote updateData, HttpServletRequest request, HttpServletResponse response) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null || !"admin".equals(claims.get("role"))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseVO.error("无权限访问");
        }
        Integer id = updateData.getId();
        if (id == null) {
            return ResponseVO.error("<UNK>");
        }
        techNoteService.updateById(updateData, id);
        return ResponseVO.ok("修改成功");
    }

    //删除笔记
    @PostMapping("delete")
    public ResponseVO deleteById(@RequestBody Map<String, Integer> param, HttpServletRequest request, HttpServletResponse response) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null || !"admin".equals(claims.get("role"))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseVO.error("无权限访问");
        }
        Integer id = param.get("id");
        techNoteService.deleteById(id);
        return ResponseVO.ok("删除成功");
    }



}
