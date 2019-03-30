package com.lsy.spring.boot.blog.service;

import com.lsy.spring.boot.blog.domain.Comment;

/**
 * Comment Service 接口.
 * @Author: yoo
 * @Date: 2019-01-16 16:57
 */
public interface CommentService {

    /**
     * 根据Id获取Comment
     * @param id
     * @return
     */
    Comment getCommentById(Long id);

    /**
     * 根据Id移除Comment(删除评论)
     * @param id
     */
    void removeComment(Long id);
}
