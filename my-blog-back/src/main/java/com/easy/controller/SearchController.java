package com.easy.controller;

import com.easy.entity.query.ProjectQuery;
import com.easy.entity.query.TechNoteQuery;
import com.easy.entity.query.UserQuery;
import com.easy.entity.vo.ResponseVO;
import com.easy.service.ProjectService;
import com.easy.service.TechNoteService;
import com.easy.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Description: 统一搜索接口
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    @Resource
    private UserService userService;
    @Resource
    private TechNoteService techNoteService;
    @Resource
    private ProjectService projectService;

    /**
     * 统一搜索接口
     * 前端API: globalSearch
     */
    @GetMapping("")
    public ResponseVO search(
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "all") String type,
            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {

        if ("user".equalsIgnoreCase(type)) {
            UserQuery query = new UserQuery();
            query.setNicknameFuzzy(keyword);
            query.setPageNo(pageNo);
            query.setPageSize(pageSize);
            return ResponseVO.ok(userService.findListByPage(query));
        } else if ("note".equalsIgnoreCase(type)) {
            TechNoteQuery query = new TechNoteQuery();
            query.setTitleFuzzy(keyword);
            query.setContentFuzzy(keyword);
            query.setPageNo(pageNo);
            query.setPageSize(pageSize);
            return ResponseVO.ok(techNoteService.findListByPage(query));
        } else if ("project".equalsIgnoreCase(type)) {
            ProjectQuery query = new ProjectQuery();
            query.setNameFuzzy(keyword);
            query.setDescriptionFuzzy(keyword);
            query.setReadmeFuzzy(keyword);
            query.setPageNo(pageNo);
            query.setPageSize(pageSize);
            return ResponseVO.ok(projectService.findListByPage(query));
        } else {
            return ResponseVO.error("暂不支持该类型的搜索");
        }
    }
}