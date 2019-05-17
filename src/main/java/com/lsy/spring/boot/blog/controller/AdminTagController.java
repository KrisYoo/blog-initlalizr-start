package com.lsy.spring.boot.blog.controller;

import com.lsy.spring.boot.blog.domain.Blog;
import com.lsy.spring.boot.blog.domain.Vote;
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
 * @Date: 2019-05-14 19:04
 */
@RestController
@RequestMapping("/admintags")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
public class AdminTagController {
    @Autowired
    private com.lsy.spring.boot.blog.service.BlogService blogService;

    /**
     * 查询博客
     * @return
     */
    @GetMapping
    public ModelAndView listTags(
            @RequestParam(value="async",required=false) boolean async,
            @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
            @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
            Model model) {
        Pageable pageable = new PageRequest(pageIndex, pageSize);
        Page<Blog> page = blogService.listAllBlogs(pageable);
        List<Blog> list = page.getContent();	// 当前所在页面数据列表

        model.addAttribute("page", page);
        model.addAttribute("adminTags", list);
        return new ModelAndView(async==true?"blog/tags :: #mainContainerRepleace":"blog/tags", "adminTagModel", model);
    }

    @DeleteMapping("/tags/{id}")
    public ResponseEntity<Response> deleteTags(@PathVariable("id") Long id) {
        Blog blog = blogService.getBlogById(id);
        blog.setTags("");
        try {
            blogService.removeTags(blog);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功"));
    }
}