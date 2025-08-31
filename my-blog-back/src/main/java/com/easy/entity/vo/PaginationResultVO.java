package com.easy.entity.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

import java.util.List;

@Setter
@Getter
public class PaginationResultVO<T> {
    // Getter 和 Setter
    private Integer totalCount;   // 总记录数
    private Integer pageSize;     // 每页大小
    private Integer pageNo;       // 当前页码
    private Integer pageTotal;    // 总页数
    private List<T> list = new ArrayList<>(); // 数据列表


    public PaginationResultVO(Integer totalCount, Integer pageSize, Integer pageNo, List<T> list) {
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.list = list;
        // 自动计算总页数
    }

    public PaginationResultVO(Integer totalCount, Integer pageSize, Integer pageNo,Integer pageTotal, List<T> list) {
        if(pageNo == 0) {
            pageNo = 1;
        }
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.pageTotal = pageTotal;
        this.list = list;
    }

    public PaginationResultVO() {}

}
