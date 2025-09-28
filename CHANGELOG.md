### 版本号：1.2.5
- 更新日期：2025-09-28
- 更新内容：
1. provider包为底层包，将authn资源放入authn包
2. user引用provider包
3. bind和third用户单独放到ubind包中，字段更新
4. 重新生成UserThirdParty资源
5. 增加app包和相关资源
6. 增加数据库脚本，更新UserOrganRelation表结构，增加DEPT_ID
7. 调整organ和user内的适配方法名称
8. 调整注释中的version、since和CreateTime信息

### 版本号：1.2.4
- 更新日期：2025-09-27
- 更新内容：
1. 修改user包的资源
2. 修改provider资源
3. adminId在工程中修改为String类型
4. user包内 TODO Admin的适配方法 

### 版本号：1.2.3
- 更新日期：2025-09-23
- 更新内容：
1. 增加audit包资源，修改javadoc文档
2. eframe版本升级为1.1.6

### 版本号：1.2.2
- 更新日期：2025-09-20
- 更新内容：
1. 增加core包资源，修改javadoc文档
2. eframe版本升级为1.1.5

### 版本号：1.2.1
- 更新日期：2025-09-05
- 更新内容：
1. 增加support包资源，修改javadoc文档
2. 增加高版本springboot和springframework部分资源
3. eframe版本升级为1.1.4
4. tools版本升级为2.6.7

### 版本号：1.2.0
- 更新日期：2025-08-31
- 更新内容：
1. webs目录内增加smart-doc验证方法
2. 排除eframe-api的接口，仅生成emis的接口
3. 生成provider资源，验证接口文档生成

### 版本号：1.1.0
- 更新日期：2025-08-31
- 更新内容：
1. 基于eframe-java-backend进行扩展
2. 增加子包简介，创建每个子包目录
3. pom文件和初步的groupId、artificialId

### 版本号：1.0.0
- 更新日期：2025-08-20
- 更新内容：
1. 初始化工程目录
2. 增加根目录的README、Q&A、CHANGELOG
3. 增加THIRD_PARTY.md
4. 采用maven方式进行打包，新增初始化pom文件
5. 修改README.en文件信息
