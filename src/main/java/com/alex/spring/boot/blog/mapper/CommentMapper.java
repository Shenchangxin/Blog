package com.alex.spring.boot.blog.mapper;

import com.alex.spring.boot.blog.domain.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 评论Mapper
 */
@Repository
@Mapper
public interface CommentMapper {
    /**
     * 保存回复
     */
    void saveComment(Comment comment);

    /**
     * 根据id查询评论
     */
    Comment findCommentById(Integer commentId);

    /**
     * 根据id删除评论
     */
    void deleteCommentById(Integer commentId);

    /**
     * 查询博文下的评论
     *
     */
    List<Comment> findCommentByBlogId(@Param("blogId") Integer blogId, @Param("start") Integer start, @Param("showCount") Integer showCount);

    /**
     * 获取博文下评论数量
     */
    Long getCommentCountByBlogId(Integer blogId);

    /**
     * 获取最新count 条评论
     */
    List<Comment> findNewComment(Integer count);

    /**
     * 查询
     */
    List<Comment> findUserNewComment(@Param("userId") Integer userId,@Param("count") Integer count);
}
