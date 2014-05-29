/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50615
Source Host           : localhost:3306
Source Database       : certificate

Target Server Type    : MYSQL
Target Server Version : 50615
File Encoding         : 65001

Date: 2014-05-29 15:40:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `certificate_score`
-- ----------------------------
DROP TABLE IF EXISTS `certificate_score`;
CREATE TABLE `certificate_score` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `basic_info_id` bigint(11) DEFAULT NULL COMMENT '[外键]学生ID',
  `certificate_type_id` int(11) DEFAULT NULL COMMENT '[外键]证书类型ID',
  `source_score` float DEFAULT NULL COMMENT '原成绩',
  `translated_score` float DEFAULT NULL COMMENT '转换后的成绩',
  `certificate_photo_url` varchar(100) DEFAULT NULL COMMENT '证书照片URL',
  `certificate_acquire_time` date DEFAULT NULL COMMENT '证书获得时间',
  `check_website` varchar(100) DEFAULT NULL COMMENT '证书验真网址',
  `check_website_screenshot` varchar(100) DEFAULT NULL COMMENT '证书验真网址截图URL',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `verify_times` int(11) DEFAULT NULL COMMENT '审核次数',
  `submit_time` timestamp NULL DEFAULT NULL COMMENT '提交时间',
  `verify_teacher_a` bigint(11) DEFAULT NULL COMMENT '第一次的审核老师ID',
  `verify_time_a` timestamp NULL DEFAULT NULL COMMENT '审核时间',
  `comment_a` varchar(100) DEFAULT NULL COMMENT '第一次审核的意见',
  `verify_teacher_b` bigint(11) DEFAULT NULL COMMENT '第二次审核老师ID',
  `verify_time_b` timestamp NULL DEFAULT NULL COMMENT '第二次审核的时间',
  `comment_b` varchar(100) DEFAULT NULL COMMENT '第二次审核的意见',
  `verify_status_a` tinyint(4) DEFAULT NULL COMMENT '第一次审核状态',
  `verify_status_b` tinyint(4) DEFAULT NULL COMMENT '第二次审核状态',
  `grade_a` float DEFAULT NULL COMMENT '第一学期成绩',
  `grade_b` float DEFAULT NULL COMMENT '第二学期成绩',
  `grade_c` float DEFAULT NULL COMMENT '第三学期成绩',
  `grade_final` float DEFAULT NULL COMMENT '综合成绩',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=138 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `certificate_type`
-- ----------------------------
DROP TABLE IF EXISTS `certificate_type`;
CREATE TABLE `certificate_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `certificate_name` varchar(25) DEFAULT NULL COMMENT '证书名称',
  `formula` varchar(300) DEFAULT NULL COMMENT '成绩换算公式',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `role_type`
-- ----------------------------
DROP TABLE IF EXISTS `role_type`;
CREATE TABLE `role_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_name` varchar(30) DEFAULT NULL COMMENT '角色名称',
  `role_code` varchar(30) DEFAULT NULL COMMENT '角色编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `user_admin`
-- ----------------------------
DROP TABLE IF EXISTS `user_admin`;
CREATE TABLE `user_admin` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(13) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(25) COLLATE utf8_bin DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for `user_student`
-- ----------------------------
DROP TABLE IF EXISTS `user_student`;
CREATE TABLE `user_student` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `login_name` varchar(13) DEFAULT NULL COMMENT '系统登录用户名',
  `password` varchar(32) DEFAULT NULL COMMENT '系统登录密码',
  `role_id` int(11) DEFAULT NULL COMMENT '[外键]角色编码',
  `name` varchar(25) DEFAULT NULL COMMENT '真实姓名',
  `gender` tinyint(4) DEFAULT NULL COMMENT '性别',
  `college` varchar(50) DEFAULT NULL COMMENT '大学',
  `major` varchar(50) DEFAULT NULL COMMENT '专业',
  `class_num` varchar(25) DEFAULT NULL COMMENT '班级',
  `phone_num` char(11) DEFAULT NULL COMMENT '电话号码',
  `photo` varchar(100) DEFAULT NULL COMMENT '头像路径',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `identity_num` varchar(18) DEFAULT NULL COMMENT '身份证号码',
  `grade` varchar(20) DEFAULT NULL COMMENT '年级',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=616 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `user_teacher`
-- ----------------------------
DROP TABLE IF EXISTS `user_teacher`;
CREATE TABLE `user_teacher` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `login_name` varchar(13) DEFAULT NULL COMMENT '系统登录用户名',
  `password` varchar(32) DEFAULT NULL COMMENT '系统登录密码',
  `name` varchar(25) DEFAULT NULL COMMENT '真实姓名',
  `role_id` int(11) DEFAULT NULL COMMENT '[外键]角色编码',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
