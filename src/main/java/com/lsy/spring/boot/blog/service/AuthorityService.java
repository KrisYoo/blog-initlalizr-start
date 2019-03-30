package com.lsy.spring.boot.blog.service;

import com.lsy.spring.boot.blog.domain.Authority;

/**
 * Authority服务接口.
 * @Author: yoo
 * @Date: 2019-01-08 16:22
 */
public interface AuthorityService {

    /**
     * 根据Id查询Authority.
     * @param id
     * @return
     */
    Authority getAuthorityById(Long id);
}
