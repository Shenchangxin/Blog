package com.alex.spring.boot.blog.service;

public interface ReplyService {

    /**
     * 保存回复
     */
     void saveReply(Integer commentId, String replyBody, Integer rootId);

    /**
     * 删除回复
     *
     * @param replyId
     */
     void deleteReply(Integer replyId);

    /**
     * 管理员删除回复
     */
     void adminDeleteReply(Integer replyId);
}
