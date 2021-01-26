package com.alex.spring.boot.blog.service;

import com.alex.spring.boot.blog.domain.Blog;
import com.alex.spring.boot.blog.domain.Comment;
import com.alex.spring.boot.blog.domain.Reply;
import com.alex.spring.boot.blog.domain.User;
import com.alex.spring.boot.blog.dto.Result;
import com.alex.spring.boot.blog.mapper.BlogMapper;
import com.alex.spring.boot.blog.mapper.CommentMapper;
import com.alex.spring.boot.blog.mapper.ReplyMapper;
import com.alex.spring.boot.blog.mapper.UserMapper;
import com.alex.spring.boot.blog.util.DateUtil;
import com.alex.spring.boot.blog.util.FormatUtil;
import com.alex.spring.boot.blog.util.JsonWebTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private JsonWebTokenUtil jsonWebTokenUtil;

    @Autowired
    private DateUtil dateUtil;

    @Autowired
    private HttpServletRequest request;

    /**
     * 发布评论
     * 博文评论数加1
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveComment(String commentBody, Integer blogId) {
        User user = userMapper.findUserByName(jsonWebTokenUtil.getUsernameFromRequest(request));
        Blog blog = blogMapper.findBlogById(blogId);
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setBody(commentBody);
        comment.setBlog(blog);
        comment.setTime(dateUtil.getCurrentDate());
        commentMapper.saveComment(comment);

        //评论数加一
        blog.setCommentCount(blog.getCommentCount() + 1);
        blogMapper.updateBlog(blog);
    }

    /**
     * 删除评论
     * 级联删除评论下的所有回复
     * 博文评论数 - (评论数+回复数)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Integer commentId) {
        User user = userMapper.findUserByName(jsonWebTokenUtil.getUsernameFromRequest(request));
        Comment discuss = commentMapper.findCommentById(commentId);
        if (discuss == null) {
            throw new RuntimeException("评论不存在");
        }
        if (!user.getId().equals(discuss.getUser().getId())) {
            throw new RuntimeException("无权删除");
        }

        commentMapper.deleteCommentById(commentId);

        //返回受影响行数
        Integer rows = replyMapper.deleteReplyByCommentId(commentId);


        Blog blog = blogMapper.findBlogById(discuss.getBlog().getId());
        blog.setCommentCount(blog.getCommentCount() - 1 - rows);
        blogMapper.updateBlog(blog);
    }

    /**
     * 管理员删除评论
     * 博文评论数-1
     */
    @Override
    public void adminDeleteComment(Integer commentId) {
        Comment comment = commentMapper.findCommentById(commentId);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }
        commentMapper.deleteCommentById(commentId);

        Integer rows = replyMapper.deleteReplyByCommentId(commentId); //返回受影响行数

        Blog blog = blogMapper.findBlogById(comment.getBlog().getId());
        blog.setCommentCount(blog.getCommentCount() - 1 - rows);
        blogMapper.updateBlog(blog);
    }

    /**
     * 根据博文id查询 该博文下的评论及回复
     *
     * @param blogId
     * @return
     */
    @Override
    public List<Comment> findCommentByBlogId(Integer blogId, Integer page, Integer showCount) {

        List<Comment> comments = commentMapper.findCommentByBlogId(blogId, (page - 1) * showCount, showCount);

        for (Comment comment : comments) {
            List<Reply> replyList = replyMapper.findReplyByCommentId(comment.getId());
            for (Reply reply : replyList) {
                if (reply.getReply() != null) {
                    reply.setReply(replyMapper.findReplyById(reply.getReply().getId()));
                }
            }
            comment.setReplyList(replyList);
        }
        return comments;
    }

    /**
     * 获取博文下评论数量
     *
     * @param blogId
     * @return
     */
    @Override
    public Long getCommentCountByBlogId(Integer blogId) {
        return commentMapper.getCommentCountByBlogId(blogId);
    }

    /**
     * 获取最新六条评论
     *
     * @return
     */
    @Override
    public List<Comment> findNewComment() {
        return commentMapper.findNewComment(6);
    }


    /**
     * 获取用户发布的所有博文下的评论
     *
     * @return
     */
    @Override
    public List<Comment> findUserNewComment() {
        User user = userMapper.findUserByName(jsonWebTokenUtil.getUsernameFromRequest(request));
        return commentMapper.findUserNewComment(user.getId(), 6);
    }
}

