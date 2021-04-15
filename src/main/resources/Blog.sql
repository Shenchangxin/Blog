//博客
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog` (
  `blog_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一博文id--主键',
  `blog_title` varchar(255) NOT NULL COMMENT '博文标题',
  `blog_body` text NOT NULL COMMENT '博文内容',
  `blog_discussCount` int(11) NOT NULL COMMENT '博文评论数',
  `blog_blogViews` int(11) NOT NULL COMMENT '博文浏览数',
  `blog_time` datetime NOT NULL COMMENT '博文发布时间',
  `blog_state` int(11) NOT NULL COMMENT '博文状态--0 删除 1正常',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `like_count` int(11) NOT NULL DEFAULT 0 COMMENT '博文点赞总数',
  PRIMARY KEY (`blog_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

//标签
DROP TABLE IF EXISTS `blog_tag`;
CREATE TABLE `blog_tag` (
  `blog_id` int(11) NOT NULL COMMENT '博文id',
  `tag_id` int(11) NOT NULL COMMENT '标签id',
  UNIQUE KEY `blog_id` (`blog_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

//评论
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
                           `comment_id` int(255) NOT NULL AUTO_INCREMENT COMMENT '评论唯一id',
                           `comment_body` varchar(255) NOT NULL COMMENT '评论内容',
                           `comment_time` datetime NOT NULL COMMENT '评论时间',
                           `user_id` int(11) NOT NULL COMMENT '用户id',
                           `blog_id` int(11) NOT NULL COMMENT '博文id',
                           PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

回复
DROP TABLE IF EXISTS `reply`;
CREATE TABLE `reply` (
  `reply_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '回复唯一id',
  `reply_body` varchar(255) NOT NULL COMMENT '回复内容',
  `reply_time` datetime NOT NULL COMMENT '回复时间',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `discuss_id` int(11) NOT NULL COMMENT '评论id',
  `reply_rootid` int(11) DEFAULT NULL COMMENT '父回复节点id',
  PRIMARY KEY (`reply_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

//角色
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(30) NOT NULL COMMENT '角色名',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

//插入角色
INSERT INTO `role` VALUES ('1', 'ROLE_USER');
INSERT INTO `role` VALUES ('2', 'ROLE_ADMIN');


//标签
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `tag_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一标签id--主键',
  `tag_name` varchar(20) NOT NULL COMMENT '标签名',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`tag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

//用户
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户唯一id--主键',
  `user_name` varchar(30) NOT NULL COMMENT '用户名--不能重复',
  `user_password` varchar(255) NOT NULL COMMENT '用户密码',
  `user_mail` varchar(50) NOT NULL COMMENT '用户邮箱',
  `user_state` int(11) NOT NULL COMMENT '用户状态 0 封禁 1正常',
  `user_reward` varchar(255) DEFAULT NULL COMMENT '用户打赏码图片路径',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

//插入用户名为“admin”，密码为“1”，角色为“ROLE_USER、ROLE_ADMIN”的初始用户
INSERT INTO `user` VALUES ('1', 'admin', '$2a$10$usmASSUxqidbn2RrQi4jdeVWUcFyTfmwZgTxSy8FIXQ5CVpm/0qEa', 'xxxxx@xxxx.com', '1', 'null');


//点赞
DROP TABLE IF EXISTS `vote`;
CREATE TABLE `vote`  (
                              `id` int(11) NOT NULL AUTO_INCREMENT,
                              `user_id` int(11) NOT NULL COMMENT '点赞的用户id',
                              `blog_id` int(11) NOT NULL COMMENT '被点赞的博文id',
                              `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '点赞状态，0取消，1点赞',
                              `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                              `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
                              PRIMARY KEY (`id`) USING BTREE,
                              UNIQUE INDEX `ul1`(`user_id`, `blog_id`) USING BTREE,
                              UNIQUE INDEX `ul2`(`blog_id`, `user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 131 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户点赞表' ROW_FORMAT = Dynamic;


//用户表与角色表之间的关联表
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`user_role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

//初始化admin用户的角色
INSERT INTO `user_role` VALUES ('1', '1', '1');
INSERT INTO `user_role` VALUES ('2', '1', '2');
