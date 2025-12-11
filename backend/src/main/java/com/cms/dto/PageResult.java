package com.cms.dto;

import lombok.Data;

import java.util.List;

/**
 * 分页结果类
 * 用于封装分页查询结果
 * @author 张嘉欢
 * @date 2025-11-30
 */
@Data
public class PageResult<T> {
    /**
     * 总记录数
     */
    private long total;
    
    /**
     * 分页数据
     */
    private List<T> data;
    
    /**
     * 当前页码
     */
    private int page;
    
    /**
     * 每页大小
     */
    private int pageSize;
    
    /**
     * 总页数
     */
    private int totalPages;
    
    public PageResult(List<T> data, long total, int page, int pageSize) {
        this.data = data;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) total / pageSize);
    }
}
