package com.lsy.spring.boot.blog.service;

import com.lsy.spring.boot.blog.domain.Catalog;
import com.lsy.spring.boot.blog.domain.User;

import java.util.List;

/**
 * Catalog Service 服务接口
 * @Author: yoo
 * @Date: 2019-01-17 13:43
 */
public interface CatalogService {

    /**
     * 保存Catalog
     * @param catalog
     * @return
     */
    Catalog saveCatalog(Catalog catalog);

    /**
     * 删除
     * @param id
     */
    void removeCatalog(Long id);

    /**
     * 根据Id获取Catalog
     * @param id
     * @return
     */
    Catalog getCatalogById(Long id);

    /**
     * 获取Catalog列表
     * @param user
     * @return
     */
    List<Catalog> listCatalogs(User user);
}
