package com.seehoo.rent.app.common;

import lombok.Data;

import java.util.List;

/**
 * 统一分页反参：{total, current, size, pages, records}
 */
@Data
public class PageResult<T> {

    private Long total;
    private Long current;
    private Long size;
    private Long pages;
    private List<T> records;

    public static <S, T> PageResult<T> of(com.baomidou.mybatisplus.extension.plugins.pagination.Page<S> page, List<T> records) {
        PageResult<T> r = new PageResult<>();
        r.total = page.getTotal();
        r.current = page.getCurrent();
        r.size = page.getSize();
        r.pages = page.getPages();
        r.records = records;
        return r;
    }
}
