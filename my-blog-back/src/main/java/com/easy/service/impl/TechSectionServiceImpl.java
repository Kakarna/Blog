package com.easy.service.impl;

import com.easy.entity.query.SimplePage;
import com.easy.entity.vo.PaginationResultVO;
import com.easy.entity.po.TechSection;
import com.easy.entity.query.TechSectionQuery;
import com.easy.mapper.TechSectionMappers;
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
    private TechSectionMappers<TechSection, TechSectionQuery> techSectionMappers;


    @Override
    public List<TechSection> findListByParam(TechSectionQuery query) {
        return this.techSectionMappers.selectList(query);
    }

    @Override
    public Integer findCountByParam(TechSectionQuery query) {
        return this.techSectionMappers.selectCount(query);
    }

    @Override
    public PaginationResultVO<TechSection> findListByPage(TechSectionQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<TechSection> list = this.findListByParam(query);
        return new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
    }

    @Override
    public Integer add(TechSection section) {
        return this.techSectionMappers.insert(section);
    }

    @Override
    public Integer addBatch(List<TechSection> sectionList) {
        if (sectionList == null || sectionList.isEmpty()) {
            return 0;
        }
        return this.techSectionMappers.insertBatch(sectionList);
    }

    @Override
    public Integer addOrUpdateBatch(List<TechSection> sectionList) {
        if (sectionList == null || sectionList.isEmpty()) {
            return 0;
        }
        return this.techSectionMappers.insertOrUpdateBatch(sectionList);
    }

    @Override
    public TechSection getById(Integer id) {
        return this.techSectionMappers.selectById(id);
    }

    @Override
    public Integer updateById(TechSection section, Integer id) {
        return this.techSectionMappers.updateById(section, id);
    }

    @Override
    public Integer deleteById(Integer id) {
        return this.techSectionMappers.deleteById(id);
    }

    @Override
    public TechSection getByName(String name) {
        return this.techSectionMappers.selectByName(name);
    }

    @Override
    public Integer updateByName(TechSection section, String name) {
        return this.techSectionMappers.updateByName(section, name);
    }

    @Override
    public Integer deleteByName(String name) {
        return this.techSectionMappers.deleteByName(name);
    }
}
