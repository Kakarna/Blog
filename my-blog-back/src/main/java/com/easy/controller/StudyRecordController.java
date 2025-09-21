package com.easy.controller;

import com.easy.entity.po.StudyRecord;
import com.easy.entity.po.User;
import com.easy.entity.query.StudyRecordQuery;
import com.easy.entity.vo.ResponseVO;
import com.easy.entity.vo.StudyRecordVO;
import com.easy.service.StudyRecordService;
import com.easy.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/studyRecord")
public class StudyRecordController extends ABaseController {

    @Autowired
    private StudyRecordService studyRecordService;
    @Autowired
    private UserService userService;

    /**
     * 加载公共学习记录
     * 前端API: fetchPublicStudyRecords
     */
    @PostMapping("loadPublicDataList")
    public ResponseVO loadPublicDataList(@RequestBody StudyRecordVO queryVO) {
        StudyRecordQuery query = new StudyRecordQuery();
        query.setPageNo(queryVO.getPageNo());
        query.setPageSize(queryVO.getPageSize());
        query.setSortField(queryVO.getSortField());
        query.setSortOrder(queryVO.getSortOrder());

        User publicUser = userService.getPublicUser();
        if (publicUser != null) {
            query.setUserId(publicUser.getId());
        }

        return getSuccessResponseVO(studyRecordService.findListByPage(query));
    }

    /**
     * 加载私人学习记录
     * 前端API: fetchPrivateStudyRecords
     */
    @PostMapping("loadPrivateDataList")
    public ResponseVO loadPrivateDataList(@RequestBody StudyRecordVO queryVO,
                                          HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null) {
            return ResponseVO.error("未登录或身份验证失败");
        }

        Integer userId = (Integer) claims.get("id");
        StudyRecordQuery query = new StudyRecordQuery();
        query.setPageNo(queryVO.getPageNo());
        query.setPageSize(queryVO.getPageSize());
        query.setSortField(queryVO.getSortField());
        query.setSortOrder(queryVO.getSortOrder());
        query.setUserId(userId);

        return getSuccessResponseVO(studyRecordService.findListByPage(query));
    }

    /**
     * 添加学习记录
     * 前端API: addStudyRecord
     */
    @PostMapping("add")
    public ResponseVO<?> add(@RequestBody StudyRecordVO addVO, HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null) {
            return ResponseVO.error("请登录");
        }

        Integer userId = (Integer) claims.get("id");
        StudyRecord record = new StudyRecord();
        record.setUserId(userId);
        record.setTitle(addVO.getTitle());
        record.setContent(addVO.getContent());
        record.setStudyDate(addVO.getStudyDate());

        studyRecordService.add(record);
        return ResponseVO.ok("添加成功");
    }

    /**
     * 更新学习记录
     * 前端API: updateStudyRecord
     */
    @PostMapping("update")
    public ResponseVO update(@RequestBody StudyRecord updateData, HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null) {
            return ResponseVO.error("请登录");
        }

        Integer userId = (Integer) claims.get("id");
        String role = (String) claims.get("role");

        StudyRecord existing = studyRecordService.getStudyRecordById(updateData.getId());
        if (existing == null) return ResponseVO.error("记录不存在");

        if (!"admin".equals(role) && !existing.getUserId().equals(userId)) {
            return ResponseVO.error("无权限修改他人记录");
        }

        studyRecordService.updateStudyRecordById(updateData, updateData.getId());
        return ResponseVO.ok("修改成功");
    }

    /**
     * 删除学习记录
     * 前端API: deleteStudyRecord
     */
    @PostMapping("delete")
    public ResponseVO deleteById(@RequestBody Map<String, Integer> param, HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null) {
            return ResponseVO.error("请登录");
        }

        Integer userId = (Integer) claims.get("id");
        String role = (String) claims.get("role");

        Integer id = param.get("id");
        StudyRecord existing = studyRecordService.getStudyRecordById(id);
        if (existing == null) return ResponseVO.error("记录不存在");

        if (!"admin".equals(role) && !existing.getUserId().equals(userId)) {
            return ResponseVO.error("无权限删除他人记录");
        }

        studyRecordService.deleteStudyRecordById(id);
        return ResponseVO.ok("删除成功");
    }
}


