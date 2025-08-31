package com.easy.controller;

import com.easy.entity.po.TechSection;
import com.easy.entity.query.TechSectionQuery;
import com.easy.entity.vo.ResponseVO;
import com.easy.service.TechSectionService;
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

    /**
     * 备注: 目前使用到的接口
     */

    //添加分区
    @PostMapping("add")
    public ResponseVO<String> add(@RequestBody TechSection techSection, HttpServletRequest request, HttpServletResponse response) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null || !"admin".equals(claims.get("role"))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseVO.error("无权限访问");
        }
        techSectionService.add(techSection); // 执行添加逻辑
        return ResponseVO.ok("添加成功");
    }


    //更新分区
    @PutMapping("update/{id}")
    public ResponseVO<String> update(@PathVariable Integer id, @RequestBody TechSection
            techSection, HttpServletRequest request, HttpServletResponse response) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null || !"admin".equals(claims.get("role"))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseVO.error("无权限访问");
        }
        techSectionService.updateById(techSection, id);
        return ResponseVO.ok("修改成功");
    }


    // 删除分区
    @DeleteMapping("delete/{id}")
    public ResponseVO<String> delete(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null || !"admin".equals(claims.get("role"))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseVO.error("无权限访问");
        }
        techSectionService.deleteById(id);
        return ResponseVO.ok("删除成功");
    }






    /*
      备注: 预留拓展接口
     */


    /**
     * 分页查询分区列表
     */
    @GetMapping("loadDataList")
    public ResponseVO loadDataList(TechSectionQuery query) {
        return getSuccessResponseVO(techSectionService.findListByPage(query));
    }


    /**
     * 批量新增分区
     */
    @PostMapping("/addBatch")
    public ResponseVO<Void> addBatch(@RequestBody List<TechSection> sectionList) {
        techSectionService.addBatch(sectionList);
        return getSuccessResponseVO(null);
    }

    /**
     * 批量新增或修改分区
     */
    @PostMapping("/addOrUpdateBatch")
    public ResponseVO<Void> addOrUpdateBatch(@RequestBody List<TechSection> sectionList) {
        techSectionService.addOrUpdateBatch(sectionList);
        return getSuccessResponseVO(null);
    }

    /**
     * 根据Id查询分区
     */
    @GetMapping("/getById")
    public ResponseVO<TechSection> getById(@RequestParam Integer id) {
        return getSuccessResponseVO(techSectionService.getById(id));
    }



    /**
     * 根据名称查询分区
     */
    @GetMapping("/getByName")
    public ResponseVO<TechSection> getByName(@RequestParam String name) {
        return getSuccessResponseVO(techSectionService.getByName(name));
    }

    /**
     * 根据名称更新分区
     */
    @PostMapping("/updateByName")
    public ResponseVO<Void> updateByName(@RequestBody TechSection section, @RequestParam String name) {
        techSectionService.updateByName(section, name);
        return getSuccessResponseVO(null);
    }

    /**
     * 根据名称删除分区
     */
    @DeleteMapping("/deleteByName")
    public ResponseVO<Void> deleteByName(@RequestParam String name) {
        techSectionService.deleteByName(name);
        return getSuccessResponseVO(null);
    }
}
