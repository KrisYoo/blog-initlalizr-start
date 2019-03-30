package com.lsy.spring.boot.blog.repository;

import com.lsy.spring.boot.blog.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: yoo
 * @Date: 2019-01-16 21:38
 */
public interface VoteRepository extends JpaRepository<Vote, Long> {
}
