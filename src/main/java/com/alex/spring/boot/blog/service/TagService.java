package com.alex.spring.boot.blog.service;


import com.alex.spring.boot.blog.domain.Tag;

import java.util.List;

public interface TagService {

    /**
     * 新增标签
     */
    void saveTag(String tagName);

    /**
     * 删除标签
     */
    void deleteTagById(Integer tagId);

    /**
     * 更改标签
     *
     */
    void updateTag(Integer tagId, String tagName);

    /**
     * 查询该user下的所有标签
     */
     List<Tag> findTagByUserId();
}
