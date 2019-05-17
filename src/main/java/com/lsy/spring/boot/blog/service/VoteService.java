package com.lsy.spring.boot.blog.service;

import com.lsy.spring.boot.blog.domain.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * vote 服务接口.
 * @Author: yoo
 * @Date: 2019-01-16 21:39
 */
public interface VoteService {

    /**
     * 根据Id获取vote
     * @param id
     * @return
     */
    Vote getVoteById(Long id);

    /**
     * 删除vote
     * @param id
     */
    void removeVote(Long id);
}
