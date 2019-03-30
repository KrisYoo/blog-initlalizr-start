package com.lsy.spring.boot.blog.repository;

import com.lsy.spring.boot.blog.domain.Blog;
import com.lsy.spring.boot.blog.domain.Catalog;
import com.lsy.spring.boot.blog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Blog 仓库
 * @Author: yoo
 * @Date: 2019-01-10 16:37
 */
public interface BlogRepository extends JpaRepository<Blog, Long> {

    /**
     * 根据用户名，博客标题分页查询博客列表
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Blog> findByUserAndTitleLike(User user, String title, Pageable pageable);

    /**
     * 根据用户名、博客查询博客列表（时间逆序）
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Blog> findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(String title, User user, String tags, User user2, Pageable pageable);

    /**
     * 根据分类查询博客列表
     * @param catalog
     * @param pageable
     * @return
     */
    Page<Blog> findByCatalog(Catalog catalog, Pageable pageable);

}
