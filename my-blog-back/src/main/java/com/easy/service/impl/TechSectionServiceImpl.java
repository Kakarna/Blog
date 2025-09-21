package com.easy.service.impl;

import com.easy.entity.po.TechNote;
import com.easy.entity.query.SimplePage;
import com.easy.entity.vo.PaginationResultVO;
import com.easy.entity.po.TechSection;
import com.easy.entity.query.TechSectionQuery;
import com.easy.mapper.TechSectionMappers;
import com.easy.service.TechNoteService;
import com.easy.service.TechSectionService;
import com.easy.enums.PageSize;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 技术分区表 Service 实现类
 * @date: 2025/08/01
 * @Author: Sena
 */
@Service("techSectionService")
public class TechSectionServiceImpl implements TechSectionService {

    @Resource
    private TechSectionMappers<TechSection, TechSectionQuery> techSectionMapper;

    @Resource
    private TechNoteService techNoteService;

    /**
     * 根据用户ID查询技术分区
     * 前端API: getPrivateSections, getPublicSections - 用于获取用户的技术分区
     */
    @Override
    public List<TechSection> findByUserId(Integer userId) {
        TechSectionQuery query = new TechSectionQuery();
        query.setUserId(userId);  // 查询指定用户ID的分区
        List<TechSection> sections = techSectionMapper.selectList(query);
        return sections;
    }

    /**
     * 根据查询条件获取分区列表
     * 用于内部查询
     */
    @Override
    public List<TechSection> findListByParam(TechSectionQuery query) {
        return this.techSectionMapper.selectList(query);
    }

    /**
     * 根据查询条件统计分区数量
     * 用于内部查询
     */
    @Override
    public Integer findCountByParam(TechSectionQuery query) {
        return this.techSectionMapper.selectCount(query);
    }

    /**
     * 分页查询分区列表
     * 用于内部查询
     */
    @Override
    public PaginationResultVO<TechSection> findListByPage(TechSectionQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<TechSection> list = this.findListByParam(query);
        return new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
    }

    /**
     * 添加技术分区
     * 前端API: addSection - 用于添加新的技术分区
     */
    @Override
    public Integer add(TechSection section) {
        return this.techSectionMapper.insert(section);
    }

    /**
     * 根据ID获取分区详情
     * 用于编辑前获取分区信息
     */
    @Override
    public TechSection getById(Integer id) {
        return this.techSectionMapper.selectById(id);
    }

    /**
     * 根据ID更新分区
     * 前端API: updateSection - 用于更新已有分区
     */
    @Override
    public Integer updateById(TechSection section, Integer id) {
        return this.techSectionMapper.updateById(section, id);
    }

    /**
     * 根据ID删除分区
     * 前端API: deleteSection - 用于删除分区
     */
    @Override
    public Integer deleteById(Integer id) {
        return this.techSectionMapper.deleteById(id);
    }

    // ==================== 预留接口 ====================
    // 以下接口当前未被前端直接使用，预留用于后续功能扩展

    /**
     * 批量添加分区
     */
    @Override
    public Integer addBatch(List<TechSection> sectionList) {
        if (sectionList == null || sectionList.isEmpty()) {
            return 0;
        }
        return this.techSectionMapper.insertBatch(sectionList);
    }

    /**
     * 批量添加或更新分区
     */
    @Override
    public Integer addOrUpdateBatch(List<TechSection> sectionList) {
        if (sectionList == null || sectionList.isEmpty()) {
            return 0;
        }
        return this.techSectionMapper.insertOrUpdateBatch(sectionList);
    }

    /**
     * 根据名称获取分区
     */
    @Override
    public TechSection getByName(String name) {
        return this.techSectionMapper.selectByName(name);
    }

    /**
     * 根据名称更新分区
     */
    @Override
    public Integer updateByName(TechSection section, String name) {
        return this.techSectionMapper.updateByName(section, name);
    }

    /**
     * 根据名称删除分区
     */
    @Override
    public Integer deleteByName(String name) {
        return this.techSectionMapper.deleteByName(name);
    }
}
