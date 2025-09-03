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
     * 备注: 目前使用到的接口
     */

    //分页查询
    @RequestMapping("loadDataList")
    public ResponseVO loadDataList(@RequestBody StudyRecordVO queryVO) {
        // 先转换成业务查询对象
        StudyRecordQuery query = new StudyRecordQuery();
        query.setPageNo(queryVO.getPageNo());
        query.setPageSize(queryVO.getPageSize());
        query.setSortField(queryVO.getSortField());
        query.setSortOrder(queryVO.getSortOrder());
        return getSuccessResponseVO(studyRecordService.findListByPage(query));
    }

    //添加日记
    @PostMapping("add")
    public ResponseVO<?> add(@RequestBody StudyRecordVO addVO, HttpServletRequest request, HttpServletResponse response) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null || !"admin".equals(claims.get("role"))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseVO.error("无权限访问");
        }

        System.out.println("收到请求：" + addVO); // 加这一行调试
        /*try {*/
        // 1. 校验参数
        if (addVO.getUsername() == null) {
            return ResponseVO.error("缺少用户名");
        }

        // 2. 查询 userId
        User user = userService.getUserByUsername(addVO.getUsername());
        if (user == null) {
            return ResponseVO.error("用户不存在");
        }

        // 3. 构建实体对象
        StudyRecord record = new StudyRecord();
        record.setUserId(user.getId());
        record.setTitle(addVO.getTitle());
        record.setContent(addVO.getContent());
        record.setStudyDate(addVO.getStudyDate());

        // 4. 保存
        studyRecordService.add(record);
        return ResponseVO.ok("添加成功");
      /*  }catch (Exception e) {
            e.printStackTrace();
            String msg = e.getMessage() != null ? e.getMessage() : "未知错误";
            return ResponseVO.error("添加日记失败: " + msg);
        }*/

    }

    //更新日记
    @PostMapping("update")
    public ResponseVO update(@RequestBody StudyRecord updateData, HttpServletRequest request, HttpServletResponse response) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null || !"admin".equals(claims.get("role"))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseVO.error("无权限访问");
        }
        Integer id = updateData.getId();
        if (id == null) {
            return ResponseVO.error("<UNK>");
        }
        studyRecordService.updateStudyRecordById(updateData, id);
        return ResponseVO.ok("修改成功");
    }

    //删除日记
    @PostMapping("delete")
    public ResponseVO deleteById(@RequestBody Map<String, Integer> param, HttpServletRequest request, HttpServletResponse response) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null || !"admin".equals(claims.get("role"))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            return ResponseVO.error("无权限访问");
        }
        Integer id = param.get("id");
        studyRecordService.deleteStudyRecordById(id);
        return ResponseVO.ok("删除成功");
    }

}

