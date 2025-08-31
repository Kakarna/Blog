package com.easy.service;

import com.easy.entity.vo.PaginationResultVO;
import com.easy.entity.po.TechNote;
import com.easy.entity.query.TechNoteQuery;

import java.util.List;

/**
 * @Description: 技术笔记表 Service
 * @date: 2025/08/02
 * @Author: Sena
 */
public interface TechNoteService {

    /**
     * 根据条件查询列表
     */
    List<TechNote> findListByParam(TechNoteQuery query);

    /**
     * 根据条件查询数量
     */
    Integer findCountByParam(TechNoteQuery query);

    /**
     * 分页查询
     */
    PaginationResultVO<TechNote> findListByPage(TechNoteQuery query);

    /**
     * 新增
     */
    Integer add(TechNote note);

    /**
     * 批量新增
     */
    Integer addBatch(List<TechNote> noteList);

    /**
     * 批量新增或修改
     */
    Integer addOrUpdateBatch(List<TechNote> noteList);

    /**
     * 根据Id查询
     */
    TechNote getById(Integer id);

    /**
     * 根据Id更新
     */
    Integer updateById(TechNote note, Integer id);

    /**
     * 根据Id删除
     */
    Integer deleteById(Integer id);

    /**
     * 根据标题查询
     */
    TechNote getByTitle(String title);

    /**
     * 根据标题更新
     */
    Integer updateByTitle(TechNote note, String title);

    /**
     * 根据标题删除
     */
    Integer deleteByTitle(String title);
}
