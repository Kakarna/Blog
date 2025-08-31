package com.easy.entity.query;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseQuery {
    private SimplePage simplePage;
    private Integer pageNo;
    private Integer pageSize;
    private String orderBy;

}
