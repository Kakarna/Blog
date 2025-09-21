package com.easy.controller;

/**
 * 仪表板数据控制器
 * 提供仪表板所需的各种统计数据接口
 * 所有接口均为只读，无需认证
 */
import com.easy.entity.po.TechNote;
import com.easy.entity.query.TechNoteQuery;
import com.easy.entity.vo.PaginationResultVO;
import com.easy.entity.vo.ResponseVO;
import com.easy.service.ProjectService;
import com.easy.service.TechNoteService;
import com.easy.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private TechNoteService techNoteService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private VisitService visitService;

    /**
     * 获取仪表板统计数据
     * 前端API: getDashboardStats
     */
    @GetMapping("/stats")
    public ResponseVO getStats() {
        Integer articleCount = techNoteService.countAll();
        Integer projectCount = projectService.countAll();
        Integer recentUpdates = techNoteService.countRecent(7);
        Integer visits = visitService.getVisits();

        Map<String,Integer> stats = new HashMap<>();
        stats.put("articleCount", articleCount);
        stats.put("projectCount", projectCount);
        stats.put("recentUpdates", recentUpdates);
        stats.put("visits", visits);

        return ResponseVO.ok(stats);
    }

    /**
     * 获取笔记分类统计
     * 前端API: getNoteCategoryStats
     */
    @GetMapping("/noteCategoryStats")
    public ResponseVO getNoteCategoryStats() {
        List<Map<String, Object>> stats = techNoteService.countByCategory();
        return ResponseVO.ok(stats);
    }

    /**
     * 获取笔记趋势数据
     * 前端API: getNoteTrend
     */
    @GetMapping("/noteTrend")
    public ResponseVO getNoteTrend() {
        List<Map<String, Object>> trend = techNoteService.countByDay(7);
        return ResponseVO.ok(trend);
    }

    /**
     * 获取最近活动
     * 前端API: getRecentActivities
     */
    @GetMapping("/recentActivities")
    public ResponseVO getRecentActivities() {
        TechNoteQuery query = new TechNoteQuery();
        query.setOrderBy("updated_time desc");
        query.setPageNo(1);
        query.setPageSize(5);

        PaginationResultVO<TechNote> pageResult = techNoteService.findListByPage(query);
        List<TechNote> recentNotes = pageResult.getList();

        return ResponseVO.ok(recentNotes);
    }
}
