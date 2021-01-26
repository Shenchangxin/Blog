package com.alex.spring.boot.blog.service;

import com.alex.spring.boot.blog.domain.Blog;
import com.alex.spring.boot.blog.domain.Comment;
import com.alex.spring.boot.blog.domain.Reply;
import com.alex.spring.boot.blog.domain.User;
import com.alex.spring.boot.blog.mapper.BlogMapper;
import com.alex.spring.boot.blog.mapper.CommentMapper;
import com.alex.spring.boot.blog.mapper.ReplyMapper;
import com.alex.spring.boot.blog.mapper.UserMapper;
import com.alex.spring.boot.blog.util.DateUtil;
import com.alex.spring.boot.blog.util.JsonWebTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;

@Service
public class ReplyServiceImpl implements ReplyService{

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private JsonWebTokenUtil jsonWebTokenUtil;

    @Autowired
    private DateUtil dateUtil;

    @Autowired
    private HttpServletRequest request;

    /**
     * 保存回复
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveReply(Integer commentId, String replyBody, Integer rootId) {
        User user = userMapper.findUserByName(jsonWebTokenUtil.getUsernameFromRequest(request));
        Reply reply = new Reply();
        Comment comment = commentMapper.findCommentById(commentId);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }

        reply.setComment(comment);
        reply.setUser(user);
        reply.setBody(replyBody);
        reply.setTime(dateUtil.getCurrentDate());
        Reply rootReply = new Reply();
        rootReply.setId(rootId);
        reply.setReply(rootReply);
        replyMapper.saveReply(reply);

        //更新博文评论数
        Blog blog = blogMapper.findBlogById(comment.getBlog().getId());
        blog.setCommentCount(blog.getCommentCount() + 1);
        blogMapper.updateBlog(blog);

    }

    /**
     * 删除回复
     */
    @Override
    public void deleteReply(Integer replyId) {
        User user = userMapper.findUserByName(jsonWebTokenUtil.getUsernameFromRequest(request));
        Reply reply = replyMapper.findReplyById(replyId);
        if (reply == null) {
            throw new RuntimeException("回复不存在");
        }

        if (!user.getId().equals(reply.getUser().getId())) {
            throw new RuntimeException("无权删除");
        }

        //删除回复
        replyMapper.deleteReplyById(replyId);

        Comment discuss = commentMapper.findCommentById(reply.getComment().getId());
        //更新博文评论数
        Blog blog = blogMapper.findBlogById(discuss.getBlog().getId());
        blog.setCommentCount(blog.getCommentCount() - 1);
        blogMapper.updateBlog(blog);

    }

    /**
     * 管理员删除回复
     *
     * @param replyId
     */
    @Override
    public void adminDeleteReply(Integer replyId) {
        Reply reply = replyMapper.findReplyById(replyId);
        if (reply == null) {
            throw new RuntimeException("回复不存在");
        }
        //删除回复
        replyMapper.deleteReplyById(replyId);

        Comment comment = commentMapper.findCommentById(reply.getComment().getId());
        //更新博文评论数
        Blog blog = blogMapper.findBlogById(comment.getBlog().getId());
        blog.setCommentCount(blog.getCommentCount() - 1);
        blogMapper.updateBlog(blog);
    }
}

