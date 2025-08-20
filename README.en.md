# emis-java-backend

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
![java version](https://img.shields.io/badge/JAVA-1.8-green.svg)
![gitee star](https://gitee.com/linlannet/emis-java-backend/badge/star.svg)

## Introduce

The back-end project of Enterprise Multiple Identity System (EMIS) based on JAVA is based on Springboot, adopting the framework and security supporting the highest version of JAVA 1.8, adopting Mybatis framework for back-end data access, supporting a set of code adaptation of various databases, providing scaffolding to quickly generate Mapper, etc. In terms of warehouse functions, it draws lessons from mainstream SSO and IAM frameworks to realize unified user management. The front-end provides console management project, enterprise personal portal project and mobile application extension of VUE framework and REACT framework.

**common tools：** <a target="_blank">https://gitee.com/linlannet/java-tools-parent

**code generate：** <a target="_blank">https://gitee.com/linlannet/java-tools-generate

**smart doc plugin：** <a target="_blank">https://gitee.com/linlannet/java-plugin-doc-generate


Front-end engineering path:

**VUE+TS：** <a target="_blank">https://gitee.com/linlannet/emis-vue-frontend

**REACT+TS：** <a target="_blank">https://gitee.com/linlannet/emis-react-frontend


## Change Log

[CHANGELOG](./CHANGELOG.md)


## Application effect

**Guide to the use of EMIS ：** <a target="_blank">https://gitee.com/linlannet/emis-docs

## Technical Guidelines
### Software Architecture
```
Springboot + Framework + Security + Mybatis
```

### Installation
1. mvn clean install
2. mvn clean deploy

### Instructions
1. use by maven
```
   <dependency>
      <groupId>${groupId}</groupId>
      <artifactId>${artifactId}</artifactId>
      <version>${project.version}</version>
      <scope>test</scope>
   </dependency>
```
2. use direct jar

## Contribution
1.  Fork repository
2.  new Feat_1.0.0 branch
3.  commit
```
git config user.name linlaninfo
git config user.email linlannet@163.com
git config --global --list
git config --list
```
4. new Pull Request
5. create and commit tag
```
create
git tag -a 1.0.0 -m "initial version"
list
git tag
commit
git push origin --tags
delete
git tag -d 1.0.0

install: mvn clean install
deploy: mvn clean deploy

```
6. other

## Licence

[Apache 2.0 © emis-java-backend 2023](./LICENSE)
