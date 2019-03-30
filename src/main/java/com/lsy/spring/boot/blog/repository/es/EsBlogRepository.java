package com.lsy.spring.boot.blog.repository.es;

import com.lsy.spring.boot.blog.domain.es.EsBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsBlogRepository extends ElasticsearchRepository<EsBlog, String> {

    /**
     * 模糊查询(去重)
     * @param title
     * @param Summary
     * @param content
     * @param tags
     * @param pageable
     * @return
     */
    Page<EsBlog> findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining
            (String title, String Summary, String content, String tags, Pageable pageable);

    /**
     * 通过blogId查询Blog.
     * @param blogId
     * @return
     */
    EsBlog findByBlogId(Long blogId);

}
