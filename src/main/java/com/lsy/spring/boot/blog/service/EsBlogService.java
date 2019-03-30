package com.lsy.spring.boot.blog.service;

import com.lsy.spring.boot.blog.domain.User;
import com.lsy.spring.boot.blog.domain.es.EsBlog;
import com.lsy.spring.boot.blog.vo.TagVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * EsBlog 接口.
 * @Author: yoo
 * @Date: 2019-01-17 21:37
 */
public interface EsBlogService {
    /**
     * 删除EsBlog
     * @param id
     */
    void removeEsBlog(String id);

    /**
     * 更新EsBlog
     * @param esBlog
     * @return
     */
    EsBlog updateEsBlog(EsBlog esBlog);

    /**
     * 根据id获取Blog
     * @param blogId
     * @return
     */
    EsBlog getEsBlogByBlogId(Long blogId);

    /**
     * 最新博客列表,分页
     * @param keyword
     * @param pageable
     * @return
     */
    Page<EsBlog> listNewestEsBlogs(String keyword, Pageable pageable);

    /**
     * 最热博客列表,分页
     * @param keyword
     * @param pageable
     * @return
     */
    Page<EsBlog> listHotestEsblogs(String keyword, Pageable pageable);

    /**
     * 博客列表，分页
     * @param pageable
     * @return
     */
    Page<EsBlog> listEsBlogs(Pageable pageable);

    /**
     * 最新前5
     * @return
     */
    List<EsBlog> listTop5NewestEsBlogs();

    /**
     * 最热前5
     * @return
     */
    List<EsBlog> listTop5HotestEsBlogs();

    /**
     * 最热前 30 标签
     * @return
     */
    List<TagVO> listTop30Tags();

    /**
     * 最热前12用户
     * @return
     */
    List<User> listTop12Users();
}
