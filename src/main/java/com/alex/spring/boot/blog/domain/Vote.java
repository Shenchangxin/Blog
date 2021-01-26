package com.alex.spring.boot.blog.domain;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户点赞状态
 */
@Data
@ToString
@ApiModel("点赞")
public class Vote implements Serializable {

    private static final long serialVersionUID = -3956628880213302317L;

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 点赞的用户
     */
    private User user;

    /**
     * 被点赞的用户的博文
     */
    private Blog blog;

    /**
     * 点赞的状态.默认未点赞
     */
    private Integer status = 0;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public Vote() {
    }

    public Vote(int blogId, int userId, int status) {
        Blog blog = new Blog();
        blog.setId(blogId);

        User user = new User();
        user.setId(userId);

        this.blog = blog;
        this.user = user;
        this.status = status;
    }
}
