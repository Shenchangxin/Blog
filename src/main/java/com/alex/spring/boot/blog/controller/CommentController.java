package com.alex.spring.boot.blog.controller;


import com.alex.spring.boot.blog.domain.Comment;
import com.alex.spring.boot.blog.dto.PageResult;
import com.alex.spring.boot.blog.dto.Result;
import com.alex.spring.boot.blog.dto.StatusCode;
import com.alex.spring.boot.blog.service.CommentService;
import com.alex.spring.boot.blog.util.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "评论api", description = "评论api", basePath = "/comment")
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private FormatUtil formatUtil;



    @ApiOperation(value = "发布评论", notes = "评论内容+博文id")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/{blogId}")
    public Result discuss(String commentBody, @PathVariable Integer blogId) {
        if (!formatUtil.checkStringNull(commentBody)) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }
        if (!formatUtil.checkPositive(blogId)) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }

        commentService.saveComment(commentBody, blogId);
        return Result.create(StatusCode.OK, "评论成功");
    }


    @ApiOperation(value = "删除评论", notes = "评论id")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping("/{commentId}")
    public Result deleteDiscuss(@PathVariable Integer commentId) {
        if (!formatUtil.checkPositive(commentId)) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }
        try {
            commentService.deleteComment(commentId);
            return Result.create(StatusCode.OK, "删除评论成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, "删除失败" + e.getMessage());
        }

    }

    @ApiOperation(value = "管理员删除评论", notes = "评论id")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/admin/{commentId}")
    public Result adminDeleteDiscuss(@PathVariable Integer commentId) {
        if (!formatUtil.checkPositive(commentId)) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }

        try {
            commentService.adminDeleteComment(commentId);
            return Result.create(StatusCode.OK, "删除评论成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, "删除失败" + e.getMessage());
        }

    }

    @ApiOperation(value = "分页查询博文评论以及回复", notes = "博文id+页码+显示数")
    @GetMapping("/{blogId}/{page}/{showCount}")
    public Result getDiscussByBlog(@PathVariable Integer blogId,
                                   @PathVariable Integer page,
                                   @PathVariable Integer showCount) {

        if (!formatUtil.checkPositive(blogId, page, showCount)) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }
        PageResult<Comment> pageResult = new PageResult<>(commentService.getCommentCountByBlogId(blogId), commentService.findCommentByBlogId(blogId, page, showCount));

        return Result.create(StatusCode.OK, "查询成功", pageResult);
    }

    @ApiOperation(value = "首页获取最新评论", notes = "获取最新六条评论")
    @GetMapping("/newComment")
    public Result newDiscuss() {
        return Result.create(StatusCode.OK, "查询成功", commentService.findNewComment());
    }

    @ApiOperation(value = "获取用户发布的所有博文下的评论", notes = "获取用户发布的所有博文下的评论")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/userNewComment")
    public Result userNewDiscuss() {
        return Result.create(StatusCode.OK, "查询成功", commentService.findUserNewComment());
    }


}
