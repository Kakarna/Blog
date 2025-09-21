package com.easy.service;

import com.easy.entity.vo.PaginationResultVO;
import com.easy.entity.po.TechSection;
import com.easy.entity.query.TechSectionQuery;

import java.util.List;

/**
 * @Description: 技术分区表 Service
 * @date: 2025/08/01
 * @Author: Sena
 */
public interface TechSectionService {

    /**
     * 目前用到的
     */

    // 根据用户ID获取所有分区（含预留笔记列表填充）
    List<TechSection> findByUserId(Integer userId);


    Integer add(TechSection section);

    Integer updateById(TechSection section, Integer id);

    Integer deleteById(Integer id);


    /**
     * 预留
     */

    List<TechSection> findListByParam(TechSectionQuery query);

    Integer findCountByParam(TechSectionQuery query);

    PaginationResultVO<TechSection> findListByPage(TechSectionQuery query);

    Integer addBatch(List<TechSection> sectionList);

    Integer addOrUpdateBatch(List<TechSection> sectionList);

    TechSection getById(Integer id);

    TechSection getByName(String name);

    Integer updateByName(TechSection section, String name);

    Integer deleteByName(String name);

}
