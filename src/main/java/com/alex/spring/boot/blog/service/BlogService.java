package com.alex.spring.boot.blog.service;

import com.alex.spring.boot.blog.domain.Blog;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Blog服务接口
 */
public interface BlogService {

    /**
     * 保存博文
     *
     * @param blogTitle
     * @param blogBody
     * @param tagIds
     */
    void saveBlog(String blogTitle, String blogBody, Integer[] tagIds) throws JsonProcessingException;

    /**
     * 根据id查询博文以及博文标签
     * 正常状态
     *
     * @param blogId
     * @return
     */
    Blog findBlogById(Integer blogId, boolean isHistory) throws IOException;


    /**
     * 根据用户查询博文以及标签
     * 正常状态
     *
     * @param page      页数
     * @param showCount 显示数量
     * @return
     */
    List<Blog> findBlogByUser(Integer page, Integer showCount) ;

    /**
     * 查询该用户的博客数量
     * 正常状态
     *
     * @return
     */
    Long getBlogCountByUser();

    /**
     * 查询主页所有博客数量
     * 正常状态
     *
     * @return
     */
    Long getHomeBlogCount();

    /**
     * 查询主页博客
     * 正常状态
     *
     * @param page      页码
     * @param showCount 显示条数
     * @return
     */
    List<Blog> findHomeBlog(Integer page, Integer showCount) throws IOException;

    /**
     * 查询热门博文
     * 正常状态
     *
     * @return
     */
    List<Blog> findHotBlog() throws IOException ;

    /**
     * 搜索博文
     * 正常状态
     *
     * @param searchText
     * @return
     */
    List<Blog> searchBlog(String searchText, Integer page, Integer showCount);

    /**
     * 符合关键词的博文数量
     * 正常状态
     *
     * @param searchText
     * @return
     */
    Long getSearchBlogCount(String searchText);

    /**
     * 查询所有博文
     * 正常状态
     *
     * @return
     */
    List<Blog> findAllBlog(Integer page, Integer showCount);

    /**
     * 修改博文
     *
     * @param blogId
     * @param blogTitle
     * @param blogBody
     * @param tagIds
     */
    void updateBlog(Integer blogId, String blogTitle, String blogBody, Integer[] tagIds) throws JsonProcessingException;

    /**
     * 用户删除博文
     *
     * @param blogId
     */
    void deleteBlog(Integer blogId) throws JsonProcessingException ;

    /**
     * 管理员删除博文
     *
     * @param blogId
     */
    void adminDeleteBlog(Integer blogId)throws JsonProcessingException;



    /**
     * 符合关键字的博文数量
     * 所有状态
     *
     * @param searchText
     * @return
     */
    Long getSearchAllBlogCount(String searchText);

    /**
     * 搜索博文
     * 所有状态
     *
     * @param searchText 搜索内容
     * @param page
     * @param showCount
     * @return
     */
    List<Blog> searchAllBlog(String searchText, Integer page, Integer showCount) ;


    /**
     * 按月份归档博客
     * 正常状态
     *
     * @return
     */
    List<Map> statisticalBlogByMonth()throws IOException;

    /**
     * 查询博客记录数
     * 所有状态
     *
     * @return
     */
    Long getAllBlogCount();

    /**
     * @Description: 获取用户点赞数
     * @Param: [blogId]
     * @return: int
     */
    int getBlogLikeCountByBlogId(Integer blogId);


}
