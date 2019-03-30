package com.lsy.spring.boot.blog.repository;

import com.lsy.spring.boot.blog.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Authority仓库.
 * @Author: yoo
 * @Date: 2019-01-08 16:21
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}
