package com.alex.spring.boot.blog.domain;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.ToString;

/**
 * 博文
 */
@Data
@ToString(exclude = "body")
public class Blog implements Serializable {
    /**
     * blog(36) => 541312(10)
     */
    private static final long serialVersionUID = 541312L;
    /**
     * id
     */
    private Integer id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String body;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 浏览数
     */
    private Integer blogViews;

    /**
     * 发布时间
     */
    private Date time;
    /**
     * 博文状态--0删除 1正常
     */
    private Integer state;

    /**
     * 所属用户
     */
    private User user;
    /**
     * 博文对应的标签
     */
    private List<Tag> tags;

    /**
     * 博文点赞数
     */
    private Integer likeCount;

    public Blog() {
    }

    public Blog(int blogId, int likeCount) {
        this.id = blogId;
        this.likeCount = likeCount;
    }
}