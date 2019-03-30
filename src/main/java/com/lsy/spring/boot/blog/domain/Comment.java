package com.lsy.spring.boot.blog.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Comment实体
 * @Author: yoo
 * @Date: 2019-01-16 14:51
 */
@Entity //实体
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id //主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增长策略
    private Long id; // 用户的唯一标示

    @NotEmpty(message = "评论内容不能为空")
    @Size(min = 2, max = 500)
    @Column(nullable = false) //映射字段不能为空
    private String content; //评论内容

    /**
     * CascadeType.DETACH: 级联脱管操作
     * 如果你要删除一个实体，但是它有外键无法删除，
     * 你就需要这个级联权限了。它会撤销所有相关的外键关联。
     * FetchType.LAZY: 懒加载
     * 加载一个实体时，定义懒加载的属性不会马上从数据库中加载。
     */
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private  User user;

    @Column(nullable = false) // 映射为字段，值不能为空
    @org.hibernate.annotations.CreationTimestamp // 由数据库自动创建时间
    private Timestamp createTime;

    protected Comment() { //JPA 的规范要求无参构造函数；设为 protected 防止直接使用

    }

    public Comment( User user, String content) {
        this.content = content;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }
}
