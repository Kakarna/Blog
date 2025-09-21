package com.easy.mapper;

import com.easy.entity.po.EmailCode;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

public interface EmailCodeMappers {

    EmailCode selectByEmail(@Param("email") String email);

    void insert(EmailCode record);

    void update(EmailCode record);

    void deleteByEmail(@Param("email") String email);

    Integer countSendToday(@Param("today") LocalDate today);
}

