/*
 Navicat Premium Data Transfer

 Source Server         : wsl2 docker mysql8.0
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 127.0.0.1:3306
 Source Schema         : partridge

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 16/09/2022 17:28:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for actor
-- ----------------------------
DROP TABLE IF EXISTS `actor`;
CREATE TABLE `actor`  (
  `ID` int NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `CREATED_TIME` datetime NULL DEFAULT NULL,
  `UPDATED_TIME` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 174 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for author
-- ----------------------------
DROP TABLE IF EXISTS `author`;
CREATE TABLE `author`  (
  `ID` int NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `CREATED_TIME` datetime NULL DEFAULT NULL,
  `UPDATED_TIME` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ehentai_gallery
-- ----------------------------
DROP TABLE IF EXISTS `ehentai_gallery`;
CREATE TABLE `ehentai_gallery`  (
  `GID` bigint NOT NULL COMMENT 'ehentai gallery id',
  `ELE_ID` bigint NULL DEFAULT NULL,
  `TITLE` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `TITLE_JPN` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `CATEGORY` int NULL DEFAULT NULL COMMENT 'gallery分类',
  `UPLOADER` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上传者',
  `RATING` double NULL DEFAULT NULL COMMENT '评分',
  `RATING_COUNT` int NULL DEFAULT NULL COMMENT '评分人数',
  `PAGES` int NULL DEFAULT NULL COMMENT '总页数',
  `PREVIEW_PAGE` int NULL DEFAULT NULL COMMENT '预览画廊对应页',
  `TOKEN` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'gtoken',
  `POSTED` datetime NULL DEFAULT NULL COMMENT '上传时间',
  `FAVORITE_COUNT` int NULL DEFAULT NULL COMMENT '喜爱数',
  `CASHED_FLAG` tinyint NULL DEFAULT NULL COMMENT '(0-否;1-是)',
  `DOWNLOAD_FLAG` tinyint NULL DEFAULT NULL COMMENT '(0-否;1-是)',
  `CREATED_TIME` datetime NULL DEFAULT NULL,
  `UPDATED_TIME` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`GID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ele_actor_re
-- ----------------------------
DROP TABLE IF EXISTS `ele_actor_re`;
CREATE TABLE `ele_actor_re`  (
  `ID` int NOT NULL AUTO_INCREMENT,
  `ELE_ID` bigint NOT NULL,
  `ACTOR_ID` int NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 907 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ele_author_re
-- ----------------------------
DROP TABLE IF EXISTS `ele_author_re`;
CREATE TABLE `ele_author_re`  (
  `ID` int NOT NULL AUTO_INCREMENT,
  `ELE_ID` bigint NOT NULL,
  `AUTHOR_ID` int NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ele_file
-- ----------------------------
DROP TABLE IF EXISTS `ele_file`;
CREATE TABLE `ele_file`  (
  `ID` int NOT NULL AUTO_INCREMENT,
  `ELE_ID` bigint NULL DEFAULT NULL,
  `NAME` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `TYPE` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `PATH` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `PAGE_NUM` tinyint NULL DEFAULT NULL COMMENT 'ehentai_gallery 对应画廊文件页码',
  `IS_AVAILABLE_FLAG` tinyint NULL DEFAULT NULL COMMENT '启用标志(0-禁用;1-启用)',
  `CREATED_TIME` datetime NULL DEFAULT NULL,
  `UPDATED_TIME` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 405 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ele_org_re
-- ----------------------------
DROP TABLE IF EXISTS `ele_org_re`;
CREATE TABLE `ele_org_re`  (
  `ID` int NOT NULL AUTO_INCREMENT,
  `ELE_ID` bigint NOT NULL,
  `ORG_ID` int NOT NULL,
  `RE_TYPE` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 629 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ele_tag_re
-- ----------------------------
DROP TABLE IF EXISTS `ele_tag_re`;
CREATE TABLE `ele_tag_re`  (
  `ID` int NOT NULL AUTO_INCREMENT,
  `ELE_ID` bigint NOT NULL,
  `TAG_ID` int NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2536 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for element
-- ----------------------------
DROP TABLE IF EXISTS `element`;
CREATE TABLE `element`  (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `TYPE` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `SHARED_FLAG` tinyint NULL DEFAULT NULL COMMENT '(0-否;1-是)',
  `UPLOADER` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上传用户',
  `CREATED_TIME` datetime NULL DEFAULT NULL,
  `UPDATED_TIME` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 539 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '基本元素表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for jav
-- ----------------------------
DROP TABLE IF EXISTS `jav`;
CREATE TABLE `jav`  (
  `ELE_ID` bigint NOT NULL,
  `CODE` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '識別碼',
  `TITLE` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `PUBLISH_DATE` datetime NULL DEFAULT NULL COMMENT '發行日期',
  `LENGTH` int NULL DEFAULT NULL COMMENT '長度',
  `DIRECTOR` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `SERIES` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `CREATED_TIME` datetime NULL DEFAULT NULL,
  `UPDATED_TIME` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`ELE_ID`, `CODE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for organization
-- ----------------------------
DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization`  (
  `ID` int NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `CREATED_TIME` datetime NULL DEFAULT NULL,
  `UPDATED_TIME` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tag_info
-- ----------------------------
DROP TABLE IF EXISTS `tag_info`;
CREATE TABLE `tag_info`  (
  `ID` int NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `GROUP_NAME` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `SOURCE` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `CREATED_TIME` datetime NULL DEFAULT NULL,
  `UPDATED_TIME` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1681 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
