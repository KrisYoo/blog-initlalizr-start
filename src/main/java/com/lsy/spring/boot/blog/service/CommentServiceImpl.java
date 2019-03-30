package com.lsy.spring.boot.blog.service;

import com.lsy.spring.boot.blog.domain.Comment;
import com.lsy.spring.boot.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * CommentService 实现类.
 * @Author: yoo
 * @Date: 2019-01-16 17:00
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findOne(id);
    }

    @Override
    @Transactional
    public void removeComment(Long id) {
        commentRepository.delete(id);
    }
}
