# emis-java-backend

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
![java version](https://img.shields.io/badge/JAVA-1.8-green.svg)
![gitee star](https://gitee.com/linlannet/emis-java-backend/badge/star.svg)

## 介绍

基于JAVA开发的企业级多身份管理系统后端工程，基于Springboot，采用支持 JAVA 1.8最高版本的framework、security，后端数据访问采用Mybatis框架，支持多种数据库一套代码适配，提供脚手架可快速生成Mapper等，在仓库功能上，借鉴主流的SSO和IAM框架，实现统一用户管理。前端提供VUE框架和REACT框架的控制台管理工程、企业个人门户工程和移动端应用扩展。

**工具类代码地址：** <a target="_blank">https://gitee.com/linlannet/java-tools-parent

**智能代码生成工具地址：** <a target="_blank">https://gitee.com/linlannet/java-tools-generate

**非浸入式自动生成接口API代码地址：** <a target="_blank">https://gitee.com/linlannet/java-plugin-doc-generate


前端工程路径：

**VUE+TS前端工程地址：** <a target="_blank">https://gitee.com/linlannet/emis-vue-frontend

**REACT+TS前端工程地址：** <a target="_blank">https://gitee.com/linlannet/emis-react-frontend


## 版本历史

![CHANGELOG](CHANGELOG.md)

## 应用效果

**多身份管理系统使用指南及效果：** <a target="_blank">https://gitee.com/linlannet/emis-docs

## 技术路径
### 软件架构
```
Springboot + Framework + Security + Mybatis
```
### 安装教程
1. mvn clean install
2. mvn clean deploy

### 使用说明
1. 通过依赖使用
```
   <dependency>
      <groupId>${groupId}</groupId>
      <artifactId>${artifactId}</artifactId>
      <version>${project.version}</version>
      <scope>test</scope>
   </dependency>
```
2. 直接拷贝打包后的jar包

## 参与贡献
1.  Fork 本仓库
2.  新建 Feat_1.0.0 分支
3.  提交代码
```
git config user.name linlaninfo
git config user.email linlannet@163.com
git config --global --list
git config --list
```
4. 新建 Pull Request
5. 创建和提交tag
```
创建
git tag -a 1.0.0 -m "初始化版本"
查看
git tag
提交
git push origin --tags
删除
git tag -d 1.0.0

本地编译：mvn clean install
发布到github：mvn clean deploy

```
6. 其他
