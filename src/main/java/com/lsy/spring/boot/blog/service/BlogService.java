package com.lsy.spring.boot.blog.service;

import com.lsy.spring.boot.blog.domain.Blog;
import com.lsy.spring.boot.blog.domain.Catalog;
import com.lsy.spring.boot.blog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Author: yoo
 * @Date: 2019-01-10 16:45
 */
public interface BlogService {
    /**
     * 保存
     * @param blog
     * @return
     */
    Blog saveBlog(Blog blog);

    /**
     * 删除
     * @param id
     */
    void removeBlog(Long id);

    /**
     * 通过Id获取博客
     * @param id
     * @return
     */
    Blog getBlogById(Long id);

    /**
     * 根据用户名进行博客名称分页模糊查询（最新）
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Blog> listBlogsByTitleVote(User user, String title, Pageable pageable);

    /**
     * 根据用户名进行博客名称分页模糊查询（最热）
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Blog> listBlogsByTitleVoteAndSort(User user, String title, Pageable pageable);

    /**
     * 阅读量递增
     * @param id
     */
    void readingIncrease(Long id);

    /**
     * 发表评论
     * @param blogId
     * @param commentContent
     * @return
     */
    Blog createComment(Long blogId, String commentContent);

    /**
     * 删除评论
     * @param blogId
     * @param commentId
     */
    void removeComment(Long blogId, Long commentId);

    /**
     * 点赞
     * @param blogId
     * @return
     */
    Blog createVote(Long blogId);

    /**
     * 取消点赞
     * @param blogId
     * @param voteId
     * @return
     */
    void removeVote(Long blogId, Long voteId);

    /**
     * 根据分类查询博客
     * @param catalog
     * @param pageable
     * @return
     */
    Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable);

    /**
     * 获取所有博客
     * @param pageable
     * @return
     */
    Page<Blog> listAllBlogs(Pageable pageable);

    void removeTags(Blog blog);

}
