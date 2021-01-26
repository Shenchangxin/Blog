package com.alex.spring.boot.blog.service;

import com.alex.spring.boot.blog.domain.*;
import com.alex.spring.boot.blog.mapper.*;
import com.alex.spring.boot.blog.util.DateUtil;
import com.alex.spring.boot.blog.util.JsonWebTokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class BlogServiceImpl implements BlogService {


    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private TagMapper tagMapper;


    @Autowired
    private DateUtil dateUtil;

    @Autowired
    private JsonWebTokenUtil jsonWebTokenUtil;

    @Autowired
    private HttpServletRequest request;


    @Autowired
    private VoteMapper voteMapper;

    /**
     * 保存博文
     *
     * @param blogTitle
     * @param blogBody
     * @param tagIds
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveBlog(String blogTitle, String blogBody, Integer[] tagIds) throws JsonProcessingException {
        User user = userMapper.findUserByName(jsonWebTokenUtil.getUsernameFromRequest(request));

        for (Integer tagId : tagIds) {
            // 通过标签id检查标签是否属于该用户
            if (!tagMapper.findTagById(tagId).getUser().getId().equals(user.getId())) {
                throw new RuntimeException();
            }
        }

        Blog blog = new Blog();
        //博文用户
        blog.setUser(user);
        //浏览量
        blog.setBlogViews(0);
        //评论数
        blog.setCommentCount(0);
        //标题
        blog.setTitle(blogTitle);
        //内容
        blog.setBody(blogBody);
        //1 正常状态
        blog.setState(1);
        //点赞数
        blog.setLikeCount(0);
        //发布时间
        blog.setTime(dateUtil.getCurrentDate());
        blogMapper.saveBlog(blog);

        for (Integer tagId : tagIds) {
            //保存该博文的标签
            blogMapper.saveBlogTag(blog.getId(), tagId);
        }


        // 获取标签名
        blog.setTags(tagMapper.findTagByBlogId(blog.getId()));
    }

    /**
     * 根据id查询博文以及博文标签
     * 正常状态
     *
     * @param blogId
     * @return
     */
    public Blog findBlogById(Integer blogId, boolean isHistory) throws IOException {

        Blog blog = null;
        blog = blogMapper.findBlogById(blogId);
        if (blog == null) {
            throw new RuntimeException("博客不存在");
        }else{
            blog.setTags(tagMapper.findTagByBlogId(blogId));
        }

        //历史查看过
        if (isHistory) {
            // 直接返回 浏览量不增加
            return blog;
        } else {
            // 浏览量 + 1
            blog.setBlogViews(blog.getBlogViews() + 1);
            blogMapper.updateBlog(blog);
        }
        return blog;
    }


    /**
     * 根据用户查询博文以及标签
     * 正常状态
     *
     * @param page      页数
     * @param showCount 显示数量
     * @return
     */
    public List<Blog> findBlogByUser(Integer page, Integer showCount) {


        User user = userMapper.findUserByName(jsonWebTokenUtil.getUsernameFromRequest(request));
        List<Blog> blogs = blogMapper.findBlogByUserId(user.getId(), (page - 1) * showCount, showCount);

        for (Blog blog : blogs) {
            blog.setTags(tagMapper.findTagByBlogId(blog.getId()));
        }
        return blogs;
    }

    /**
     * 查询该用户的博客数量
     * 正常状态
     *
     * @return
     */
    public Long getBlogCountByUser() {
        User user = userMapper.findUserByName(jsonWebTokenUtil.getUsernameFromRequest(request));
        return blogMapper.getBlogCountByUserId(user.getId());
    }

    /**
     * 查询主页所有博客数量
     * 正常状态
     *
     * @return
     */
    public Long getHomeBlogCount() {
        return blogMapper.getHomeBlogCount();
    }

    /**
     * 查询主页博客
     * 正常状态
     *
     * @param page      页码
     * @param showCount 显示条数
     * @return
     */
    public List<Blog> findHomeBlog(Integer page, Integer showCount) throws IOException {

        // mysql 分页中的开始位置
        int start = (page - 1) * showCount;


        // 返回的blog列表
        List<Blog> blogs = new LinkedList<>();

        // /1/5     limit 1,5 1+5=6
        // /5/5     limit 5,5 5+5=10
        //          limit 10,10 10+10=20
        //          limit 6,5 6+5=11

        blogs.addAll(blogMapper.findHomeBlog(start, showCount));
        for (Blog blog : blogs) {
            blog.setTags(tagMapper.findTagByBlogId(blog.getId()));
        }
        return blogs;
    }

    /**
     * 查询热门博文
     * 正常状态
     *
     * @return
     */
    public List<Blog> findHotBlog() throws IOException {
            return blogMapper.findHotBlog(6);
    }

    /**
     * 搜索博文
     * 正常状态
     *
     * @param searchText
     * @return
     */
    public List<Blog> searchBlog(String searchText, Integer page, Integer showCount) {
        List<Blog> blogs = blogMapper.searchBlog(searchText, (page - 1) * showCount, showCount);
        for (Blog blog : blogs) {
            blog.setTags(tagMapper.findTagByBlogId(blog.getId()));
        }
        return blogs;
    }

    /**
     * 符合关键词的博文数量
     * 正常状态
     *
     * @param searchText
     * @return
     */
    public Long getSearchBlogCount(String searchText) {
        return blogMapper.getSearchBlogCount(searchText);
    }

    /**
     * 查询所有博文
     * 正常状态
     *
     * @return
     */
    public List<Blog> findAllBlog(Integer page, Integer showCount) {
        return blogMapper.findAllblog((page - 1) * showCount, showCount);
    }

    /**
     * 修改博文
     *
     * @param blogId
     * @param blogTitle
     * @param blogBody
     * @param tagIds
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateBlog(Integer blogId, String blogTitle, String blogBody, Integer[] tagIds) throws JsonProcessingException {
        User user = userMapper.findUserByName(jsonWebTokenUtil.getUsernameFromRequest(request));
        Blog blog = blogMapper.findBlogById(blogId);
        if (!user.getId().equals(blog.getUser().getId())) {
            throw new RuntimeException("无权限修改");
        }


        blog.setTitle(blogTitle);
        blog.setBody(blogBody);
        blogMapper.updateBlog(blog);
        //删除原有标签
        tagMapper.deleteTagByBlogId(blog.getId());
        //保存新标签
        for (Integer tagId : tagIds) {
            //保存该博文的标签
            blogMapper.saveBlogTag(blog.getId(), tagId);
        }

            blog.setTags(tagMapper.findTagByBlogId(blogId));
    }

    /**
     * 用户删除博文
     *
     * @param blogId
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteBlog(Integer blogId) throws JsonProcessingException {
        User user = userMapper.findUserByName(jsonWebTokenUtil.getUsernameFromRequest(request));
        Blog blog = blogMapper.findBlogById(blogId);

        if (!user.getId().equals(blog.getUser().getId())) {
            throw new RuntimeException("无权限删除");
        }


        //更改博客状态
        blog.setState(0);
        blogMapper.updateBlog(blog);

        //级联删除blog_tag
        tagMapper.deleteTagByBlogId(blogId);

    }

    /**
     * 管理员删除博文
     *
     * @param blogId
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminDeleteBlog(Integer blogId) throws JsonProcessingException {

        Blog blog = new Blog();
        blog.setId(blogId);
        blog.setState(0);
        //更改博客状态
        blogMapper.updateBlog(blog);
        //级联删除blog_tag
        tagMapper.deleteTagByBlogId(blogId);

    }

    //存在业务冲突，弃用此方法
//    /**
//     * 撤销删除博文
//     *
//     * @param blogId
//     */
//    public void undoDelete(Integer blogId) {
//        blogDao.updateBlogState(blogId, 1);
//    }


    /**
     * 符合关键字的博文数量
     * 所有状态
     *
     * @param searchText
     * @return
     */
    public Long getSearchAllBlogCount(String searchText) {
        return blogMapper.getSearchAllBlogCount(searchText);
    }

    /**
     * 搜索博文
     * 所有状态
     *
     * @param searchText 搜索内容
     * @param page
     * @param showCount
     * @return
     */
    public List<Blog> searchAllBlog(String searchText, Integer page, Integer showCount) {
        List<Blog> blogs = blogMapper.searchAllBlog(searchText, (page - 1) * showCount, showCount);
        return blogs;
    }


    /**
     * 按月份归档博客
     * 正常状态
     *
     * @return
     */
    public List<Map> statisticalBlogByMonth() throws IOException {
        List<Map> maps = blogMapper.statisticalBlogByMonth(6);
        return maps;
    }

    /**
     * 查询博客记录数
     * 所有状态
     *
     * @return
     */
    public Long getAllBlogCount() {
        return blogMapper.getAllBlogCount();
    }

    /**
     * @Description: 获取用户点赞数
     * @Param: [blogId]
     * @return: int
     */
    public int getBlogLikeCountByBlogId(Integer blogId) {
        int likeCount;
        String likeCountKey = String.valueOf(blogId);

        likeCount =  blogMapper.getBlogLikeCountByBlogId(blogId);

        return likeCount;
    }



}