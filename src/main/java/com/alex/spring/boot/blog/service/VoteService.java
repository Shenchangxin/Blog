package com.alex.spring.boot.blog.service;

import com.alex.spring.boot.blog.domain.Vote;



public interface VoteService {

    /**
     * @Description: 保存用户点赞数据
     */
    void saveVote(Vote vote);
    /**
     * @Description: 用户是否点过赞
     */
    boolean getVote(Integer blogId);
}
