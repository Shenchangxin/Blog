package com.alex.spring.boot.blog.controller;


import com.alex.spring.boot.blog.domain.Vote;
import com.alex.spring.boot.blog.dto.Result;
import com.alex.spring.boot.blog.dto.StatusCode;
import com.alex.spring.boot.blog.service.VoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 用户点赞
 */
@Api(tags = "用户点赞api", description = "用户点赞api", basePath = "/vote")
@RestController
@RequestMapping("/vote")
public class VoteController {

    @Autowired
    private VoteService voteService;

    /**
     * 保存点赞数据
     */
    @ApiOperation(value = "保存点赞数据")
    @PostMapping("/saveVote")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Result saveUserLike(Vote vote) {
        if (voteService.getVote(vote.getBlog().getId())) {
            return Result.create(StatusCode.ERROR, "你已点过赞");
        }

        try {
            voteService.saveVote(vote);
            return Result.create(StatusCode.OK, "点赞记录保存成功");
        } catch (RuntimeException re) {
            return Result.create(StatusCode.ERROR, re.getMessage());
        }
    }

    /**
     * 判断用户是否点过赞
     */
    @ApiOperation(value = "用户是否点过赞")
    @GetMapping("/isVote/{blogId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Result getUserLike(@PathVariable Integer blogId) {
        try {
            return Result.create(StatusCode.OK, "获取点赞记录成功", voteService.getVote(blogId));
        } catch (RuntimeException re) {
            return Result.create(StatusCode.ERROR, re.getMessage());
        }
    }
}