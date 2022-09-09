/*
 Navicat Premium Data Transfer

 Source Server         : mysql_master
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 127.0.0.1:3306
 Source Schema         : partridge

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 07/09/2022 19:24:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for actor
-- ----------------------------
DROP TABLE IF EXISTS `actor`;
CREATE TABLE `actor` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `CREATED_TIME` datetime DEFAULT NULL,
  `UPDATED_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=174 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for author
-- ----------------------------
DROP TABLE IF EXISTS `author`;
CREATE TABLE `author` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `CREATED_TIME` datetime DEFAULT NULL,
  `UPDATED_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for ehentai_gallery
-- ----------------------------
DROP TABLE IF EXISTS `ehentai_gallery`;
CREATE TABLE `ehentai_gallery` (
  `GID` bigint NOT NULL COMMENT 'ehentai gallery id',
  `ELE_ID` bigint DEFAULT NULL,
  `TITLE` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `TITLE_JPN` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `CATEGORY` int DEFAULT NULL COMMENT 'gallery分类',
  `UPLOADER` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '上传者',
  `RATING` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '评分',
  `RATING_COUNT` int DEFAULT NULL COMMENT '评分人数',
  `PAGES` int DEFAULT NULL COMMENT '总页数',
  `PREVIEW_PAGE` int DEFAULT NULL COMMENT '预览画廊对应页',
  `TOKEN` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'gtoken',
  `POSTED` datetime DEFAULT NULL COMMENT '上传时间',
  `FAVORITE_COUNT` int DEFAULT NULL COMMENT '喜爱数',
  `CASHED_FLAG` tinyint DEFAULT NULL COMMENT '(0-否;1-是)',
  `DOWNLOAD_FLAG` tinyint DEFAULT NULL COMMENT '(0-否;1-是)',
  `CREATED_TIME` datetime DEFAULT NULL,
  `UPDATED_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`GID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for ele_actor_re
-- ----------------------------
DROP TABLE IF EXISTS `ele_actor_re`;
CREATE TABLE `ele_actor_re` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `ELE_ID` bigint NOT NULL,
  `ACTOR_ID` int NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=907 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for ele_author_re
-- ----------------------------
DROP TABLE IF EXISTS `ele_author_re`;
CREATE TABLE `ele_author_re` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `ELE_ID` bigint NOT NULL,
  `AUTHOR_ID` int NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for ele_file
-- ----------------------------
DROP TABLE IF EXISTS `ele_file`;
CREATE TABLE `ele_file` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `ELE_ID` bigint DEFAULT NULL,
  `NAME` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `TYPE` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `PATH` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `PAGE_NUM` tinyint DEFAULT NULL COMMENT 'ehentai_gallery 对应画廊文件页码',
  `IS_AVAILABLE_FLAG` tinyint DEFAULT NULL COMMENT '启用标志(0-禁用;1-启用)',
  `CREATED_TIME` datetime DEFAULT NULL,
  `UPDATED_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=332 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for ele_org_re
-- ----------------------------
DROP TABLE IF EXISTS `ele_org_re`;
CREATE TABLE `ele_org_re` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `ELE_ID` bigint NOT NULL,
  `ORG_ID` int NOT NULL,
  `RE_TYPE` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=629 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for ele_tag_re
-- ----------------------------
DROP TABLE IF EXISTS `ele_tag_re`;
CREATE TABLE `ele_tag_re` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `ELE_ID` bigint NOT NULL,
  `TAG_ID` int NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2326 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for element
-- ----------------------------
DROP TABLE IF EXISTS `element`;
CREATE TABLE `element` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `TYPE` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `CREATED_TIME` datetime DEFAULT NULL,
  `UPDATED_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=334 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for jav
-- ----------------------------
DROP TABLE IF EXISTS `jav`;
CREATE TABLE `jav` (
  `ELE_ID` int NOT NULL,
  `CODE` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '識別碼',
  `TITLE` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `PUBLISH_DATE` datetime DEFAULT NULL COMMENT '發行日期',
  `LENGTH` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '長度',
  `DIRECTOR` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `SERIES` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `CREATED_TIME` datetime DEFAULT NULL,
  `UPDATED_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ELE_ID`,`CODE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for organization
-- ----------------------------
DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `CREATED_TIME` datetime DEFAULT NULL,
  `UPDATED_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `GROUP_NAME` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `SOURCE` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `CREATED_TIME` datetime DEFAULT NULL,
  `UPDATED_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1677 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
