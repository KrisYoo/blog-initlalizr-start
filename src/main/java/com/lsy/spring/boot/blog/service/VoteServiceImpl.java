package com.lsy.spring.boot.blog.service;

import com.lsy.spring.boot.blog.domain.Vote;
import com.lsy.spring.boot.blog.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * vote 服务实现.
 * @Author: yoo
 * @Date: 2019-01-16 21:40
 */
@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Override
    public Vote getVoteById(Long id) {
        return voteRepository.findOne(id);
    }

    @Override
    @Transactional
    public void removeVote(Long id) {
        voteRepository.delete(id);
    }
}
