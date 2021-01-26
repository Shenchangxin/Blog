package com.alex.spring.boot.blog.service;

import com.alex.spring.boot.blog.domain.Blog;
import com.alex.spring.boot.blog.domain.User;
import com.alex.spring.boot.blog.domain.Vote;
import com.alex.spring.boot.blog.mapper.BlogMapper;
import com.alex.spring.boot.blog.mapper.UserMapper;
import com.alex.spring.boot.blog.mapper.VoteMapper;
import com.alex.spring.boot.blog.util.JsonWebTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;


/**
 * 点赞服务
 */
@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteMapper voteMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private JsonWebTokenUtil jsonWebTokenUtil;

    @Autowired
    private HttpServletRequest request;

    /**
     * 保存用户点赞数据
     */
    @Override
    public void saveVote(Vote vote) {
        User user = userMapper.findUserByName(jsonWebTokenUtil.getUsernameFromRequest(request));
        vote.setUser(user);
        Blog blog =blogMapper.findBlogById(vote.getBlog().getId());
        Integer likeCount = blog.getLikeCount();
        blog.setLikeCount(likeCount + 1);
        blogMapper.updateBlog(blog);
        voteMapper.saveVote(vote);
    }

    /**
     * 用户是否点过赞
     */
    @Override
    public boolean getVote(Integer blogId) {
        User user = userMapper.findUserByName(jsonWebTokenUtil.getUsernameFromRequest(request));
        int status = 0;
        Vote vote = voteMapper.getVote(blogId, user.getId());
        if (vote != null) {
            status = vote.getStatus();
        }
        return status != 0;
    }

}

