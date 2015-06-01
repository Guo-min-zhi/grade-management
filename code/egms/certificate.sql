/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50506
Source Host           : localhost:3306
Source Database       : certificate

Target Server Type    : MYSQL
Target Server Version : 50506
File Encoding         : 65001

Date: 2014-06-23 12:37:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `certificate_score`
-- ----------------------------
DROP TABLE IF EXISTS `certificate_score`;
CREATE TABLE `certificate_score` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `basic_info_id` bigint(11) DEFAULT NULL,
  `certificate_type_id` int(11) DEFAULT NULL,
  `source_score` float DEFAULT NULL,
  `translated_score` float DEFAULT NULL,
  `certificate_photo_url` varchar(100) DEFAULT NULL,
  `certificate_acquire_time` date DEFAULT NULL,
  `check_website` varchar(100) DEFAULT NULL,
  `check_website_screenshot` varchar(100) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `verify_times` int(11) DEFAULT NULL,
  `submit_time` timestamp NULL DEFAULT NULL,
  `verify_teacher_a` bigint(11) DEFAULT NULL,
  `verify_time_a` timestamp NULL DEFAULT NULL,
  `comment_a` varchar(100) DEFAULT NULL,
  `verify_teacher_b` bigint(11) DEFAULT NULL,
  `verify_time_b` timestamp NULL DEFAULT NULL,
  `comment_b` varchar(100) DEFAULT NULL,
  `verify_status_a` tinyint(4) DEFAULT NULL,
  `verify_status_b` tinyint(4) DEFAULT NULL,
  `grade_a` float DEFAULT NULL,
  `grade_b` float DEFAULT NULL,
  `grade_c` float DEFAULT NULL,
  `grade_final` varchar(10) DEFAULT NULL,
  `oral_score` float DEFAULT NULL,
  `written_score` float DEFAULT NULL,
  `grade_status` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of certificate_score
-- ----------------------------

-- ----------------------------
-- Table structure for `certificate_type`
-- ----------------------------
DROP TABLE IF EXISTS `certificate_type`;
CREATE TABLE `certificate_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `certificate_name` varchar(25) DEFAULT NULL,
  `formula` varchar(300) DEFAULT NULL,
  `upload_type` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of certificate_type
-- ----------------------------
INSERT INTO `certificate_type` VALUES ('1', '托福', '(110<=x && x<=120)?100:((80 <= x && x <= 110)?(0.0190476 * (x pow 2) - 2.4476 * x + 138.4286):0)', '1');
INSERT INTO `certificate_type` VALUES ('4', 'GMAT', '(750 <= x && x <= 800)?100:((550 <= x && x< 750)?(1/8 * x + 25/4):0)', '1');
INSERT INTO `certificate_type` VALUES ('6', 'GRE', '(1500 <= x && x <= 1600)?100:(1100 <= x  && x < 1500?(1/16 * x + 25/4):(x == 1000)?70:0)', '1');
INSERT INTO `certificate_type` VALUES ('7', '雅思', 'x==(9.0||8.5||8.0)?100:(x==7.5?95:(x==7.0?90:(x==6.5?80:(x==6.0?70:0))))', '1');
INSERT INTO `certificate_type` VALUES ('8', '国家四级', '0.003008 * ((x/10) pow 3) - 0.5661 * ((x/10) pow 2 ) + 35.8 * (x/10) - 669.33', '0');
INSERT INTO `certificate_type` VALUES ('9', '国家六级', '0.00439 * ((x/10) pow 3) - 0.8017 * ((x/10) pow 2) + 48.95 * (x/10) - 904.91', '0');
INSERT INTO `certificate_type` VALUES ('10', '全校性英语考试成绩', 'x1 * 0.2 + x2 * 0.2 + x3 * 0.2 + x4 * 0.25 * 0.4 + x5 * 0.75 * 0.4', '0');
INSERT INTO `certificate_type` VALUES ('11', '口语班考试成绩', 'x1 * 0.2 + x2 * 0.3 + x3 * 0.1 +x3 * 0.25 * 0.4 + x5 * 0.75 * 0.4', '0');
INSERT INTO `certificate_type` VALUES ('12', '软件国际班成绩', 'x1 * 0.2 + x2 * 0.3 + x3 * 0.1 +x3 * 0.25 * 0.4 + x5 * 0.75 * 0.4', '0');
INSERT INTO `certificate_type` VALUES ('13', '机电国际班成绩', 'x1 * 0.3 + x2 * 0.3 + x4 * 0.25 * 0.4 + x5 * 0.75 * 0.4', '0');

-- ----------------------------
-- Table structure for `role_type`
-- ----------------------------
DROP TABLE IF EXISTS `role_type`;
CREATE TABLE `role_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(30) DEFAULT NULL,
  `role_code` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_type
-- ----------------------------
INSERT INTO `role_type` VALUES ('1', '学生', 'student');
INSERT INTO `role_type` VALUES ('2', '老师', 'teacher');
INSERT INTO `role_type` VALUES ('3', '管理员', 'admin');

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
-- Records of user_admin
-- ----------------------------
INSERT INTO `user_admin` VALUES ('1', 'admin', 'e10adc3949ba59abbe56e057f20f883e', null, '1');

-- ----------------------------
-- Table structure for `user_student`
-- ----------------------------
DROP TABLE IF EXISTS `user_student`;
CREATE TABLE `user_student` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(13) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `name` varchar(25) DEFAULT NULL,
  `gender` tinyint(4) DEFAULT NULL,
  `college` varchar(50) DEFAULT NULL,
  `major` varchar(50) DEFAULT NULL,
  `class_num` varchar(25) DEFAULT NULL,
  `phone_num` char(11) DEFAULT NULL,
  `photo` varchar(100) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `identity_num` varchar(18) DEFAULT NULL,
  `grade` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_student
-- ----------------------------

-- ----------------------------
-- Table structure for `user_teacher`
-- ----------------------------
DROP TABLE IF EXISTS `user_teacher`;
CREATE TABLE `user_teacher` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(13) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `name` varchar(25) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_teacher
-- ----------------------------
