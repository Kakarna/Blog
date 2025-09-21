package com.easy.service;

import com.easy.entity.vo.PaginationResultVO;
import com.easy.entity.po.TechNote;
import com.easy.entity.query.TechNoteQuery;

import java.util.List;
import java.util.Map;

/**
 * @Description: 技术笔记表 Service
 * @date: 2025/08/02
 * @Author: Sena
 */
public interface TechNoteService {

    /**
     * 查询笔记总数
     * @return 数量
     */
    Integer countAll();


    /**
     * 统计最近 N 天内更新的笔记数量
     */
    Integer countRecent(int days);


    /**
     * 统计不同分区的笔记数量
     */
    List<Map<String, Object>> countByCategory();

    List<Map<String, Object>> countByDay(int days);


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

    /**
     * 根据用户ID获取所有笔记
     * @param userId 用户ID
     * @return 笔记列表
     */
    List<TechNote> getNotesByUserId(Integer userId);

    /**
     * 获取公共笔记（分页）
     * @param query 查询条件
     * @return 分页结果
     */
    PaginationResultVO<TechNote> getPublicNotes(TechNoteQuery query);

    /**
     * 根据用户ID分页获取笔记
     * @param query 查询条件（包含分页参数和用户ID）
     * @return 分页结果
     */
    PaginationResultVO<TechNote> getNotesByUserId(TechNoteQuery query);

    /**
     * 根据用户ID获取笔记（分页）
     * @param query 查询条件
     * @param userId 用户ID
     * @return 分页结果
     */
    PaginationResultVO<TechNote> getPrivateNotes(TechNoteQuery query, Integer userId);
}
