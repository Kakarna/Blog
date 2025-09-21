package com.easy.service.impl;

import com.easy.entity.query.SimplePage;
import com.easy.entity.vo.PaginationResultVO;
import com.easy.entity.po.TechNote;
import com.easy.entity.query.TechNoteQuery;
import com.easy.mapper.TechNoteMappers;
import com.easy.service.TechNoteService;
import com.easy.enums.PageSize;
import com.easy.service.UserService;
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

    @Resource
    private UserService userService;

    /**
     * 统计所有技术笔记数量
     * 用于仪表盘展示
     */
    @Override
    public Integer countAll() {
        return techNoteMappers.countAll();
    }

    /**
     * 统计最近几天的笔记数量
     * 用于仪表盘展示
     */
    @Override
    public Integer countRecent(int days) {
        return techNoteMappers.countRecent(days);
    }

    /**
     * 按分类统计数量（饼图）
     * 用于仪表盘展示
     */
    @Override
    public List<Map<String, Object>> countByCategory() {
        return techNoteMappers.countByCategory();
    }

    /**
     * 按天统计数量（折线图）
     * 用于仪表盘展示
     */
    @Override
    public List<Map<String, Object>> countByDay(int days) {
        return techNoteMappers.countByDay(days);
    }

    /**
     * 根据查询条件获取笔记列表
     * 前端API: getTechNoteList - 用于按分类获取笔记列表
     */
    @Override
    public List<TechNote> findListByParam(TechNoteQuery query) {
        return this.techNoteMappers.selectList(query);
    }

    /**
     * 根据查询条件统计笔记数量
     * 前端API: getTechNoteList - 用于分页查询
     */
    @Override
    public Integer findCountByParam(TechNoteQuery query) {
        return this.techNoteMappers.selectCount(query);
    }

    /**
     * 分页查询笔记列表
     * 前端API: getTechNoteList - 用于分页展示笔记列表
     */
    @Override
    public PaginationResultVO<TechNote> findListByPage(TechNoteQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<TechNote> list = this.findListByParam(query);
        return new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
    }

    /**
     * 添加技术笔记
     * 前端API: addTechNote - 用于添加新的技术笔记
     */
    @Override
    public Integer add(TechNote note) {
        if (note.getUserId() == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return this.techNoteMappers.insertWithUserId(
            note.getSectionId(),
            note.getTitle(),
            note.getContent(),
            note.getUserId()
        );
    }

    /**
     * 根据ID获取笔记详情
     * 前端API: getTechNoteById - 用于获取笔记详情
     */
    @Override
    public TechNote getById(Integer id) {
        return this.techNoteMappers.selectById(id);
    }

    /**
     * 根据ID更新笔记
     * 前端API: updateTechNote - 用于更新已有笔记
     */
    @Override
    public Integer updateById(TechNote note, Integer id) {
        return this.techNoteMappers.updateById(note, id);
    }

    /**
     * 根据ID删除笔记
     * 前端API: deleteTechNoteById - 用于删除笔记
     */
    @Override
    public Integer deleteById(Integer id) {
        return this.techNoteMappers.deleteById(id);
    }

    /**
     * 获取用户私有笔记
     * 前端API: getPrivateNotes - 用于获取当前用户的私有笔记
     */
    @Override
    public PaginationResultVO<TechNote> getPrivateNotes(TechNoteQuery query, Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        List<TechNote> notes = techNoteMappers.findByUserId(userId);
        return new PaginationResultVO<>(notes.size(), query.getPageSize(), query.getPageNo(), 
            (int) Math.ceil((double)notes.size()/query.getPageSize()), notes);
    }

    /**
     * 根据用户ID获取笔记列表
     * 前端API: getNotesByUserId - 兼容旧接口，内部调用getPrivateNotes
     */
    @Override
    public PaginationResultVO<TechNote> getNotesByUserId(TechNoteQuery query) {
        if (query.getUserId() == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return this.findListByPage(query);
    }

    /**
     * 获取公共笔记
     * 用于展示公共账号的笔记
     */
    @Override
    public PaginationResultVO<TechNote> getPublicNotes(TechNoteQuery query) {
        query.setUserId(userService.getPublicUser().getId());
        return this.findListByPage(query);
    }

    // ==================== 预留接口 ====================
    // 以下接口当前未被前端直接使用，预留用于后续功能扩展

    /**
     * 批量添加笔记
     */
    @Override
    public Integer addBatch(List<TechNote> noteList) {
        if (noteList == null || noteList.isEmpty()) {
            return 0;
        }
        return this.techNoteMappers.insertBatch(noteList);
    }

    /**
     * 批量添加或更新笔记
     */
    @Override
    public Integer addOrUpdateBatch(List<TechNote> noteList) {
        if (noteList == null || noteList.isEmpty()) {
            return 0;
        }
        return this.techNoteMappers.insertOrUpdateBatch(noteList);
    }

    /**
     * 根据标题获取笔记
     */
    @Override
    public TechNote getByTitle(String title) {
        return this.techNoteMappers.selectByTitle(title);
    }

    /**
     * 根据标题更新笔记
     */
    @Override
    public Integer updateByTitle(TechNote note, String title) {
        return this.techNoteMappers.updateByTitle(note, title);
    }

    /**
     * 根据标题删除笔记
     */
    @Override
    public Integer deleteByTitle(String title) {
        return this.techNoteMappers.deleteByTitle(title);
    }

    /**
     * 根据用户ID获取笔记列表（非分页）
     */
    @Override
    public List<TechNote> getNotesByUserId(Integer userId) {
        TechNoteQuery query = new TechNoteQuery();
        query.setUserId(userId);
        return this.techNoteMappers.selectList(query);
    }
}
