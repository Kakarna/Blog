package com.easy.service.impl;

import com.easy.entity.query.SimplePage;
import com.easy.entity.vo.PaginationResultVO;
import com.easy.entity.po.TechNote;
import com.easy.entity.query.TechNoteQuery;
import com.easy.mapper.TechNoteMappers;
import com.easy.service.TechNoteService;
import com.easy.enums.PageSize;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description: 技术笔记表 Service 实现类
 * @date: 2025/08/02
 * @Author: Sena
 */
@Service("techNoteService")
public class TechNoteServiceImpl implements TechNoteService {

    @Resource
    private TechNoteMappers<TechNote, TechNoteQuery> techNoteMappers;

    @Override
    public Integer countAll() {
        return techNoteMappers.countAll();
    }

    @Override
    public Integer countRecent(int days) {
        return techNoteMappers.countRecent(days);
    }

    /**
     * 按分类统计数量（饼图）
     */
    @Override
    public List<Map<String, Object>> countByCategory() {
        return techNoteMappers.countByCategory();
    }

    /**
     * 按天统计数量（折线图）
     */
    @Override
    public List<Map<String, Object>> countByDay(int days) {
        return techNoteMappers.countByDay(days);
    }

    @Override
    public List<TechNote> findListByParam(TechNoteQuery query) {
        return this.techNoteMappers.selectList(query);
    }

    @Override
    public Integer findCountByParam(TechNoteQuery query) {
        return this.techNoteMappers.selectCount(query);
    }

    @Override
    public PaginationResultVO<TechNote> findListByPage(TechNoteQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<TechNote> list = this.findListByParam(query);
        return new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
    }

    @Override
    public Integer add(TechNote note) {
        return this.techNoteMappers.insert(note);
    }

    @Override
    public Integer addBatch(List<TechNote> noteList) {
        if (noteList == null || noteList.isEmpty()) {
            return 0;
        }
        return this.techNoteMappers.insertBatch(noteList);
    }

    @Override
    public Integer addOrUpdateBatch(List<TechNote> noteList) {
        if (noteList == null || noteList.isEmpty()) {
            return 0;
        }
        return this.techNoteMappers.insertOrUpdateBatch(noteList);
    }

    @Override
    public TechNote getById(Integer id) {
        return this.techNoteMappers.selectById(id);
    }

    @Override
    public Integer updateById(TechNote note, Integer id) {
        return this.techNoteMappers.updateById(note, id);
    }

    @Override
    public Integer deleteById(Integer id) {
        return this.techNoteMappers.deleteById(id);
    }

    @Override
    public TechNote getByTitle(String title) {
        return this.techNoteMappers.selectByTitle(title);
    }

    @Override
    public Integer updateByTitle(TechNote note, String title) {
        return this.techNoteMappers.updateByTitle(note, title);
    }

    @Override
    public Integer deleteByTitle(String title) {
        return this.techNoteMappers.deleteByTitle(title);
    }
}
