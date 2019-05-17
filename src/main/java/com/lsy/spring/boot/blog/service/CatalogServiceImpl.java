package com.lsy.spring.boot.blog.service;

import com.lsy.spring.boot.blog.domain.Catalog;
import com.lsy.spring.boot.blog.domain.User;
import com.lsy.spring.boot.blog.repository.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Catalog Service实现类.
 * @Author: yoo
 * @Date: 2019-01-17 13:48
 */
@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private CatalogRepository catalogRepository;

    @Override
    public Catalog saveCatalog(Catalog catalog) {
        //判重
        List<Catalog> list = catalogRepository.findByUserAndName(catalog.getUser(), catalog.getName());
        if (list != null && list.size() > 0) {
            throw new IllegalArgumentException("该分类已经存在");
        }
        return catalogRepository.save(catalog );
    }

    @Override
    public void removeCatalog(Long id) {
        catalogRepository.delete(id);
    }

    @Override
    public Catalog getCatalogById(Long id) {
        return catalogRepository.findOne(id);
    }

    @Override
    public List<Catalog> listCatalogs(User user) {
        return catalogRepository.findByUser(user);
    }

    @Override
    public Page<Catalog> listAllCatalogs(Pageable pageable) {
        Page<Catalog> catalogs = catalogRepository.findAll(pageable);
        return catalogs;
    }
}
