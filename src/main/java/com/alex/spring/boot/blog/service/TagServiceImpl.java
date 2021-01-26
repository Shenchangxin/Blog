package com.alex.spring.boot.blog.service;


import com.alex.spring.boot.blog.domain.Tag;
import com.alex.spring.boot.blog.domain.User;
import com.alex.spring.boot.blog.mapper.BlogMapper;
import com.alex.spring.boot.blog.mapper.TagMapper;
import com.alex.spring.boot.blog.mapper.UserMapper;
import com.alex.spring.boot.blog.util.JsonWebTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JsonWebTokenUtil jsonWebTokenUtil;

    /**
     * 新增标签
     */
    @Override
    public void saveTag(String tagName) {
        String username = jsonWebTokenUtil.getUsernameFromRequest(request);
        User user = userMapper.findUserByName(username);


        if (tagMapper.findTagByTagName(tagName) != null) //mysql where tag_name 忽略大小写
        {
            throw new RuntimeException("标签重复");
        }

        Tag tag = new Tag();
        tag.setUser(user);
        tag.setName(tagName);
        tagMapper.saveTag(tag);
    }

    /**
     * 删除标签
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTagById(Integer tagId) {
        String username = jsonWebTokenUtil.getUsernameFromRequest(request);
        User user = userMapper.findUserByName(username);
        Tag tag = tagMapper.findTagById(tagId);
        if (!user.getId().equals(tag.getUser().getId())) {
            throw new RuntimeException("无权删除此标签");
        }

        //查询此标签下是否有博文
        if (blogMapper.findBlogCountByTagId(tagId) > 0) {
            throw new RuntimeException("此标签关联了博客");
        }

        tagMapper.deleteTag(tagId);
    }

    /**
     * 更改标签
     */
    @Override
    public void updateTag(Integer tagId, String tagName) {
        String username = jsonWebTokenUtil.getUsernameFromRequest(request);
        User user = userMapper.findUserByName(username);
        Tag tag = tagMapper.findTagById(tagId);
        if (!user.getId().equals(tag.getUser().getId())) {
            throw new RuntimeException("无权修改此标签");
        }
        tag.setName(tagName);
        tagMapper.updateTagName(tag);
    }

    /**
     * 查询该user下的所有标签
     */
    @Override
    public List<Tag> findTagByUserId() {
        String username = jsonWebTokenUtil.getUsernameFromRequest(request);
        User user = userMapper.findUserByName(username);
        return tagMapper.findTagByUserId(user.getId());
    }
}
