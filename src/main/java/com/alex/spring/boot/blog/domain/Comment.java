package com.alex.spring.boot.blog.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.*;


/**
 * 评论
 */
@Data
@ToString
public class Comment {
    private Integer id;//id
    private String body;//评论内容
    private Date time;//评论时间
    private User user;//评论用户
    private Blog blog;//评论博文
    private List<Reply> replyList;


}

