package com.lsy.spring.boot.blog.controller;

import com.lsy.spring.boot.blog.domain.Catalog;
import com.lsy.spring.boot.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @Author: yoo
 * @Date: 2019-05-12 17:11
 */
@RestController
@RequestMapping("/admincatalogs")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
public class AdminCatalogController {
    @Autowired
    private com.lsy.spring.boot.blog.service.CatalogService catalogService;

    /**
     * 查询博客
     * @return
     */
    @GetMapping
    public ModelAndView listCatalogs(
            @RequestParam(value="async",required=false) boolean async,
            @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
            @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
            Model model) {
        Pageable pageable = new PageRequest(pageIndex, pageSize);
        Page<Catalog> page = catalogService.listAllCatalogs(pageable);
        List<Catalog> list = page.getContent();	// 当前所在页面数据列表

        model.addAttribute("page", page);
        model.addAttribute("adminCatalogs", list);
        return new ModelAndView(async==true?"blog/catalogs :: #mainContainerRepleace":"blog/catalogs", "adminCatalogModel", model);
    }

    @DeleteMapping("/catalogs/{id}")
    public ResponseEntity<Response> deleteBlog(@PathVariable("id") Long id) {
        try {
            catalogService.removeCatalog(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功"));
    }
}
