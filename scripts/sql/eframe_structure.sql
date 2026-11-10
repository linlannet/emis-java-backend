/*

 Source Server         : LLDB
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : eframedb_1.0

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 24/08/2025 15:40:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ADMIN_DEPT
-- ----------------------------
DROP TABLE IF EXISTS `ADMIN_DEPT`;
CREATE TABLE `ADMIN_DEPT`  (
                               `DEPT_ID` bigint(0) NOT NULL COMMENT '单位ID',
                               `ORGAN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '系统机构ID',
                               `PARENT_ID` bigint(0) NULL DEFAULT NULL COMMENT '父节点',
                               `APP_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用ID',
                               `DEPTTYPE_ID` bigint(0) NULL DEFAULT NULL COMMENT '部门分类ID',
                               `FOREIGN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外部业务ID',
                               `CREATE_TYPE` int(0) NULL DEFAULT 1 COMMENT '创建方式0导入1录入2授权',
                               `TYPE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '部门类型，NEISHE内设机构，FENZI分子机构',
                               `NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称，多级累加名称',
                               `SNAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称简称，本单位显示名称',
                               `AREA_CODE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地域编码，所在城市',
                               `ORGAN_LEADER` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '领导名称',
                               `LEADER_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '负责人ID，用户接收相关信息',
                               `LEADER_MOBILE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '负责人手机号码，用户接收相关信息',
                               `IS_ORGAN_LEVEL` tinyint(1) NULL DEFAULT 1 COMMENT '是否机构本级1是0否默认1',
                               `IS_VIRTUAL` tinyint(1) NULL DEFAULT 0 COMMENT '是否虚拟节点1是0不是默认0',
                               `IS_LEAF` tinyint(1) NULL DEFAULT 0 COMMENT '是否叶子节点',
                               `IS_DISABLED` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否禁用0否1是',
                               `SEARCH_CODE` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '多级累加路径',
                               `IMAGE_PATH` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片路径',
                               `PRIORITY` int(0) NOT NULL DEFAULT 10 COMMENT '排序',
                               `STATUS` int(0) NULL DEFAULT NULL COMMENT '状态0未生效1正常2受限3锁定',
                               `CREATOR_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建用户ID',
                               `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                               `LAST_TIME` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                               `DEL_FLAG` int(0) NULL DEFAULT NULL COMMENT '是否删除1已删除0正常',
                               `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
                               `SPARE1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1',
                               `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2',
                               PRIMARY KEY (`DEPT_ID`) USING BTREE,
                               INDEX `IDX_ADMIN_DEPT_PARENT_ID`(`PARENT_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统机构部门' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ADMIN_DEPT_POSITION
-- ----------------------------
DROP TABLE IF EXISTS `ADMIN_DEPT_POSITION`;
CREATE TABLE `ADMIN_DEPT_POSITION`  (
                                        `DEPTPOS_ID` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '用户角色ID',
                                        `DEPT_ID` bigint(0) NULL DEFAULT NULL COMMENT '单位ID',
                                        `POSITION_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '岗位ID',
                                        `IS_DEF` tinyint(1) NULL DEFAULT 0 COMMENT '是否默认部门0否1是',
                                        `DEL_FLAG` int(0) NULL DEFAULT NULL COMMENT '是否删除1已删除0正常',
                                        `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                        PRIMARY KEY (`DEPTPOS_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色关联' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ADMIN_DEPT_ROLE
-- ----------------------------
DROP TABLE IF EXISTS `ADMIN_DEPT_ROLE`;
CREATE TABLE `ADMIN_DEPT_ROLE`  (
                                    `DEPTROLE_ID` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '角色功能ID',
                                    `ROLE_ID` bigint(0) NULL DEFAULT NULL COMMENT '角色ID',
                                    `DEPT_ID` bigint(0) NULL DEFAULT NULL COMMENT '部门ID',
                                    PRIMARY KEY (`DEPTROLE_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色部门关联' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ADMIN_LOGIN_LOG
-- ----------------------------
DROP TABLE IF EXISTS `ADMIN_LOGIN_LOG`;
CREATE TABLE `ADMIN_LOGIN_LOG`  (
                                    `LOG_ID` bigint(0) NOT NULL COMMENT '登录ID',
                                    `ADMIN_ID` bigint(0) NULL DEFAULT NULL COMMENT '管理员ID',
                                    `USER_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户ID或用户名',
                                    `APP_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用ID',
                                    `CATEGORY` int(0) NULL DEFAULT NULL COMMENT '日志类型，1登录2登出',
                                    `TITLE` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '日志标题',
                                    `CONTENT` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '日志内容',
                                    `SRC_CODE` int(0) NULL DEFAULT NULL COMMENT '当前登录来源',
                                    `LOG_TIME` datetime(0) NULL DEFAULT NULL COMMENT '登录时间',
                                    `LOG_IP` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录IP',
                                    `LOG_URL` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访问的路径地址',
                                    `LOG_BROWSE` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访问的浏览器',
                                    `LOG_OS` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访问的操作系统',
                                    `STATUS` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态，1成功，0失败',
                                    `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
                                    PRIMARY KEY (`LOG_ID`) USING BTREE,
                                    INDEX `IDX_ADMIN_LOGIN_LOG_A_ID`(`ADMIN_ID`) USING BTREE,
                                    INDEX `IDX_ADMIN_LOGIN_LOG_L_TIME`(`LOG_TIME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员登录日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ADMIN_USER
-- ----------------------------
DROP TABLE IF EXISTS `ADMIN_USER`;
CREATE TABLE `ADMIN_USER`  (
                               `ADMIN_ID` bigint(0) NOT NULL COMMENT '企业管理用户ID',
                               `USER_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '系统管理用户ID',
                               `ORGAN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '机构ID',
                               `DEPT_ID` bigint(0) NULL DEFAULT NULL COMMENT '单位ID',
                               `CREATE_TYPE` int(0) NULL DEFAULT 1 COMMENT '创建方式0导入1录入2授权',
                               `NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '显示名称，可设置为用户名、手机号、昵称、真名',
                               `USERNAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
                               `MOBILE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号码',
                               `EMAIL` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电子邮箱',
                               `PASSWORD` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
                               `SALT` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '混淆字符',
                               `IMAGE_PATH` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
                               `ADMIN_TYPE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '管理类型：ADMIN管理员，DEMO演示用户',
                               `IS_SUPER_ADMIN` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否超级管理员',
                               `IS_AUDIT_ADMIN` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否审计只读管理员',
                               `IS_SELF_ADMIN` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否只管理自己的数据',
                               `ADMIN_RANGE` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '管理的范围配置JSON，APP_ID或者SITE_ID的数组，或者单个APP_ID',
                               `ADMIN_PERMS` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '管理权限JSON',
                               `LAST_APP_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最后登录的应用ID',
                               `LAST_LOGIN_TIME` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
                               `LAST_LOGIN_IP` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '127.0.0.1' COMMENT '最后登录IP',
                               `LOGIN_COUNT` int(0) NOT NULL DEFAULT 0 COMMENT '登录次数',
                               `STATUS` int(0) NOT NULL DEFAULT 1 COMMENT '状态0未生效1正常2受限3锁定',
                               `CREATOR_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人ID',
                               `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                               `LAST_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '最后时间',
                               `DEL_FLAG` int(0) NULL DEFAULT 0 COMMENT '删除标记0正常1已删除2应用内受限3回收站',
                               `SPARE1` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1',
                               `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2',
                               PRIMARY KEY (`ADMIN_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ADMIN_USER_POSITION
-- ----------------------------
DROP TABLE IF EXISTS `ADMIN_USER_POSITION`;
CREATE TABLE `ADMIN_USER_POSITION`  (
                                        `USERPOS_ID` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '用户岗位ID',
                                        `POSITION_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '岗位ID',
                                        `IS_MEMBER` tinyint(1) NULL DEFAULT 0 COMMENT '是否会员0否1是',
                                        `TYPE` int(0) NULL DEFAULT NULL COMMENT '类型，1负责人2书记11一般21文秘',
                                        `ADMIN_ID` bigint(0) NULL DEFAULT NULL COMMENT '管理用户ID',
                                        `IS_DEF` tinyint(1) NULL DEFAULT 0 COMMENT '是否主岗位0否1是',
                                        `DEL_FLAG` int(0) NULL DEFAULT NULL COMMENT '是否删除1已删除0正常',
                                        `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                        PRIMARY KEY (`USERPOS_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户岗位关联' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ADMIN_USER_ROLE
-- ----------------------------
DROP TABLE IF EXISTS `ADMIN_USER_ROLE`;
CREATE TABLE `ADMIN_USER_ROLE`  (
                                    `USERROLE_ID` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '用户角色ID',
                                    `ROLE_ID` bigint(0) NULL DEFAULT NULL COMMENT '角色ID',
                                    `ADMIN_ID` bigint(0) NULL DEFAULT NULL COMMENT '公共管理用户ID',
                                    PRIMARY KEY (`USERROLE_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色关联' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for BASE_APP
-- ----------------------------
DROP TABLE IF EXISTS `BASE_APP`;
CREATE TABLE `BASE_APP`  (
                             `APP_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用ID',
                             `FTP_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件上传配置ID',
                             `DOMAIN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '域平台编号',
                             `ORGAN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位编码，所属单位，管理单位',
                             `XZQH_ID` bigint(0) NULL DEFAULT NULL COMMENT '应用适用顶级行政区划ID',
                             `SERTYPE_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用服务类型ID',
                             `NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用名称',
                             `PATH` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用访问路径',
                             `APP_TYPE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用类型，QUANBU全部领域，WORK工作端，H5移动端，SCREEN大屏端；B/S，C/S，独立应用，可多选',
                             `IS_SITE` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否站点，如果是站点，则SITE内存在记录',
                             `IS_DISABLED` tinyint(1) NULL DEFAULT 0 COMMENT '是否禁用0否1是，单点登录托管型为0',
                             `IS_MOBILE_APP` tinyint(1) NULL DEFAULT 0 COMMENT '是否为复杂权限应用0否（一般应用APP应用）1是',
                             `STATUS` int(0) NOT NULL DEFAULT 1 COMMENT '状态0异常关闭1正常2升级3锁定',
                             `PRIORITY` int(0) NOT NULL DEFAULT 10 COMMENT '排序',
                             `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `LAST_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '最后时间',
                             `DEL_FLAG` int(0) NULL DEFAULT 0 COMMENT '删除标记0正常1已删除2应用内受限3回收站',
                             `DELETE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
                             `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
                             `SPARE1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1，LOGO图标',
                             `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2，应用创建来源，如果为单点登录托管型，则保存SSO_TUOGUAN',
                             PRIMARY KEY (`APP_ID`) USING BTREE,
                             INDEX `IDX_BASE_APP_PRI`(`PRIORITY`) USING BTREE,
                             INDEX `IDX_BASE_APP_CTIME`(`CREATE_TIME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '应用信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for BASE_AREATYPE
-- ----------------------------
DROP TABLE IF EXISTS `BASE_AREATYPE`;
CREATE TABLE `BASE_AREATYPE`  (
                                  `AREATYPE_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '地域ID',
                                  `NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '地域名称',
                                  `SNAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地域名称，简称，英文',
                                  `PRIORITY` int(0) NOT NULL DEFAULT 10 COMMENT '排序',
                                  `IS_DISABLED` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否禁用0否1是',
                                  `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                  `LAST_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '最后时间',
                                  `DEL_FLAG` int(0) NULL DEFAULT 0 COMMENT '删除标记0正常1已删除2应用内受限3回收站',
                                  `DELETE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
                                  `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
                                  `SPARE1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1',
                                  `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2',
                                  PRIMARY KEY (`AREATYPE_ID`) USING BTREE,
                                  INDEX `IDX_BASE_AREATYPE_PRI`(`PRIORITY`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '地域类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for BASE_CONFIG_PARTS
-- ----------------------------
DROP TABLE IF EXISTS `BASE_CONFIG_PARTS`;
CREATE TABLE `BASE_CONFIG_PARTS`  (
                                      `CFG_KEY` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置项名称',
                                      `CFG_VALUE` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置项值',
                                      PRIMARY KEY (`CFG_KEY`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通用配置分子项' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for BASE_CONFIG_WHOLE
-- ----------------------------
DROP TABLE IF EXISTS `BASE_CONFIG_WHOLE`;
CREATE TABLE `BASE_CONFIG_WHOLE`  (
                                      `CFG_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置编号',
                                      `CFG_KEY` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '配置项名称，站点ID，地域ID',
                                      `CFG_VALUE` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '配置项值，JSON格式，包括缺省图片，定时任务，TRUE/FALSE，0/1',
                                      `CFG_CATAGORY` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类标记',
                                      `IS_DISABLED` tinyint(1) NULL DEFAULT NULL COMMENT '是否禁用0否1是',
                                      `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
                                      PRIMARY KEY (`CFG_ID`) USING BTREE,
                                      INDEX `IDX_BASE_CONFIG_WHOLE_KEY`(`CFG_KEY`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通用配置合项' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for BASE_DICTIONARY
-- ----------------------------
DROP TABLE IF EXISTS `BASE_DICTIONARY`;
CREATE TABLE `BASE_DICTIONARY`  (
                                    `CDD_ID` bigint(0) NOT NULL COMMENT '字典编号',
                                    `TYPE_CODE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典类型代码',
                                    `PARENT_ID` bigint(0) NULL DEFAULT NULL COMMENT '字典父节点编号，简单的字典使用',
                                    `NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典名称',
                                    `SNAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典名称简称，英文，多语种名称',
                                    `CDD_VALUE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典值',
                                    `CDD_LEVEL` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典级别, tree时有效',
                                    `SEARCH_CODE` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '快速码',
                                    `DISP_FLAG` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '显示标记，用|分隔，第1位全部第2位重点第3位特殊分类；如1|1|0',
                                    `PRIORITY` int(0) NULL DEFAULT NULL COMMENT '排序码',
                                    `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                    `LAST_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '最后时间',
                                    `DEL_FLAG` int(0) NULL DEFAULT 0 COMMENT '删除标记0正常1已删除2应用内受限3回收站',
                                    `DELETE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
                                    `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
                                    `SPARE1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1',
                                    `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2',
                                    PRIMARY KEY (`CDD_ID`) USING BTREE,
                                    INDEX `IDX_BASE_DICTIONARY_TCODE`(`TYPE_CODE`) USING BTREE,
                                    INDEX `IDX_BASE_DICTIONARY_SCODE`(`SEARCH_CODE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '公共字典明细' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for BASE_DIC_TYPE
-- ----------------------------
DROP TABLE IF EXISTS `BASE_DIC_TYPE`;
CREATE TABLE `BASE_DIC_TYPE`  (
                                  `TYPE_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典类型代码',
                                  `NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典类型名称',
                                  `TYPE_CLASS` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典类型分类，GB国家标准，HB行业标准，ZD自定义标准',
                                  `TYPE_REF` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典引用名称',
                                  `TYPE_MODE` int(0) NULL DEFAULT 0 COMMENT '字典类型模式0平面1树形',
                                  `SEARCH_CODE` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '快速码',
                                  `PRIORITY` int(0) NULL DEFAULT NULL COMMENT '排序码',
                                  `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                  `LAST_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '最后时间',
                                  `STATUS` int(0) NULL DEFAULT NULL COMMENT '字典类型修改状态',
                                  `DEL_FLAG` int(0) NULL DEFAULT 0 COMMENT '删除标记0正常1已删除2应用内受限3回收站',
                                  `DELETE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
                                  `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典类型说明',
                                  `SPARE1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1',
                                  `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2',
                                  PRIMARY KEY (`TYPE_ID`) USING BTREE,
                                  INDEX `IDX_BASE_DIC_TYPE_SEARCH`(`SEARCH_CODE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for BASE_LABEL
-- ----------------------------
DROP TABLE IF EXISTS `BASE_LABEL`;
CREATE TABLE `BASE_LABEL`  (
                               `LABEL_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签ID',
                               `NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名称',
                               `ATTR` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签属性，归类说明，搜索词标记，可以为标签分类中的关键类型，标签可扩展表、字段应用，如行政区划标签可关联行政区划表',
                               `REF_COUNT` int(0) NOT NULL DEFAULT 1 COMMENT '被引用的次数',
                               `ACCESS_COUNT` int(0) NULL DEFAULT NULL COMMENT '标签访问次数',
                               `STATUS` int(0) NULL DEFAULT NULL COMMENT '标签状态',
                               `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                               `LAST_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '最后时间',
                               `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签描述',
                               PRIMARY KEY (`LABEL_ID`) USING BTREE,
                               UNIQUE INDEX `IDX_BASE_LABEL_NAME`(`NAME`) USING BTREE,
                               INDEX `IDX_BASE_LABEL_RCOUNT`(`REF_COUNT`) USING BTREE,
                               INDEX `IDX_BASE_LABEL_ACOUNT`(`ACCESS_COUNT`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '全局标签字词' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for BASE_ORGAN
-- ----------------------------
DROP TABLE IF EXISTS `BASE_ORGAN`;
CREATE TABLE `BASE_ORGAN`  (
                               `ORGAN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '单位编码',
                               `XZQH_ID` bigint(0) NULL DEFAULT NULL COMMENT '单位所在地行政区划ID，区县级',
                               `NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
                               `SOURCE` int(0) NOT NULL COMMENT '创建来源类型0后台，1前台，结合SrcCode的编码，进行机构属性扩展',
                               `UNION_CODE` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '统一信用代码',
                               `ORGAN_CODE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组织机构代码，地域类机构的地域ID',
                               `CREATE_TYPE` int(0) NULL DEFAULT 1 COMMENT '创建方式0导入1录入2授权，1为唯一标记，2可能为重复的数据',
                               `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                               `LAST_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '最后时间',
                               `PRIORITY` int(0) NULL DEFAULT 10 COMMENT '排序',
                               `DEL_FLAG` int(0) NULL DEFAULT 0 COMMENT '删除标记0正常1已删除2应用内受限3回收站',
                               `DELETE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
                               `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
                               `SPARE1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1，地域类机构的地域CODE，本地编码',
                               `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2，地域类机构外部ID',
                               PRIMARY KEY (`ORGAN_ID`) USING BTREE,
                               INDEX `IDX_BASE_ORGAN_UCODE`(`UNION_CODE`) USING BTREE,
                               INDEX `IDX_BASE_ORGAN_OCODE`(`ORGAN_CODE`) USING BTREE,
                               INDEX `IDX_BASE_ORGAN_PRI`(`PRIORITY`) USING BTREE,
                               INDEX `IDX_BASE_ORGAN_SP2`(`SPARE2`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '基础单位' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for BASE_ORGAN_EXT
-- ----------------------------
DROP TABLE IF EXISTS `BASE_ORGAN_EXT`;
CREATE TABLE `BASE_ORGAN_EXT`  (
                                   `ORGAN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '单位编号',
                                   `NAME_PY` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '全拼',
                                   `SNAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位简称',
                                   `NAME_EN` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称英文',
                                   `SEARCH_CODE` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '查询码，单位名称的拼音首字母',
                                   `ESTABLISH_DATE` datetime(0) NULL DEFAULT NULL COMMENT '成立时间',
                                   `KEYWORDS` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关键字',
                                   `IMAGE_PATH` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位图片',
                                   `WEB` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位网址',
                                   `FAX` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位传真',
                                   `TEL` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位电话',
                                   `CONTACT_NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人名称',
                                   `CONTACT_MOBILE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人手机',
                                   `CONTACT_EMAIL` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人电子邮箱',
                                   `POST_CODE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮政编码',
                                   `ADDRESS` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地址',
                                   `UNIT_LOGO` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位LOGO',
                                   `UNIT_QRCODE` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位二维码',
                                   `BGD_SOUND` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '背景音乐',
                                   `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位简介',
                                   PRIMARY KEY (`ORGAN_ID`) USING BTREE,
                                   INDEX `IDX_BASE_ORGAN_EXT_SEARCH`(`SEARCH_CODE`) USING BTREE,
                                   INDEX `IDX_BASE_ORGAN_EXT_ADDR`(`ADDRESS`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '基础单位扩展信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for BASE_XZQH
-- ----------------------------
DROP TABLE IF EXISTS `BASE_XZQH`;
CREATE TABLE `BASE_XZQH`  (
                              `XZQH_ID` bigint(0) NOT NULL COMMENT '行政区划编号',
                              `PARENT_ID` bigint(0) NULL DEFAULT NULL COMMENT '行政区划父节点',
                              `COUNTRY_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '国家或地区编号',
                              `AREATYPE_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地域级别ID',
                              `LFT` int(0) NULL DEFAULT NULL COMMENT '树左边',
                              `RGT` int(0) NULL DEFAULT NULL COMMENT '树右边',
                              `CODE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '行政区划代码',
                              `NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '行政区划名称',
                              `NAME_TOTAL` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '行政区划完整名称',
                              `SNAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '行政区划简洁名称',
                              `NAME_EN` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称英文',
                              `SEARCH_CODE` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '快速码',
                              `IS_DISPLAY` tinyint(1) NULL DEFAULT 1 COMMENT '是否显示(1显示0不显示)',
                              `HAS_CONTENT` tinyint(1) NULL DEFAULT 1 COMMENT '是否有内容(1有内容，编号单位等)',
                              `PRIORITY` int(0) NULL DEFAULT 10 COMMENT '排序',
                              `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                              `LAST_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '最后时间',
                              `DEL_FLAG` int(0) NULL DEFAULT 0 COMMENT '删除标记0正常1已删除2应用内受限3回收站',
                              `DELETE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
                              `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
                              PRIMARY KEY (`XZQH_ID`) USING BTREE,
                              INDEX `IDX_BASE_XZQH_CODE`(`CODE`) USING BTREE,
                              INDEX `IDX_BASE_XZQH_NAME`(`NAME`) USING BTREE,
                              INDEX `IDX_BASE_XZQH_PRI`(`PRIORITY`) USING BTREE,
                              INDEX `IDX_BASE_XZQH_SCODE`(`SEARCH_CODE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '行政区划' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for COMM_ATTACH
-- ----------------------------
DROP TABLE IF EXISTS `COMM_ATTACH`;
CREATE TABLE `COMM_ATTACH`  (
                                `ATTACH_ID` bigint(0) NOT NULL COMMENT '附件ID',
                                `FOREIGN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外部外键ID，关联TASK时：TASK_ID',
                                `OP_L_ID` bigint(0) NULL DEFAULT NULL COMMENT '上传人ID，关联MEMBER_ID',
                                `RESTYPE_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型ID',
                                `ATTACH_TYPE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '附件类型，材料类型，如一般附件，一般图片，营业执照/组织机构代码证/房屋所有权证/国有土地使用证/上海企业投资项目备案意见/发改委立项批文/其它/',
                                `ATTACH_PATH` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '附件路径',
                                `ATTACH_NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '附件名称，材料名称',
                                `SHOW_NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件名，显示名称',
                                `IS_IMAGE` tinyint(1) NULL DEFAULT 0 COMMENT '是否图片0否1是',
                                `IS_VIDEO` tinyint(1) NULL DEFAULT 0 COMMENT '是否视频0否1是',
                                `PREVIEW_PATH` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '附件预览路径',
                                `DOWN_COUNT` int(0) NULL DEFAULT 0 COMMENT '下载次数，收取数量,记录所收取申请材料的数量',
                                `PRIORITY` int(0) NULL DEFAULT 10 COMMENT '排序',
                                `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '上传时间',
                                `DESCRIPTION` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
                                PRIMARY KEY (`ATTACH_ID`) USING BTREE,
                                INDEX `IDX_COMM_ATTACH_F_ID`(`FOREIGN_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '应用基础通用附件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for COMM_EXPORT
-- ----------------------------
DROP TABLE IF EXISTS `COMM_EXPORT`;
CREATE TABLE `COMM_EXPORT`  (
                                `EXPORT_ID` bigint(0) NOT NULL COMMENT '导出任务ID',
                                `APP_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用ID',
                                `SITE_ID` bigint(0) NULL DEFAULT NULL COMMENT '站点ID',
                                `FOREIGN_TYPE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外部外键类型，申请信息SasTaskInfo，个人信息GlobalUser，企业信息GlobalOrgan',
                                `FOREIGN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外部外键ID，关联TASK_ID，USER_ID，ORGAN_ID',
                                `FOREIGN_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外部名称，申请对象名称，个人名称，企业名称',
                                `TYPE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '导出材料类型，ZIP压缩包',
                                `OP_TIME` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
                                `OP_USER_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人ID',
                                `OP_USER_NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人名称',
                                `OP_ORGAN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作部门ID',
                                `OP_ORGAN_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作部门名称',
                                `DOWN_COUNT` int(0) NULL DEFAULT 0 COMMENT '下载次数',
                                `DOWN_PATH` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '下载预览路径',
                                `PRIORITY` int(0) NULL DEFAULT 10 COMMENT '排序',
                                `STATUS` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态：队列中、下载中、已完成',
                                `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                `LAST_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '最后时间',
                                `DESCRIPTION` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
                                `SPARE1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1',
                                `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2',
                                PRIMARY KEY (`EXPORT_ID`) USING BTREE,
                                INDEX `IDX_COMM_EXPORT_F_ID`(`FOREIGN_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '应用基础通用导出' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for COMM_FILE
-- ----------------------------
DROP TABLE IF EXISTS `COMM_FILE`;
CREATE TABLE `COMM_FILE`  (
                              `FILE_PATH` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件路径',
                              `APP_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用ID',
                              `RESCLASS_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资源种类ID',
                              `USER_TYPE` int(0) NULL DEFAULT NULL COMMENT '用户类型',
                              `FILE_NAME` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件名称',
                              `FILE_ISVALID` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否有效',
                              `REF_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参考ID',
                              `REF_TYPE` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类名称',
                              `FILE_SIZE` bigint(0) NULL DEFAULT NULL COMMENT '文件大小，单位KB',
                              `FILE_EXTENSION` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件扩展名',
                              `CREATOR_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建用户ID',
                              `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                              `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
                              `SPARE1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1',
                              `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2',
                              PRIMARY KEY (`FILE_PATH`) USING BTREE,
                              INDEX `IDX_COMM_FILE_NAME`(`FILE_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '公共文件附件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for COMM_FLOW
-- ----------------------------
DROP TABLE IF EXISTS `COMM_FLOW`;
CREATE TABLE `COMM_FLOW`  (
                              `FLOW_ID` bigint(0) NOT NULL COMMENT '主键',
                              `FOREIGN_TYPE` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外键类型，GWS_YIQ_NORMAL_REPORT通用报备类型，',
                              `FOREIGN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外键ID，GWS_YIQ_NORMAL_REPORT的REPORT_ID',
                              `OBJ_USER_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对象人员ID，关联GLOBAL_USER、关联ELE_PEOPLE、关联SAS_PEOPLE_BASE',
                              `OBJ_ORGAN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对象机构ID，关联GLOBAL_ORGAN、关联ELE_ORGAN、关联SAS_ORGAN_BASE',
                              `OBJ_CONN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对象关联ID，备用字段',
                              `FLOW_TYPE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作类型 D（派发）、R（复核）、C（催办）、E（退回）',
                              `ASSIGN_FLAG` int(0) NULL DEFAULT NULL COMMENT '流程单产生标示，0自动派单，1人工派单',
                              `CURR_STATUS` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务单状态，关联字典',
                              `CURR_NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '当前环节节点名称',
                              `OP_TIME` datetime(0) NULL DEFAULT NULL COMMENT '流程单发出时间',
                              `OP_USER_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流程单操作人ID',
                              `OP_USER_NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流程单操作人名称',
                              `OP_ORGAN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流程单发出部门ID',
                              `OP_ORGAN_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流程单发出部门名称',
                              `OP_INFO` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流程单内容',
                              `OP_BAK1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流程单备用',
                              `NEXT_STATUS` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '后续操作状态',
                              `NEXT_NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '后续环节节点名称',
                              `NEXT_ORGAN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '后续环节单位ID',
                              `NEXT_ORGAN_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '后续环节部门名称',
                              `NEXT_USER_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '后续环节人ID',
                              `NEXT_USER_NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '后续环节人名称',
                              `IS_SIGN` tinyint(1) NULL DEFAULT NULL COMMENT '是否签收0否1是',
                              `SIGN_TIME` datetime(0) NULL DEFAULT NULL COMMENT '签收时间',
                              `RECEIVE_DEADLINE` int(0) NULL DEFAULT NULL COMMENT '反馈时限',
                              `RECEIVE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '反馈时间',
                              `RECEIVE_CONTENT` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '反馈处置意见',
                              `RECEIVE_RESULT` int(0) NULL DEFAULT NULL COMMENT '反馈结果0不通过10通过',
                              `RECEIVE_BAK1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '反馈处置备用',
                              `IS_OVERTIME` tinyint(1) NULL DEFAULT 0 COMMENT '单个流程环节是否超期0否1是',
                              `OVER_TIME` datetime(0) NULL DEFAULT NULL COMMENT '到期时间',
                              `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '处置时间',
                              `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
                              PRIMARY KEY (`FLOW_ID`) USING BTREE,
                              INDEX `IDX_C_F_T_ID`(`FOREIGN_ID`) USING BTREE,
                              INDEX `IDX_C_F_O_O_ID`(`OP_ORGAN_ID`) USING BTREE,
                              INDEX `IDX_C_F_O_U_ID`(`OP_USER_ID`) USING BTREE,
                              INDEX `IDX_C_F_C_STATUS`(`CURR_STATUS`) USING BTREE,
                              INDEX `IDX_C_F_F_TYPE`(`FLOW_TYPE`) USING BTREE,
                              INDEX `IDX_C_F_C_TIME`(`CREATE_TIME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '应用报备通用审批流程' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for COMM_IMPORT
-- ----------------------------
DROP TABLE IF EXISTS `COMM_IMPORT`;
CREATE TABLE `COMM_IMPORT`  (
                                `IMPORT_ID` bigint(0) NOT NULL COMMENT '导出任务ID',
                                `APP_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用ID',
                                `SITE_ID` bigint(0) NULL DEFAULT NULL COMMENT '站点ID',
                                `TYPE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '导入数据材料类型，BDATA业务数据，BFILE业务附件',
                                `SUB_TYPE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '导入子类型',
                                `NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
                                `OUT_FILE_PATH` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外部文件路径',
                                `TARGET_TABLE` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '导入目标表',
                                `FUNC_CODE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '导入方法函数代码',
                                `FUNC_URL` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '导入方法函数路径URL',
                                `IMPORT_RESULT` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '导入结果，SUCCESS成功ERROR错误EXCEPTION异常',
                                `DATA_NUM` int(0) NULL DEFAULT NULL COMMENT '数据量，单位为条',
                                `OP_TIME` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
                                `OP_USER_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人ID，通过TOKEN可直接获取',
                                `OP_USER_NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人名称，可为空，也可直接使用MEMBER的VIEW_NAME',
                                `OP_ORGAN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作部门ID',
                                `OP_ORGAN_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作部门名称',
                                `STATUS` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态：队列中、下载中、已完成',
                                `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                `LAST_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '最后时间',
                                `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
                                `SPARE1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1',
                                `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2',
                                PRIMARY KEY (`IMPORT_ID`) USING BTREE,
                                INDEX `IDX_COMM_IMPORT_A_ID`(`APP_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '应用基础通用导入' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for COMM_LOG
-- ----------------------------
DROP TABLE IF EXISTS `COMM_LOG`;
CREATE TABLE `COMM_LOG`  (
                             `LOG_ID` bigint(0) NOT NULL COMMENT '日志ID',
                             `APP_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用ID',
                             `USERNAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
                             `CATEGORY` int(0) NOT NULL COMMENT '日志类型：10业务操作20运营操作30商户个人41单点登录51平台登录',
                             `OPERATION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作',
                             `METHOD` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '方法',
                             `PARAMS` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访问的路径地址详情',
                             `SRC_CODE` int(0) NOT NULL COMMENT '当前登录来源',
                             `CREATOR_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '管理用户ID',
                             `CREATE_TIME` datetime(0) NOT NULL COMMENT '创建时间',
                             `IP` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '来访IP地址',
                             `TIME` bigint(0) NULL DEFAULT NULL COMMENT '执行时长',
                             `SPARE1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1，BROWSER',
                             `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2，EXCEPTION_DETAIL',
                             PRIMARY KEY (`LOG_ID`) USING BTREE,
                             INDEX `IDX_COMM_LOG_DATE`(`CREATE_TIME`) USING BTREE,
                             INDEX `IDX_COMM_LOG_APP_ID`(`APP_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for CORE_ACCOUNT
-- ----------------------------
DROP TABLE IF EXISTS `CORE_ACCOUNT`;
CREATE TABLE `CORE_ACCOUNT`  (
                                 `ACCOUNT_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对接账户ID，外部系统分配的主键',
                                 `APP_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用ID',
                                 `ORGAN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '系统机构ID',
                                 `ORGAN_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '系统机构名称，没有ORGAN_ID时使用',
                                 `INOUT_MODE` int(0) NOT NULL DEFAULT 0 COMMENT '对接模式0内部接入1内部接出11外部接入20双向40市民云对接类短信平台',
                                 `NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对接系统或账户名称',
                                 `CNAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对接系统名称中文',
                                 `TYPE` int(0) NOT NULL COMMENT '对接系统类型1平台类型2APP类型3身份源类型9外部类型',
                                 `CLIENT_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对接系统CLIENTID，或CORPID',
                                 `CLIENT_SECRET` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '客户密钥',
                                 `AES_KEY` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'AES加解密密钥，备用',
                                 `AES_IV_KEY` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'AES iv key，备用',
                                 `SERVER_IP` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对接服务器IP地址或域名',
                                 `SERVER_TYPE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对接服务器类型，授权类型',
                                 `SERVER_URL` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '默认接口URL，存放CODE_URL，短信网关配置',
                                 `SERVER_CONF1` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务器配置扩展1，MODE为11时，访问IP地址白名单，,分隔；MODE为40时，',
                                 `SERVER_CONF2` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务器配置扩展2，MODE为40时，消息发送方式001,111,100,010等，第一位为发送站内信，第二位为短信，第三位为彩信',
                                 `SERVER_CONF3` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务器配置扩展3，MODE为1时平台类型，如果共用SMY体系，此时填写smy，多个市民云体系的APP可以用一套模式接入；MODE为40时，短信模板',
                                 `CURR_COUNT` int(0) NOT NULL DEFAULT 0 COMMENT '当前调用次数',
                                 `CONF_LIMIT` int(0) NOT NULL DEFAULT 0 COMMENT '每日限制调用次数(0无限制)',
                                 `IS_ADMIN` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否默认管理后台API账户，是否开启前后端加密，如果开启则使用公钥私钥',
                                 `IS_SINGLE` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否限制单设备同时登陆',
                                 `IS_DISABLED` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否禁用0否1是',
                                 `STATUS` int(0) NOT NULL DEFAULT 1 COMMENT '对接系统状态0不可用1可用2过期',
                                 `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建用时间',
                                 `CREATOR_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户ID',
                                 `LAST_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '最后一次调用时间',
                                 `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对接系统描述',
                                 `SPARE1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1',
                                 `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2',
                                 PRIMARY KEY (`ACCOUNT_ID`) USING BTREE,
                                 INDEX `IDX_CORE_ACCOUNT_TYPE`(`TYPE`) USING BTREE,
                                 INDEX `IDX_CORE_ACCOUNT_NAME`(`NAME`) USING BTREE,
                                 INDEX `IDX_CORE_ACCOUNT_CLIENT_ID`(`CLIENT_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '平台对接账户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for CORE_DEPTTYPE
-- ----------------------------
DROP TABLE IF EXISTS `CORE_DEPTTYPE`;
CREATE TABLE `CORE_DEPTTYPE`  (
                                  `DEPTTYPE_ID` bigint(0) NOT NULL COMMENT '部门分类ID',
                                  `PARENT_ID` bigint(0) NULL DEFAULT NULL COMMENT '父部门分类ID',
                                  `ORGTYPE_ID` bigint(0) NULL DEFAULT NULL COMMENT '机构分类ID',
                                  `AREATYPE_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地域ID',
                                  `NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门分类名称',
                                  `SNAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '部门分类简称,英文',
                                  `CODE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '行政区划代码',
                                  `LFT` int(0) NULL DEFAULT NULL COMMENT '树左边',
                                  `RGT` int(0) NULL DEFAULT NULL COMMENT '树右边',
                                  `SEARCH_CODE` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '快速码',
                                  `DISP_FLAG` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '显示标记，用|分隔，第1位全部第2位重点第3位特殊分类；如1|1|0',
                                  `IS_DISABLED` tinyint(1) NULL DEFAULT 0 COMMENT '是否禁用0否1是',
                                  `PRIORITY` int(0) NULL DEFAULT NULL COMMENT '排序',
                                  `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                  `LAST_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '最后时间',
                                  `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
                                  `SPARE1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1',
                                  `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2',
                                  PRIMARY KEY (`DEPTTYPE_ID`) USING BTREE,
                                  INDEX `IDX_CORE_DEPTTYPE_NAME`(`NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门业务分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for CORE_ORGAN
-- ----------------------------
DROP TABLE IF EXISTS `CORE_ORGAN`;
CREATE TABLE `CORE_ORGAN`  (
                               `SYSORGAN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '机构ID',
                               `ORGTYPE_ID` bigint(0) NULL DEFAULT NULL COMMENT '机构分类ID',
                               `ORGAN_OCODE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '机构编码,其他编码,自定义编码，负责人联系方式',
                               `ORGAN_GROUP` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位分组',
                               `ORGAN_LEVEL` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位级别',
                               `APPROVAL_NUMBER` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位批准文号',
                               `IS_STATION_ORG` tinyint(1) NULL DEFAULT 0 COMMENT '是否是派驻单位',
                               `ORGAN_TYPE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '机构类型，GENERAL政府通用OPEN开放平台服务商RENT租户ENT企业用户',
                               `ORGAN_PROPERTY` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '机构性质',
                               `CREATOR_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建管理用户ID',
                               `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                               `UPDATOR_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '负责人D',
                               `UPDATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '负责人修改时间',
                               `IS_ADMIN` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否管理0否1是，是则ADMIN_DEPT内存在信息',
                               `IS_MEMBER` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否前台0否1是',
                               `IS_DISABLED` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否禁用0否1是',
                               `IS_VIRTUAL` tinyint(1) NULL DEFAULT 0 COMMENT '是否非四套班子虚拟节点1是0不是默认0',
                               `IS_PUB` tinyint(1) NULL DEFAULT 0 COMMENT '是否公开1是0不是默认0',
                               `IS_GRID_ORGAN` tinyint(1) NULL DEFAULT 0 COMMENT '是否网格机构0否1是',
                               `IS_VILLAGE_ORGAN` tinyint(1) NULL DEFAULT 0 COMMENT '是否行政村机构0否1是',
                               `IS_PARTY_ORGAN` tinyint(1) NULL DEFAULT 0 COMMENT '是否党支部机构0否1是',
                               `PRIORITY` int(0) NOT NULL DEFAULT 10 COMMENT '排序',
                               `LAST_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '最后时间',
                               `DEL_FLAG` int(0) NOT NULL DEFAULT 0 COMMENT '删除标记0正常1已删除2应用内受限3回收站',
                               PRIMARY KEY (`SYSORGAN_ID`) USING BTREE,
                               INDEX `IDX_CORE_ORGAN_ORG_TYPE`(`ORGAN_TYPE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '核心机构' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for CORE_ORGAN_ATTR
-- ----------------------------
DROP TABLE IF EXISTS `CORE_ORGAN_ATTR`;
CREATE TABLE `CORE_ORGAN_ATTR`  (
                                    `RECORD_ID` bigint(0) NOT NULL COMMENT '属性ID',
                                    `ORGAN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '机构ID',
                                    `ATTR_KEY` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '属性名称',
                                    `ATTR_VALUE` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '属性值',
                                    PRIMARY KEY (`RECORD_ID`) USING BTREE,
                                    UNIQUE INDEX `IDX_CORE_ORGAN_ATTR_ID`(`RECORD_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '核心机构属性' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for CORE_ORGTYPE
-- ----------------------------
DROP TABLE IF EXISTS `CORE_ORGTYPE`;
CREATE TABLE `CORE_ORGTYPE`  (
                                 `ORGTYPE_ID` bigint(0) NOT NULL COMMENT '机构分类ID',
                                 `PARENT_ID` bigint(0) NULL DEFAULT NULL COMMENT '父分类ID',
                                 `CODE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类编码',
                                 `NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
                                 `SNAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类简称，英文',
                                 `PRIORITY` int(0) NULL DEFAULT NULL COMMENT '排序',
                                 `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                 `LAST_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '最后时间',
                                 `DEL_FLAG` int(0) NOT NULL DEFAULT 0 COMMENT '删除标记0正常1已删除2应用内受限3回收站',
                                 `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
                                 `SPARE1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1',
                                 `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2',
                                 PRIMARY KEY (`ORGTYPE_ID`) USING BTREE,
                                 INDEX `IDX_CORE_ORGTYPE_NAME`(`NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '机构分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for CORE_POSTYPE
-- ----------------------------
DROP TABLE IF EXISTS `CORE_POSTYPE`;
CREATE TABLE `CORE_POSTYPE`  (
                                 `POSTYPE_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位分类ID',
                                 `NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
                                 `SNAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类简称，英文',
                                 `PRIORITY` int(0) NULL DEFAULT NULL COMMENT '排序',
                                 `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                 `LAST_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '最后时间',
                                 `DEL_FLAG` int(0) NOT NULL DEFAULT 0 COMMENT '删除标记0正常1已删除2应用内受限3回收站',
                                 `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
                                 `SPARE1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1',
                                 `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2',
                                 PRIMARY KEY (`POSTYPE_ID`) USING BTREE,
                                 INDEX `IDX_CORE_POSTYPE_NAME`(`NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '岗位分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for CORE_RIGHTTYPE
-- ----------------------------
DROP TABLE IF EXISTS `CORE_RIGHTTYPE`;
CREATE TABLE `CORE_RIGHTTYPE`  (
                                   `RIGHTTYPE_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资源类型ID',
                                   `NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类型名称',
                                   `SNAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型简称,英文',
                                   `PRIORITY` int(0) NULL DEFAULT 10 COMMENT '排序',
                                   `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                   `LAST_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '最后时间',
                                   `DEL_FLAG` int(0) NOT NULL DEFAULT 0 COMMENT '删除标记0正常1已删除2应用内受限3回收站',
                                   `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
                                   `SPARE1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1',
                                   `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2',
                                   PRIMARY KEY (`RIGHTTYPE_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '权限资源类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for CORE_ROLETYPE
-- ----------------------------
DROP TABLE IF EXISTS `CORE_ROLETYPE`;
CREATE TABLE `CORE_ROLETYPE`  (
                                  `ROLETYPE_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色分类ID',
                                  `NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
                                  `TYPE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色类型0全局角色1系统管理角色2应用会员角色',
                                  `CODE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色编码，角色代码',
                                  `PRIORITY` int(0) NOT NULL DEFAULT 10 COMMENT '排序',
                                  `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                  `LAST_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '最后时间',
                                  `DEL_FLAG` int(0) NOT NULL DEFAULT 0 COMMENT '删除标记0正常1已删除2应用内受限3回收站',
                                  `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色描述',
                                  `SPARE1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1',
                                  `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2',
                                  PRIMARY KEY (`ROLETYPE_ID`) USING BTREE,
                                  INDEX `IDX_CORE_ROLETYPE_CODE`(`CODE`) USING BTREE,
                                  INDEX `IDX_CORE_ROLETYPE_NAME`(`NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for GEN_TABLE
-- ----------------------------
DROP TABLE IF EXISTS `GEN_TABLE`;
CREATE TABLE `GEN_TABLE`  (
                              `TABLE_ID` bigint(0) NOT NULL COMMENT '表编号',
                              `TABLE_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表名称',
                              `TABLE_COMMENT` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表描述',
                              `SUB_TABLE_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联子表的表名',
                              `SUB_TABLE_FK_NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子表关联的外键名',
                              `CLASS_NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '实体类名称',
                              `TPL_CATEGORY` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'crud' COMMENT '使用的模板，crud单表操作，tree树表操作，sub主子表，json前端自定义单表，vo业务自定义单表',
                              `TPL_WEB_TYPE` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '前端类型（element-ui模版 element-plus模版）',
                              `PACKAGE_NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成包路径',
                              `MODULE_NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成模块名',
                              `BUSINESS_NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成业务名',
                              `FUNCTION_NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成功能名',
                              `FUNCTION_AUTHOR` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成功能作者',
                              `FORM_COL_NUM` int(0) NULL DEFAULT 1 COMMENT '表单布局，单列 双列 三列',
                              `GEN_TYPE` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '生成代码方式，0ZIP压缩包，1自定义路径',
                              `GEN_PATH` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '/' COMMENT '生成路径，不填默认项目路径',
                              `OPTION_LISTS` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '其它生成选项',
                              `PRIORITY` int(0) NULL DEFAULT NULL COMMENT '排序',
                              `CREATOR_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
                              `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                              `LAST_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
                              `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
                              `SPARE1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1',
                              `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2',
                              PRIMARY KEY (`TABLE_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成业务' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for GEN_TABLE_COLUMN
-- ----------------------------
DROP TABLE IF EXISTS `GEN_TABLE_COLUMN`;
CREATE TABLE `GEN_TABLE_COLUMN`  (
                                     `COLUMN_ID` bigint(0) NOT NULL COMMENT '列编号',
                                     `TABLE_ID` bigint(0) NULL DEFAULT NULL COMMENT '归属表编号',
                                     `COLUMN_NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列名称',
                                     `COLUMN_COMMENT` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列描述',
                                     `COLUMN_TYPE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列类型',
                                     `JAVA_TYPE` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'JAVA类型',
                                     `JAVA_FIELD` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'JAVA字段名',
                                     `IS_PK` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否主键',
                                     `IS_INCREMENT` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否自增',
                                     `IS_REQUIRED` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否必填',
                                     `IS_INSERT` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否为插入字段',
                                     `IS_EDIT` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否编辑字段',
                                     `IS_LIST` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否列表字段',
                                     `IS_QUERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否查询字段',
                                     `QUERY_TYPE` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'EQ' COMMENT '查询方式，等于、不等于、大于、小于、范围',
                                     `HTML_TYPE` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '显示类型，文本框、文本域、下拉框、复选框、单选框、日期控件',
                                     `DICT_TYPE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公共字典类型',
                                     `OPTION_BAK` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '其它生成选项',
                                     `PRIORITY` int(0) NULL DEFAULT NULL COMMENT '排序',
                                     `CREATOR_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
                                     `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                     `LAST_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
                                     `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
                                     `SPARE1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1',
                                     `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2',
                                     PRIMARY KEY (`COLUMN_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成业务表字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for PUBLIC_NOTICE
-- ----------------------------
DROP TABLE IF EXISTS `PUBLIC_NOTICE`;
CREATE TABLE `PUBLIC_NOTICE`  (
                                  `MSG_ID` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '信息ID',
                                  `SITE_ID` bigint(0) NULL DEFAULT NULL COMMENT '站点ID',
                                  `ORGAN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '机构ID',
                                  `BIZ_TYPE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务类型1通知公告11促销21模块业务',
                                  `TYPE` int(0) NULL DEFAULT NULL COMMENT '类型，1通知2公告',
                                  `TITLE` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '信息标题',
                                  `CONTENT` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '信息内容',
                                  `MSG_URL` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '信息链接，链接平台内地址，可点击访问',
                                  `FOREIGN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '信息外键，如CONTENT_ID或短彩信网关信息ID',
                                  `IMAGE_PATH` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片路径',
                                  `ICON_STYLE` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标样式',
                                  `RECV_MEMBER_ID` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '信息接收会员ID',
                                  `RECV_GROUP_ID` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '信息接收会员组ID',
                                  `RECV_ORGAN_ID` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '信息接收部门ID',
                                  `RECV_TARGETID` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '接收对象ID或手机号码列表',
                                  `STATUS` int(0) NULL DEFAULT 0 COMMENT '信息状态(0未启用1正常2关闭)',
                                  `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                  `CREATOR_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建管理用户ID',
                                  `UPDATOR_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维护管理用户ID',
                                  `UPDATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                  `LAST_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '最后时间',
                                  `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
                                  PRIMARY KEY (`MSG_ID`) USING BTREE,
                                  INDEX `IDX_MESSAGE_I_TITLE`(`TITLE`) USING BTREE,
                                  INDEX `IDX_MESSAGE_I_CREATE_TIME`(`CREATE_TIME`) USING BTREE,
                                  INDEX `IDX_MESSAGE_I_BIZ_TYPE`(`BIZ_TYPE`) USING BTREE,
                                  INDEX `IDX_MESSAGE_I_MSG_TYPE`(`TYPE`) USING BTREE,
                                  INDEX `IDX_MESSAGE_I_STATUS`(`STATUS`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '平台公共消息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for PUBLIC_UNION_MESSAGE
-- ----------------------------
DROP TABLE IF EXISTS `PUBLIC_UNION_MESSAGE`;
CREATE TABLE `PUBLIC_UNION_MESSAGE`  (
                                         `RECORD_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '记录ID',
                                         `SITE_ID` bigint(0) NULL DEFAULT NULL COMMENT '站点ID',
                                         `FOREIGN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '站点门户ID',
                                         `TYPE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型',
                                         `BIZ_TYPE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务类型',
                                         `MSG_CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '统一消息创建时间',
                                         `MSG_ID` bigint(0) NULL DEFAULT NULL COMMENT '信息ID',
                                         `MSG_TYPE` int(0) NULL DEFAULT 10 COMMENT '消息类型',
                                         `MSG_TITLE` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '信息标题',
                                         `MSG_ABSTRACT` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '统一消息摘要',
                                         `MSG_ACCOUNT_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '统一消息应用账户ID',
                                         `MSG_ACCOUNT_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '统一消息应用系统或账户名称',
                                         `MSG_MODULE_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '统一消息应用应用ID',
                                         `MSG_MODULE_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '统一消息应用应用名称',
                                         `MSG_USER_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '统一消息应用用户ID',
                                         `MSG_USER_NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '统一消息应用用户名称',
                                         `MSG_ORGAN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '统一消息应用机构ID',
                                         `MSG_ORGAN_NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '统一消息应用机构名称',
                                         `MSG_URL` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '统一信息链接，链接平台内地址，可点击访问',
                                         `MSG_LABELS` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '统一消息业务标签',
                                         `MSG_CONTENT` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '统一消息信息内容',
                                         `STATUS` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态，0待阅读，10已阅读',
                                         `OP_USER_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人ID',
                                         `OP_TIME` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
                                         `OP_ORGAN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作单位ID',
                                         `OP_ORGAN_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作单位名称',
                                         `CREATOR_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人ID，不使用外键',
                                         `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                         `LAST_TIME` timestamp(0) NOT NULL COMMENT '最后时间',
                                         `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
                                         `SPARE1` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1',
                                         `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2',
                                         PRIMARY KEY (`RECORD_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '统一消息接收' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for PUBLIC_UNION_TODO
-- ----------------------------
DROP TABLE IF EXISTS `PUBLIC_UNION_TODO`;
CREATE TABLE `PUBLIC_UNION_TODO`  (
                                      `RECORD_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '记录ID',
                                      `SITE_ID` bigint(0) NULL DEFAULT NULL COMMENT '站点ID',
                                      `FOREIGN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '站点门户ID',
                                      `TYPE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型，内部则关联GLOBAL_SERTYPE，外部则为OUT',
                                      `BIZ_TYPE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务类型',
                                      `TODO_CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '待办创建时间',
                                      `TODO_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单据编号',
                                      `TODO_TYPE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单据类型',
                                      `TODO_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单据主题',
                                      `TODO_ABSTRACT` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单据摘要',
                                      `TODO_ACCOUNT_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '待办应用账户ID',
                                      `TODO_ACCOUNT_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '待办应用系统或账户名称',
                                      `TODO_MODULE_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '待办应用应用ID',
                                      `TODO_MODULE_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '待办应用应用名称',
                                      `TODO_USER_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '待办应用用户ID',
                                      `TODO_USER_NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '待办应用用户名称',
                                      `TODO_ORGAN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '待办应用机构ID',
                                      `TODO_ORGAN_NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '待办应用机构名称',
                                      `TODO_PROCESS_NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '待办流程节点名称',
                                      `TODO_PROCESS_STATUS` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '待办流程节点状态码',
                                      `TODO_PROCESS_INFO` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '待办流程节点信息',
                                      `TODO_CURR_INFO` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '待办业务当前信息',
                                      `TODO_NEXT_INFO` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '待办业务下一步信息',
                                      `TODO_URL` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '待办业务URL',
                                      `TODO_LABELS` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单据业务标签',
                                      `TODO_CONTENT` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单据其他信息',
                                      `STATUS` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态，0待办，1在办，10已办，20办结',
                                      `OP_USER_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人ID',
                                      `OP_TIME` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
                                      `OP_ORGAN_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作单位ID',
                                      `OP_ORGAN_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作单位名称',
                                      `CREATOR_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人ID，不使用外键',
                                      `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                      `LAST_TIME` timestamp(0) NOT NULL COMMENT '最后时间',
                                      `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
                                      `SPARE1` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1',
                                      `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2',
                                      PRIMARY KEY (`RECORD_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '统一超级待办' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for SCHEDULE_INFO
-- ----------------------------
DROP TABLE IF EXISTS `SCHEDULE_INFO`;
CREATE TABLE `SCHEDULE_INFO`  (
                                  `SCHEDULE_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '计划编号',
                                  `APP_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用ID',
                                  `JOB_SOURCE` int(0) NULL DEFAULT 1 COMMENT '任务类型1系统内添加2预警任务添加3指标数据获取添加4指标数据执行添加',
                                  `JOB_TYPE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务类型1内部任务2邮件3短信4内容接口',
                                  `SCHEDULE_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务名称',
                                  `OBJECT_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类对象名称',
                                  `METHOD_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '方法名称',
                                  `PARAMS` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数',
                                  `CRON_EXPRESSION` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0 0/30 * * * ?' COMMENT 'cron任务表达式，0 0/30 * * * ?',
                                  `JOB_CONFIG` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '任务详情，JSON格式',
                                  `START_DATE` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
                                  `END_DATE` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
                                  `STATUS` int(0) NULL DEFAULT NULL COMMENT '任务状态:0未启动1正常2暂停',
                                  `CREATOR_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '管理用户ID',
                                  `CREATE_TIME` datetime(0) NOT NULL COMMENT '创建时间',
                                  `LAST_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '最后时间',
                                  `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
                                  `SPARE1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1',
                                  `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2',
                                  PRIMARY KEY (`SCHEDULE_ID`) USING BTREE,
                                  INDEX `IDX_SCHEDULE_INFO_NAME`(`SCHEDULE_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for SCHEDULE_LOG
-- ----------------------------
DROP TABLE IF EXISTS `SCHEDULE_LOG`;
CREATE TABLE `SCHEDULE_LOG`  (
                                 `LOG_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '日志编号',
                                 `SCHEDULE_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务编号',
                                 `STATUS` int(0) NOT NULL COMMENT '任务状态:0成功1失败',
                                 `ERROR` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '失败信息',
                                 `TIMES` int(0) NOT NULL COMMENT '耗时(单位：毫秒)',
                                 `CREATE_TIME` datetime(0) NOT NULL COMMENT '创建时间',
                                 `SPARE1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1',
                                 `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2',
                                 PRIMARY KEY (`LOG_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for SYS_MENU
-- ----------------------------
DROP TABLE IF EXISTS `SYS_MENU`;
CREATE TABLE `SYS_MENU`  (
                             `MENU_ID` bigint(0) NOT NULL COMMENT '功能菜单ID',
                             `PARENT_ID` bigint(0) NULL DEFAULT NULL COMMENT '父功能菜单ID',
                             `APP_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用ID',
                             `NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '功能名称',
                             `ENAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '功能名称简称，英文，其他语种名称，菜单英文名称，用于控制路由',
                             `SNAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '简短名称',
                             `IS_PROTECT` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否系统菜单，0否（会员或前端菜单）1是（系统菜单）',
                             `TYPE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '功能类型,关联SYS_RIGHTTYPE，ADMIN,WORK,APP,SCREEN,MEMBER,AUDIT',
                             `CODE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '功能编号',
                             `MENU_LEVEL` int(0) NULL DEFAULT NULL COMMENT '菜单层次',
                             `SUB_COUNT` int(0) NULL DEFAULT NULL COMMENT '子节点数',
                             `PERMS` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '功能模型，功能权限资源控制参数，多个用逗号分隔，如：admin:user:list',
                             `URL_TYPE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '调用入口类型，如CATAGORY,MENU,BUTTON,OUTLINK,CHANNEL,COMPOS,SCENES,RECOMMEND,FOLLOW',
                             `URL` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '功能地址，前台URL路由地址',
                             `ICON` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '功能图标，样式序号，图标或按钮路径',
                             `POP_POSITION` int(0) NULL DEFAULT NULL COMMENT '弹出位置',
                             `IMAGE_PATH` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片路径',
                             `CSS` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'CSS页面样式',
                             `JS_EVENT` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'JS事件，功能的列表JSON配置',
                             `MENU_SEQ` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单序列,菜单关联的外键ID，如栏目ID，组件ID，三方功能ID',
                             `SHORT_CUT` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '快捷键，菜单全路径快速码',
                             `IS_LEAF` tinyint(1) NULL DEFAULT 0 COMMENT '是否叶子节点0为非叶子节点，1为叶子节点',
                             `IS_DISPLAY` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否显示0否1是',
                             `IS_DISABLED` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否禁用0否1是',
                             `IS_CHECK` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否验证权限0为无需验证，1为需要验证',
                             `PRIORITY` int(0) NOT NULL DEFAULT 10 COMMENT '排序',
                             `CREATOR_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '管理用户ID',
                             `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `LAST_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '最后时间',
                             `DEL_FLAG` int(0) NULL DEFAULT 0 COMMENT '是否删除1已删除0正常',
                             `DELETE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
                             `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
                             `SPARE1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1',
                             `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2',
                             PRIMARY KEY (`MENU_ID`) USING BTREE,
                             INDEX `IDX_SYS_MENU_CODE`(`CODE`) USING BTREE,
                             INDEX `IDX_SYS_MENU_PARENT_ID`(`PARENT_ID`) USING BTREE,
                             INDEX `IDX_SYS_MENU_PRI`(`PRIORITY`) USING BTREE,
                             INDEX `IDX_SYS_MENU_URL_TYPE`(`URL_TYPE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for SYS_POSITION
-- ----------------------------
DROP TABLE IF EXISTS `SYS_POSITION`;
CREATE TABLE `SYS_POSITION`  (
                                 `POSITION_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位ID',
                                 `PARENT_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '父岗位ID',
                                 `POSTYPE_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '岗位分类ID',
                                 `ROLETYPE_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色分类ID',
                                 `NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位名称',
                                 `SNAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '岗位简称，别名',
                                 `CODE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'UAS岗位id',
                                 `ONAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '岗位别名',
                                 `POS_FUNCTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '工作分工，二维码申请页面模板',
                                 `POS_RANK` int(0) NULL DEFAULT NULL COMMENT '级别',
                                 `POS_TYPE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '岗位类型',
                                 `EDITOR_TYPE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '编制类型',
                                 `IS_EDITOR_CREATE` tinyint(1) NULL DEFAULT NULL COMMENT '是否编委创建',
                                 `POS_NATURE` int(0) NULL DEFAULT 1 COMMENT '岗位性质0未生效1启用，STATUS，状态0未生效1正常2受限3锁定',
                                 `IS_PUB` tinyint(1) NULL DEFAULT NULL COMMENT '是否公开0否1是',
                                 `IS_AGENT` tinyint(1) NULL DEFAULT NULL COMMENT '是否代理岗位0否1是',
                                 `IS_LEADER` tinyint(1) NULL DEFAULT NULL COMMENT '是否为部门领导0否1是',
                                 `LEADER_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '部门领导用户ID',
                                 `MANAGE_DEPTIDS` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分管内设机构',
                                 `IS_BIG_CONTACT` tinyint(1) NULL DEFAULT NULL COMMENT '是否大通讯录',
                                 `IS_SMALL_CONTACT` tinyint(1) NULL DEFAULT NULL COMMENT '是否小通讯录',
                                 `MOBILE_PHONE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '岗位移动手机',
                                 `OFFICE_PHONE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '岗位办公电话',
                                 `BUILDING_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '建筑楼栋号',
                                 `ROOM_NO` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '房间号',
                                 `EXT_QM_TYPE` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '签名类型',
                                 `EXT_QM_TYPE_PHOTO` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '签名类型图片',
                                 `EXT_POS_NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '签名岗位',
                                 `EXT_ORGAN_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '签名单位',
                                 `PRIORITY` int(0) NULL DEFAULT NULL COMMENT '排序',
                                 `PRIORITY_IN_ORGAN` int(0) NULL DEFAULT 10 COMMENT '机构内排序',
                                 `PRIORITY_IN_DOMAIN` int(0) NULL DEFAULT 10 COMMENT '应用域内排序',
                                 `CREATOR_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
                                 `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                 `UPDATER_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
                                 `UPDATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                 `LAST_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '最后时间',
                                 `DEL_FLAG` int(0) NULL DEFAULT NULL COMMENT '删除标志0正常1已删除2应用内受限3回收站',
                                 `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述，岗位职责',
                                 `REMARK` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
                                 `SPARE1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1',
                                 `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2',
                                 PRIMARY KEY (`POSITION_ID`) USING BTREE,
                                 INDEX `IDX_SYS_POSITION_NAME`(`NAME`) USING BTREE,
                                 INDEX `IDX_SYS_POSITION_PRI`(`PRIORITY`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统岗位信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for SYS_ROLE
-- ----------------------------
DROP TABLE IF EXISTS `SYS_ROLE`;
CREATE TABLE `SYS_ROLE`  (
                             `ROLE_ID` bigint(0) NOT NULL COMMENT '角色ID',
                             `ROLETYPE_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色分类ID',
                             `APP_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用ID',
                             `NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色自定义名称',
                             `CODE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色自定义代码',
                             `PRIORITY` int(0) NOT NULL DEFAULT 10 COMMENT '排序',
                             `IS_REGULAR` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否固定权限1表示固定角色0表示自定义角色',
                             `IS_SUPER` tinyint(1) NOT NULL DEFAULT 0 COMMENT '拥有所有权限',
                             `IS_PUBLIC` tinyint(1) NULL DEFAULT NULL COMMENT '是否公共角色',
                             `CREATOR_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '管理用户ID',
                             `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `STATUS` int(0) NULL DEFAULT NULL COMMENT '状态0未生效1正常2受限3锁定',
                             `DESCRIPTION` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色描述',
                             `SPARE1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用1，数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
                             `SPARE2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备用2',
                             PRIMARY KEY (`ROLE_ID`) USING BTREE,
                             INDEX `IDX_SYS_ROLE_CODE`(`CODE`) USING BTREE,
                             INDEX `IDX_SYS_ROLE_APP_ID`(`APP_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for SYS_ROLE_AREATYPE
-- ----------------------------
DROP TABLE IF EXISTS `SYS_ROLE_AREATYPE`;
CREATE TABLE `SYS_ROLE_AREATYPE`  (
                                      `ROLEAREA_ID` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '用户角色ID',
                                      `ROLE_ID` bigint(0) NOT NULL COMMENT '角色ID',
                                      `AREATYPE_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地域类型ID',
                                      PRIMARY KEY (`ROLEAREA_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色地域关' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for SYS_ROLE_MENU
-- ----------------------------
DROP TABLE IF EXISTS `SYS_ROLE_MENU`;
CREATE TABLE `SYS_ROLE_MENU`  (
                                  `MENUROLE_ID` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '角色功能ID',
                                  `MENU_ID` bigint(0) NOT NULL COMMENT '功能ID',
                                  `ROLE_ID` bigint(0) NOT NULL COMMENT '角色ID',
                                  PRIMARY KEY (`MENUROLE_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色功能关联' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for SYS_ROLE_POSITION
-- ----------------------------
DROP TABLE IF EXISTS `SYS_ROLE_POSITION`;
CREATE TABLE `SYS_ROLE_POSITION`  (
                                      `ROLEPOS_ID` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '用户角色ID',
                                      `ROLE_ID` bigint(0) NOT NULL COMMENT '角色ID',
                                      `POSITION_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '岗位ID',
                                      `STATUS` int(0) NULL DEFAULT NULL COMMENT '审核状态0未审核1审核通过2冻结3例外',
                                      `BEGIN_TIME` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
                                      `END_TIME` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
                                      PRIMARY KEY (`ROLEPOS_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色岗位关联' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
