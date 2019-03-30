package com.lsy.spring.boot.blog.service;

import com.lsy.spring.boot.blog.domain.Authority;
import com.lsy.spring.boot.blog.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Authority接口服务的实现.
 * @Author: yoo
 * @Date: 2019-01-08 16:24
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;
    @Override
    public Authority getAuthorityById(Long id) {
       return authorityRepository.findOne(id);
    }
}
