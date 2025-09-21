package com.easy.mapper;

import com.easy.mapper.BaseMapper;
import com.easy.entity.po.StudyRecord;
import com.easy.entity.query.StudyRecordQuery;

import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @Description: 学习记录表 Mapper接口
 * @date: 2025/08/04
 * @Author: Sena
 */
public interface StudyRecordMappers<T, P> extends BaseMapper {

    /**
     * 根据Id查询
     * 调用方: StudyRecordServiceImpl
     */
    T selectById(@Param("id") Integer id);

    // ==================== 预留接口 ====================
    // 以下接口当前未被使用

    /**
     * 根据Id更新
     * 【预留接口】
     */
    Integer updateById(@Param("bean") T t, @Param("id") Integer id);

    /**
     * 根据Id删除
     * 【预留接口】
     */
    Integer deleteById(@Param("id") Integer id);

    /**
     * 根据用户ID查询学习记录列表
     * 调用方: StudyRecordServiceImpl
     */
    List<T> selectByUserId(@Param("userId") Integer userId);




}
