package com.lsy.spring.boot.blog.repository;

import com.lsy.spring.boot.blog.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Comment 资源库.
 * @Author: yoo
 * @Date: 2019-01-16 16:55
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
