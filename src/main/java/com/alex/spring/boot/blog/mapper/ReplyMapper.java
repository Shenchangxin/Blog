package com.alex.spring.boot.blog.mapper;

import com.alex.spring.boot.blog.domain.Reply;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ReplyMapper{
    /**
     * 保存回复
     */
    void saveReply(Reply reply);

    /**
     * 根据id查询回复
     */
    Reply findReplyById(Integer replyId);

    /**
     * 根据id删除回复
     */
    void deleteReplyById(Integer replyId);

    /**
     * 根据评论id查询回复
     */
    List<Reply> findReplyByCommentId(Integer id);

    /**
     * 根据评论id删除回复
     */
    Integer deleteReplyByCommentId(Integer discussId);
}

