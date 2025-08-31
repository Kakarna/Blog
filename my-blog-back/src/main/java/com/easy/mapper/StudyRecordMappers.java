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
     * @return
     */
    T selectById(@Param("id") Integer id);

    /**
     * 根据Id更新
     * @return
     */
    Integer updateById(@Param("bean") T t, @Param("id") Integer id);

    /**
     * 根据Id删除
     * @return
     */
    Integer deleteById(@Param("id") Integer id);

    /**
     * 根据用户ID查询学习记录列表
     * @param userId 用户ID
     * @return
     */
    List<T> selectByUserId(@Param("userId") Integer userId);




}
