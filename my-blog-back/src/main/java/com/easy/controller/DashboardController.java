package com.easy.controller;

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
    private VisitService visitService; // 如果访问量存 Redis

    

    @GetMapping("/stats")
    public ResponseVO getStats() {
        Integer articleCount = techNoteService.countAll();
        Integer projectCount = projectService.countAll();
        Integer recentUpdates = techNoteService.countRecent(7); // 最近7天
        Integer visits = visitService.getVisits();

        Map<String,Integer> stats = new HashMap<>();
        stats.put("articleCount", articleCount);
        stats.put("projectCount", projectCount);
        stats.put("recentUpdates", recentUpdates);
        stats.put("visits", visits);

        return ResponseVO.ok(stats);
    }

    @GetMapping("/noteCategoryStats")
    public ResponseVO getNoteCategoryStats() {
        List<Map<String, Object>> stats = techNoteService.countByCategory();
        return ResponseVO.ok(stats);
    }

    @GetMapping("/noteTrend")
    public ResponseVO getNoteTrend() {
        List<Map<String, Object>> trend = techNoteService.countByDay(7); // 最近 7 天
        return ResponseVO.ok(trend);
    }


    @GetMapping("/recentActivities")
    public ResponseVO getRecentActivities() {
        TechNoteQuery query = new TechNoteQuery();
        query.setOrderBy("updated_time desc"); // 按更新时间倒序
        query.setPageNo(1);                   // 第 1 页
        query.setPageSize(5);                 // 每页 5 条

        PaginationResultVO<TechNote> pageResult = techNoteService.findListByPage(query);
        List<TechNote> recentNotes = pageResult.getList(); // 取分页后的列表

        return ResponseVO.ok(recentNotes);
    }

}
