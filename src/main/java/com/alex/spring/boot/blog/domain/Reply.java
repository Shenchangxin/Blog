package com.alex.spring.boot.blog.domain;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 回复
 */
@Data
@ToString
public class Reply {
    private Integer id;//id
    private String body;//回复内容
    private Date time;//回复时间
    private User user;//用户
    private Comment comment;//评论
    private Reply reply;//父节点回复




}
