package com.lsy.spring.boot.blog.vo;

import com.lsy.spring.boot.blog.domain.Catalog;

import java.io.Serializable;

/**
 * Catalog VO
 * 负责前后台传输数据
 * @Author: yoo
 * @Date: 2019-01-17 13:34
 */
public class CatalogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private Catalog catalog;

    public CatalogVO() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }
}
