package com.alex.spring.boot.blog.service;

import com.alex.spring.boot.blog.domain.Comment;
import java.util.List;

/**
 * Comment服务接口
 */
public interface CommentService {


    /**
     * 发布评论
     * 博文评论数加1
     */
    void saveComment(String commentBody, Integer blogId);

    /**
     * 删除评论
     * 级联删除评论下的所有回复
     * 博文评论数 - (评论数+回复数)
     */

    void deleteComment(Integer commentId) ;

    /**
     * 管理员删除评论
     * 博文评论数-1
     */
    void adminDeleteComment(Integer commentId);

    /**
     * 根据博文id查询 该博文下的评论及回复
     */
    List<Comment> findCommentByBlogId(Integer blogId, Integer page, Integer showCount);

    /**
     * 获取博文下评论数量
     */
    Long getCommentCountByBlogId(Integer blogId);

    /**
     * 获取最新六条评论
     */
    List<Comment> findNewComment();


    /**
     * 获取用户发布的所有博文下的评论
     */
    List<Comment> findUserNewComment();
}
