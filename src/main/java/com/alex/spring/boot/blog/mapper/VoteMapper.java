package com.alex.spring.boot.blog.mapper;


import com.alex.spring.boot.blog.domain.Vote;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface VoteMapper {

    /**
     * 保存点赞记录
     */
    void saveVote(Vote vote);

    /**
     * 获取点赞记录
     */

    Vote getVote(Integer blogId,Integer userId);
}
