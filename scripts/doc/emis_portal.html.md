# emis_portal
Version |  Update Time  | Status | Author |  Description
---|---|---|---|---
1.0|2025-08-24 10:30|update|Linlan|desc



# default
# 平台通用接口API
# EMIS平台接口API
## AuditInfo控制域:行为审计读写控制类
### Get AuditInfo list. 获取行为审计列表或分页, 返回AuditInfo对象.
**URL:** https://linlan.net/emis_portal/api/audit/info/list.do

**Type:** GET

**Author:** Linlan
CreateTime 2025-09-06 15:55:04

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** Get AuditInfo list. 获取行为审计列表或分页, 返回AuditInfo对象.

**Request-headers:**

Header | Type|Description|Required|Since
---|---|---|---|----
token|string|令牌|true|-


**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
q|string|关键字：查询输入条件q，可匹配name、title、keywords等文本信息|false|-
orderBy|int32|排序，默认主键倒序|false|-
startTime|string|开始时间|false|-
endTime|string|结束时间|false|-
page|int32|分页信息：默认第1页|false|-
limit|int32|分页每页条数，默认10条|false|-
username|string|用户名|false|-
eventType|array|审计类型<br/>LOGIN_PORTAL -(PortalEventType.LOGIN_PORTAL)<br/>LOGIN_CONSOLE -(AuthenticationEventType.LOGIN_CONSOLE)<br/>LOGOUT_PORTAL -(PortalEventType.LOGOUT_PORTAL)<br/>LOGOUT_CONSOLE -(AuthenticationEventType.LOGOUT_CONSOLE)<br/>CREATE_ORG -(AccountEventType.CREATE_ORG)<br/>UPDATE_ORG -(AccountEventType.UPDATE_ORG)<br/>DELETE_ORG -(AccountEventType.DELETE_ORGANIZATION)<br/>MOVE_ORGANIZATION -(AccountEventType.MOVE_ORGANIZATION)<br/>CREATE_ORGANIZATION_MEMBER -(AccountEventType.CREATE_ORGANIZATION_MEMBER)<br/>CREATE_USER -(AccountEventType.CREATE_USER)<br/>UPDATE_USER -(AccountEventType.UPDATE_USER)<br/>DELETE_USER -(AccountEventType.DELETE_USER)<br/>ENABLE_USER -(AccountEventType.ENABLE_USER)<br/>DISABLE_USER -(AccountEventType.DISABLE_USER)<br/>USER_RESIGN -(AccountEventType.USER_RESIGN)<br/>MODIFY_USER_PHONE -(AccountEventType.MODIFY_USER_PHONE)<br/>MODIFY_USER_PASSWORD -(AccountEventType.MODIFY_USER_PASSWORD)<br/>UNLOCK_USER -(AccountEventType.UNLOCK_USER)<br/>MODIFY_ACCOUNT_INFO_PORTAL -(PortalEventType.MODIFY_ACCOUNT_INFO)<br/>MODIFY_USER_PASSWORD_PORTAL -(PortalEventType.MODIFY_USER_PASSWORD)<br/>MODIFY_USER_PHONE_PORTAL -(PortalEventType.MODIFY_USER_PHONE)<br/>MODIFY_USER_EMAIL_PORTAL -(PortalEventType.MODIFY_USER_EMAIL)<br/>BIND_MFA -(PortalEventType.BIND_MFA)<br/>UNBIND_MFA -(PortalEventType.UNBIND_MFA)<br/>BIND_IDP_USER -(PortalEventType.BIND_IDP_USER)<br/>UNBIND_IDP_USER -(PortalEventType.UNBIND_IDP_USER)<br/>PREPARE_MODIFY_PASSWORD -(PortalEventType.PREPARE_MODIFY_PASSWORD)<br/>PREPARE_MODIFY_MOBILE -(PortalEventType.PREPARE_MODIFY_MOBILE)<br/>PREPARE_MODIFY_EMAIL -(PortalEventType.PREPARE_MODIFY_EMAIL)<br/>PREPARE_BIND_MFA -(PortalEventType.PREPARE_BIND_MFA)<br/>CREATE_USER_GROUP -(AccountEventType.CREATE_USER_GROUP)<br/>UPDATE_USER_GROUP -(AccountEventType.UPDATE_USER_GROUP)<br/>DELETE_USER_GROUP -(AccountEventType.DELETE_USER_GROUP)<br/>ADD_USER_GROUP_MEMBER -(AccountEventType.ADD_USER_GROUP_MEMBER)<br/>REMOVE_USER_GROUP_MEMBER -(AccountEventType.REMOVE_USER_GROUP_MEMBER)<br/>CREATE_USER_DYNAMIC_GROUP -(AccountEventType.CREATE_USER_DYNAMIC_GROUP)<br/>UPDATE_USER_DYNAMIC_GROUP -(AccountEventType.UPDATE_USER_DYNAMIC_GROUP)<br/>DELETE_USER_DYNAMIC_GROUP -(AccountEventType.DELETE_USER_DYNAMIC_GROUP)<br/>UPDATE_USER_DYNAMIC_GROUP_FILTER_RULE -(AccountEventType.UPDATE_USER_DYNAMIC_GROUP_FILTER_RULE)<br/>CREATE_IDENTITY_RESOURCE -(AccountEventType.CREATE_IDENTITY_RESOURCE)<br/>UPDATE_IDENTITY_RESOURCE -(AccountEventType.UPDATE_IDENTITY_RESOURCE)<br/>DELETE_IDENTITY_RESOURCE -(AccountEventType.DELETE_IDENTITY_RESOURCE)<br/>ENABLE_IDENTITY_RESOURCE -(AccountEventType.ENABLE_IDENTITY_RESOURCE)<br/>DISABLE_IDENTITY_RESOURCE -(AccountEventType.DISABLE_IDENTITY_RESOURCE)<br/>IDENTITY_RESOURCE_SYNC -(AccountEventType.IDENTITY_RESOURCE_SYNC)<br/>ADD_IDP -(AuthenticationEventType.ADD_IDP)<br/>UPDATE_IDP -(AuthenticationEventType.UPDATE_IDP)<br/>DELETE_IDP -(AuthenticationEventType.DELETE_IDP)<br/>ENABLE_IDP -(AuthenticationEventType.ENABLE_IDP)<br/>DISABLE_IDP -(AuthenticationEventType.DISABLE_IDP)<br/>APP_SSO -(PortalEventType.APP_SSO)<br/>APP_SLO -(PortalEventType.APP_SLO)<br/>ADD_APP -(AppEventType.ADD_APP)<br/>ENABLE_APP -(AppEventType.ENABLE_APP)<br/>DISABLE_APP -(AppEventType.DISABLE_APP)<br/>UPDATE_APP -(AppEventType.UPDATE_APP)<br/>SAVE_APP_CONFIG -(AppEventType.SAVE_APP_CONFIG)<br/>DELETE_APP -(AppEventType.DELETE_APP)<br/>APP_AUTHORIZATION -(AppEventType.APP_ACCESS_POLICY)<br/>ENABLE_APP_ACCESS_POLICY -(AppEventType.ENABLE_APP_ACCESS_POLICY)<br/>DISABLE_APP_ACCESS_POLICY -(AppEventType.DISABLE_APP_ACCESS_POLICY)<br/>APP_CANCEL_ACCESS_POLICY -(AppEventType.APP_CANCEL_ACCESS_POLICY)<br/>ADD_APP_ACCOUNT -(AppEventType.ADD_APP_ACCOUNT)<br/>UPDATE_DEFAULT_APP_ACCOUNT -(AppEventType.UPDATE_DEFAULT_APP_ACCOUNT)<br/>DELETE_APP_ACCOUNT -(AppEventType.DELETE_APP_ACCOUNT)<br/>SAVE_SSO_CONFIG -(AppEventType.SAVE_SSO_CONFIG)<br/>SAVE_APP_PERMISSION_RESOURCE -(AppEventType.SAVE_APP_PERMISSION_RESOURCE)<br/>UPDATE_APP_PERMISSION_RESOURCE -(AppEventType.UPDATE_APP_PERMISSION_RESOURCE)<br/>DELETE_APP_PERMISSION_RESOURCE -(AppEventType.DELETE_APP_PERMISSION_RESOURCE)<br/>ENABLE_APP_PERMISSION_RESOURCE -(AppEventType.ENABLE_APP_PERMISSION_RESOURCE)<br/>DISABLE_APP_PERMISSION_RESOURCE -(AppEventType.DISABLE_APP_PERMISSION_RESOURCE)<br/>DELETE_USER_IDP_BIND -(AppEventType.DELETE_USER_IDP_BIND)<br/>SAVE_APP_PERMISSION_ROLE -(AppEventType.SAVE_APP_PERMISSION_ROLE)<br/>UPDATE_APP_PERMISSION_ROLE -(AppEventType.UPDATE_APP_PERMISSION_ROLE)<br/>DELETE_APP_PERMISSION_ROLE -(AppEventType.DELETE_APP_PERMISSION_ROLE)<br/>SAVE_LOGIN_SECURITY_BASIC_SETTINGS -(SettingEventType.SAVE_LOGIN_SECURITY_BASIC_SETTINGS)<br/>SAVE_SECURITY_POLICY_SETTINGS -(SettingEventType.SAVE_SECURITY_POLICY_SETTINGS)<br/>SAVE_PASSWORD_POLICY -(SettingEventType.SAVE_PASSWORD_POLICY)<br/>UPDATE_PASSWORD_POLICY -(SettingEventType.UPDATE_PASSWORD_POLICY)<br/>DELETE_PASSWORD_POLICY -(SettingEventType.DELETE_PASSWORD_POLICY)<br/>ENABLE_PASSWORD_POLICY -(SettingEventType.ENABLE_PASSWORD_POLICY)<br/>DISABLE_PASSWORD_POLICY -(SettingEventType.DISABLE_PASSWORD_POLICY)<br/>SORT_PASSWORD_POLICY -(SettingEventType.SORT_PASSWORD_POLICY)<br/>ADD_ADMINISTRATOR -(SettingEventType.ADD_ADMINISTRATOR)<br/>UPDATE_ADMINISTRATOR -(SettingEventType.UPDATE_ADMINISTRATOR)<br/>DELETE_ADMINISTRATOR -(SettingEventType.DELETE_ADMINISTRATOR)<br/>ENABLE_ADMINISTRATOR -(SettingEventType.ENABLE_ADMINISTRATOR)<br/>DISABLE_ADMINISTRATOR -(SettingEventType.DISABLE_ADMINISTRATOR)<br/>RESET_ADMINISTRATOR_PASSWORD -(SettingEventType.RESET_ADMINISTRATOR_PASSWORD)<br/>ADD_ADMINISTRATOR_ROLE -(SettingEventType.ADD_ADMINISTRATOR_ROLE)<br/>UPDATE_ADMINISTRATOR_ROLE -(SettingEventType.UPDATE_ADMINISTRATOR_ROLE)<br/>DELETE_ADMINISTRATOR_ROLE -(SettingEventType.DELETE_ADMINISTRATOR_ROLE)<br/>ENABLE_ADMINISTRATOR_ROLE -(SettingEventType.ENABLE_ADMINISTRATOR_ROLE)<br/>DISABLE_ADMINISTRATOR_ROLE -(SettingEventType.DISABLE_ADMINISTRATOR_ROLE)<br/>SAVE_MAIL_TEMPLATE -(SettingEventType.SAVE_MAIL_TEMPLATE)<br/>SAVE_MAIL_SERVICE -(SettingEventType.SAVE_MAIL_SERVICE)<br/>OFF_MAIL_SERVICE -(SettingEventType.OFF_MAIL_SERVICE)<br/>SAVE_SMS_SERVICE -(SettingEventType.SAVE_SMS_SERVICE)<br/>OFF_SMS_SERVICE -(SettingEventType.OFF_SMS_SERVICE)<br/>SAVE_GEO_LOCATION_SERVICE -(SettingEventType.SAVE_GEO_LOCATION_SERVICE)<br/>OFF_GEO_LOCATION_SERVICE -(SettingEventType.OFF_GEO_LOCATION_SERVICE)<br/>SAVE_STORAGE_SERVICE -(SettingEventType.SAVE_STORAGE_SERVICE)<br/>OFF_STORAGE_SERVICE -(SettingEventType.OFF_STORAGE_SERVICE)<br/>DOWN_LINE_SESSION -(MonitorEventType.DOWN_LINE_SESSION)<br/>ADD_APP_GROUP -(AppEventType.ADD_APP_GROUP)<br/>UPDATE_APP_GROUP -(AppEventType.UPDATE_APP_GROUP)<br/>DELETE_APP_GROUP -(AppEventType.DELETE_APP_GROUP)<br/>REMOVE_APP_GROUP_ASSOCIATION -(AppEventType.REMOVE_APP_GROUP_ASSOCIATION)<br/>SIGN_OUT_APP -(AppEventType.APP_SSO_LOGOUT)<br/>SAVE_PASSWORD_POLICY_SETTINGS -(SettingEventType.SAVE_PASSWORD_POLICY)<br/>SAVE_MFA_SETTINGS -(SettingEventType.SAVE_MFA_SETTINGS)<br/>SAVE_CAPTCHA_PROVIDER -(SettingEventType.SAVE_CAPTCHA_PROVIDER)<br/>|false|-
userType|string|用户类型|true|-
eventStatus|enum|事件状态<br/>SUCCESS -("success","成功")<br/>FAIL -("fail","失败")<br/>|false|-
startEventTime|string|事件开始时间|false|-
endEventTime|string|事件结束时间|false|-

**Request-example:**
```
curl -X GET -k -H 'token:${token}' -i https://linlan.net/emis_portal/api/audit/info/list.do?endTime=2025-10-11 22:39:08&startTime=2025-10-11 22:39:08&page=1&eventStatus=SUCCESS&username=lorine.rohan&orderBy=886&startEventTime=2025-10-11 22:39:19&endEventTime=2025-10-11 22:39:19&userType=03u366&q=wdvi7p&limit=10
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
success|boolean|成功标记:正常为true,错误为false|-
code|string|返回状态码:正常状态为0|-
msg|string|返回消息|-
data|array|返回数据对象|-
└─creatorId|string|创建人id|-
└─createTime|string|创建时间|-
└─lastTime|string|最后修改时间|-
└─id|string|主键ID|-
└─foreignId|string|外键ID|-
└─requestId|string|请求ID|-
└─sessionId|string|会话ID|-
└─eventType|enum|事件类型<br/>LOGIN_PORTAL -(PortalEventType.LOGIN_PORTAL)<br/>LOGIN_CONSOLE -(AuthenticationEventType.LOGIN_CONSOLE)<br/>LOGOUT_PORTAL -(PortalEventType.LOGOUT_PORTAL)<br/>LOGOUT_CONSOLE -(AuthenticationEventType.LOGOUT_CONSOLE)<br/>CREATE_ORG -(AccountEventType.CREATE_ORG)<br/>UPDATE_ORG -(AccountEventType.UPDATE_ORG)<br/>DELETE_ORG -(AccountEventType.DELETE_ORGANIZATION)<br/>MOVE_ORGANIZATION -(AccountEventType.MOVE_ORGANIZATION)<br/>CREATE_ORGANIZATION_MEMBER -(AccountEventType.CREATE_ORGANIZATION_MEMBER)<br/>CREATE_USER -(AccountEventType.CREATE_USER)<br/>UPDATE_USER -(AccountEventType.UPDATE_USER)<br/>DELETE_USER -(AccountEventType.DELETE_USER)<br/>ENABLE_USER -(AccountEventType.ENABLE_USER)<br/>DISABLE_USER -(AccountEventType.DISABLE_USER)<br/>USER_RESIGN -(AccountEventType.USER_RESIGN)<br/>MODIFY_USER_PHONE -(AccountEventType.MODIFY_USER_PHONE)<br/>MODIFY_USER_PASSWORD -(AccountEventType.MODIFY_USER_PASSWORD)<br/>UNLOCK_USER -(AccountEventType.UNLOCK_USER)<br/>MODIFY_ACCOUNT_INFO_PORTAL -(PortalEventType.MODIFY_ACCOUNT_INFO)<br/>MODIFY_USER_PASSWORD_PORTAL -(PortalEventType.MODIFY_USER_PASSWORD)<br/>MODIFY_USER_PHONE_PORTAL -(PortalEventType.MODIFY_USER_PHONE)<br/>MODIFY_USER_EMAIL_PORTAL -(PortalEventType.MODIFY_USER_EMAIL)<br/>BIND_MFA -(PortalEventType.BIND_MFA)<br/>UNBIND_MFA -(PortalEventType.UNBIND_MFA)<br/>BIND_IDP_USER -(PortalEventType.BIND_IDP_USER)<br/>UNBIND_IDP_USER -(PortalEventType.UNBIND_IDP_USER)<br/>PREPARE_MODIFY_PASSWORD -(PortalEventType.PREPARE_MODIFY_PASSWORD)<br/>PREPARE_MODIFY_MOBILE -(PortalEventType.PREPARE_MODIFY_MOBILE)<br/>PREPARE_MODIFY_EMAIL -(PortalEventType.PREPARE_MODIFY_EMAIL)<br/>PREPARE_BIND_MFA -(PortalEventType.PREPARE_BIND_MFA)<br/>CREATE_USER_GROUP -(AccountEventType.CREATE_USER_GROUP)<br/>UPDATE_USER_GROUP -(AccountEventType.UPDATE_USER_GROUP)<br/>DELETE_USER_GROUP -(AccountEventType.DELETE_USER_GROUP)<br/>ADD_USER_GROUP_MEMBER -(AccountEventType.ADD_USER_GROUP_MEMBER)<br/>REMOVE_USER_GROUP_MEMBER -(AccountEventType.REMOVE_USER_GROUP_MEMBER)<br/>CREATE_USER_DYNAMIC_GROUP -(AccountEventType.CREATE_USER_DYNAMIC_GROUP)<br/>UPDATE_USER_DYNAMIC_GROUP -(AccountEventType.UPDATE_USER_DYNAMIC_GROUP)<br/>DELETE_USER_DYNAMIC_GROUP -(AccountEventType.DELETE_USER_DYNAMIC_GROUP)<br/>UPDATE_USER_DYNAMIC_GROUP_FILTER_RULE -(AccountEventType.UPDATE_USER_DYNAMIC_GROUP_FILTER_RULE)<br/>CREATE_IDENTITY_RESOURCE -(AccountEventType.CREATE_IDENTITY_RESOURCE)<br/>UPDATE_IDENTITY_RESOURCE -(AccountEventType.UPDATE_IDENTITY_RESOURCE)<br/>DELETE_IDENTITY_RESOURCE -(AccountEventType.DELETE_IDENTITY_RESOURCE)<br/>ENABLE_IDENTITY_RESOURCE -(AccountEventType.ENABLE_IDENTITY_RESOURCE)<br/>DISABLE_IDENTITY_RESOURCE -(AccountEventType.DISABLE_IDENTITY_RESOURCE)<br/>IDENTITY_RESOURCE_SYNC -(AccountEventType.IDENTITY_RESOURCE_SYNC)<br/>ADD_IDP -(AuthenticationEventType.ADD_IDP)<br/>UPDATE_IDP -(AuthenticationEventType.UPDATE_IDP)<br/>DELETE_IDP -(AuthenticationEventType.DELETE_IDP)<br/>ENABLE_IDP -(AuthenticationEventType.ENABLE_IDP)<br/>DISABLE_IDP -(AuthenticationEventType.DISABLE_IDP)<br/>APP_SSO -(PortalEventType.APP_SSO)<br/>APP_SLO -(PortalEventType.APP_SLO)<br/>ADD_APP -(AppEventType.ADD_APP)<br/>ENABLE_APP -(AppEventType.ENABLE_APP)<br/>DISABLE_APP -(AppEventType.DISABLE_APP)<br/>UPDATE_APP -(AppEventType.UPDATE_APP)<br/>SAVE_APP_CONFIG -(AppEventType.SAVE_APP_CONFIG)<br/>DELETE_APP -(AppEventType.DELETE_APP)<br/>APP_AUTHORIZATION -(AppEventType.APP_ACCESS_POLICY)<br/>ENABLE_APP_ACCESS_POLICY -(AppEventType.ENABLE_APP_ACCESS_POLICY)<br/>DISABLE_APP_ACCESS_POLICY -(AppEventType.DISABLE_APP_ACCESS_POLICY)<br/>APP_CANCEL_ACCESS_POLICY -(AppEventType.APP_CANCEL_ACCESS_POLICY)<br/>ADD_APP_ACCOUNT -(AppEventType.ADD_APP_ACCOUNT)<br/>UPDATE_DEFAULT_APP_ACCOUNT -(AppEventType.UPDATE_DEFAULT_APP_ACCOUNT)<br/>DELETE_APP_ACCOUNT -(AppEventType.DELETE_APP_ACCOUNT)<br/>SAVE_SSO_CONFIG -(AppEventType.SAVE_SSO_CONFIG)<br/>SAVE_APP_PERMISSION_RESOURCE -(AppEventType.SAVE_APP_PERMISSION_RESOURCE)<br/>UPDATE_APP_PERMISSION_RESOURCE -(AppEventType.UPDATE_APP_PERMISSION_RESOURCE)<br/>DELETE_APP_PERMISSION_RESOURCE -(AppEventType.DELETE_APP_PERMISSION_RESOURCE)<br/>ENABLE_APP_PERMISSION_RESOURCE -(AppEventType.ENABLE_APP_PERMISSION_RESOURCE)<br/>DISABLE_APP_PERMISSION_RESOURCE -(AppEventType.DISABLE_APP_PERMISSION_RESOURCE)<br/>DELETE_USER_IDP_BIND -(AppEventType.DELETE_USER_IDP_BIND)<br/>SAVE_APP_PERMISSION_ROLE -(AppEventType.SAVE_APP_PERMISSION_ROLE)<br/>UPDATE_APP_PERMISSION_ROLE -(AppEventType.UPDATE_APP_PERMISSION_ROLE)<br/>DELETE_APP_PERMISSION_ROLE -(AppEventType.DELETE_APP_PERMISSION_ROLE)<br/>SAVE_LOGIN_SECURITY_BASIC_SETTINGS -(SettingEventType.SAVE_LOGIN_SECURITY_BASIC_SETTINGS)<br/>SAVE_SECURITY_POLICY_SETTINGS -(SettingEventType.SAVE_SECURITY_POLICY_SETTINGS)<br/>SAVE_PASSWORD_POLICY -(SettingEventType.SAVE_PASSWORD_POLICY)<br/>UPDATE_PASSWORD_POLICY -(SettingEventType.UPDATE_PASSWORD_POLICY)<br/>DELETE_PASSWORD_POLICY -(SettingEventType.DELETE_PASSWORD_POLICY)<br/>ENABLE_PASSWORD_POLICY -(SettingEventType.ENABLE_PASSWORD_POLICY)<br/>DISABLE_PASSWORD_POLICY -(SettingEventType.DISABLE_PASSWORD_POLICY)<br/>SORT_PASSWORD_POLICY -(SettingEventType.SORT_PASSWORD_POLICY)<br/>ADD_ADMINISTRATOR -(SettingEventType.ADD_ADMINISTRATOR)<br/>UPDATE_ADMINISTRATOR -(SettingEventType.UPDATE_ADMINISTRATOR)<br/>DELETE_ADMINISTRATOR -(SettingEventType.DELETE_ADMINISTRATOR)<br/>ENABLE_ADMINISTRATOR -(SettingEventType.ENABLE_ADMINISTRATOR)<br/>DISABLE_ADMINISTRATOR -(SettingEventType.DISABLE_ADMINISTRATOR)<br/>RESET_ADMINISTRATOR_PASSWORD -(SettingEventType.RESET_ADMINISTRATOR_PASSWORD)<br/>ADD_ADMINISTRATOR_ROLE -(SettingEventType.ADD_ADMINISTRATOR_ROLE)<br/>UPDATE_ADMINISTRATOR_ROLE -(SettingEventType.UPDATE_ADMINISTRATOR_ROLE)<br/>DELETE_ADMINISTRATOR_ROLE -(SettingEventType.DELETE_ADMINISTRATOR_ROLE)<br/>ENABLE_ADMINISTRATOR_ROLE -(SettingEventType.ENABLE_ADMINISTRATOR_ROLE)<br/>DISABLE_ADMINISTRATOR_ROLE -(SettingEventType.DISABLE_ADMINISTRATOR_ROLE)<br/>SAVE_MAIL_TEMPLATE -(SettingEventType.SAVE_MAIL_TEMPLATE)<br/>SAVE_MAIL_SERVICE -(SettingEventType.SAVE_MAIL_SERVICE)<br/>OFF_MAIL_SERVICE -(SettingEventType.OFF_MAIL_SERVICE)<br/>SAVE_SMS_SERVICE -(SettingEventType.SAVE_SMS_SERVICE)<br/>OFF_SMS_SERVICE -(SettingEventType.OFF_SMS_SERVICE)<br/>SAVE_GEO_LOCATION_SERVICE -(SettingEventType.SAVE_GEO_LOCATION_SERVICE)<br/>OFF_GEO_LOCATION_SERVICE -(SettingEventType.OFF_GEO_LOCATION_SERVICE)<br/>SAVE_STORAGE_SERVICE -(SettingEventType.SAVE_STORAGE_SERVICE)<br/>OFF_STORAGE_SERVICE -(SettingEventType.OFF_STORAGE_SERVICE)<br/>DOWN_LINE_SESSION -(MonitorEventType.DOWN_LINE_SESSION)<br/>ADD_APP_GROUP -(AppEventType.ADD_APP_GROUP)<br/>UPDATE_APP_GROUP -(AppEventType.UPDATE_APP_GROUP)<br/>DELETE_APP_GROUP -(AppEventType.DELETE_APP_GROUP)<br/>REMOVE_APP_GROUP_ASSOCIATION -(AppEventType.REMOVE_APP_GROUP_ASSOCIATION)<br/>SIGN_OUT_APP -(AppEventType.APP_SSO_LOGOUT)<br/>SAVE_PASSWORD_POLICY_SETTINGS -(SettingEventType.SAVE_PASSWORD_POLICY)<br/>SAVE_MFA_SETTINGS -(SettingEventType.SAVE_MFA_SETTINGS)<br/>SAVE_CAPTCHA_PROVIDER -(SettingEventType.SAVE_CAPTCHA_PROVIDER)<br/>|-
└─eventParam|string|事件参数|-
└─eventContent|string|事件内容|-
└─eventResult|string|事件结果|-
└─eventTime|string|事件时间|-
└─eventStatus|enum|事件状态<br/>SUCCESS -("success","成功")<br/>FAIL -("fail","失败")<br/>|-
└─actorId|string|操作者ID|-
└─actorType|string|操作者类型|-
└─actorAuthType|string|操作者认证类型|-
└─target|array|操作目标|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─id|string|目标 ID|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─name|string|目标名称|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─type|enum|目标类型<br/>USER -("user","用户")<br/>USER_DETAIL -("user_detail","用户详情")<br/>USER_GROUP -("user_group","用户组")<br/>USER_GROUP_MEMBER -("user_group_member","用户组成员")<br/>IDENTITY_SOURCE -("identity_source","身份源")<br/>ORGANIZATION -("organization","组织机构")<br/>APPLICATION -("application","应用")<br/>APPLICATION_ACCOUNT -("application_account","应用账户")<br/>SESSION -("session","会话管理")<br/>APP_PERMISSION_RESOURCE -("app_permission_resource","应用权限")<br/>APP_PERMISSION_POLICY -("app_permission_policy","应用权限策略")<br/>APP_PERMISSION_ROLE -("app_permission_role","应用权限角色")<br/>APP_GROUP -("app_group","应用分组")<br/>ROLE -("role","系统角色")<br/>ADMINISTRATOR -("administrator","管理员")<br/>PASSWORD_POLICY -("password_policy","密码策略")<br/>MAIL_TEMPLATE -("mail_template","邮件模版")<br/>IDENTITY_PROVIDER -("identity_provider","身份认证提供商")<br/>CONSOLE -("console","控制台")<br/>PORTAL -("portal","门户端")<br/>USER_IDP -("user_idp","用户绑定三方用户")<br/>|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─typeName|string|目标类型名称|-
└─userAgent|object|用户代理|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─deviceType|string|设备类型|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─platform|string|系统平台|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─platformVersion|string|平台版本|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─browser|string|浏览器|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─browserType|string|浏览器类型|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─browserMajorVersion|string|浏览器主版本|-
└─geoLocation|object|地理位置|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─ip|string|IP|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─continentCode|string|continent code|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─continentName|string|continent Name|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─countryCode|string|国家code|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─countryName|string|国家名称|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─provinceCode|string|省份code|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─provinceName|string|省份|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─cityCode|string|城市code|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─cityName|string|城市名称|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─point|object|地理坐标|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─lat|double|经度|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─lon|double|维度|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─provider|object|提供商|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─code|string|code|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─name|string|名称|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─downloadUrl|string|库文件下载地址|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─sha256Url|string|sha256校验文件下载地址|-
└─delFlag|int32|删除标记，0未删除1已删除|-
└─description|string|备注|-
└─spare1|string|备用1|-
└─spare2|string|备用2|-
requestId|string|请求ID|-
timestamp|string|返回时间戳字符串|-

**Response-example:**
```
{
  "success": true,
  "code": "0",
  "msg": "正常",
  "data": [
    {
      "creatorId": "19",
      "createTime": "2025-10-11 22:39:19",
      "lastTime": "2025-10-11 22:39:19",
      "id": "19",
      "foreignId": "19",
      "requestId": "19",
      "sessionId": "19",
      "eventType": "LOGIN_PORTAL",
      "eventParam": "evr0lz",
      "eventContent": "crdf72",
      "eventResult": "b7yzpf",
      "eventTime": "2025-10-11 22:39:19",
      "eventStatus": "SUCCESS",
      "actorId": "19",
      "actorType": "47ol9w",
      "actorAuthType": "yusa6k",
      "target": [
        {
          "id": "19",
          "name": "lorine.rohan",
          "type": "USER",
          "typeName": "lorine.rohan"
        }
      ],
      "userAgent": {
        "deviceType": "edxu3g",
        "platform": "3kz3ff",
        "platformVersion": "0.61",
        "browser": "vuspz9",
        "browserType": "t6ioxi",
        "browserMajorVersion": "0.61"
      },
      "geoLocation": {
        "ip": "207.171.28.69",
        "continentCode": "59282",
        "continentName": "lorine.rohan",
        "countryCode": "59282",
        "countryName": "lorine.rohan",
        "provinceCode": "59282",
        "provinceName": "lorine.rohan",
        "cityCode": "59282",
        "cityName": "lorine.rohan",
        "point": {
          "$ref": "..."
        },
        "provider": {
          "$ref": "..."
        }
      },
      "delFlag": 4,
      "description": "d0cn09",
      "spare1": "9rqos3",
      "spare2": "8w7bp0"
    }
  ],
  "requestId": "19",
  "timestamp": "2025-10-11 22:39:08"
}
```

### Get AuditInfo by id. 主键获取行为审计详细信息.
**URL:** https://linlan.net/emis_portal/api/audit/info/{id}.do

**Type:** GET

**Author:** Linlan
CreateTime 2025-09-06 15:55:04

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** Get AuditInfo by id. 主键获取行为审计详细信息.

**Request-headers:**

Header | Type|Description|Required|Since
---|---|---|---|----
token|string|令牌|true|-


**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|string|主键ID|true|-

**Request-example:**
```
curl -X GET -k -H 'token:${token}' -i https://linlan.net/emis_portal/api/audit/info/1.do
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
success|boolean|成功标记:正常为true,错误为false|-
code|string|返回状态码:正常状态为0|-
msg|string|返回消息|-
data|object|返回数据对象|-
└─creatorId|string|创建人id|-
└─createTime|string|创建时间|-
└─lastTime|string|最后修改时间|-
└─id|string|主键ID|-
└─foreignId|string|外键ID|-
└─requestId|string|请求ID|-
└─sessionId|string|会话ID|-
└─eventType|enum|事件类型<br/>LOGIN_PORTAL -(PortalEventType.LOGIN_PORTAL)<br/>LOGIN_CONSOLE -(AuthenticationEventType.LOGIN_CONSOLE)<br/>LOGOUT_PORTAL -(PortalEventType.LOGOUT_PORTAL)<br/>LOGOUT_CONSOLE -(AuthenticationEventType.LOGOUT_CONSOLE)<br/>CREATE_ORG -(AccountEventType.CREATE_ORG)<br/>UPDATE_ORG -(AccountEventType.UPDATE_ORG)<br/>DELETE_ORG -(AccountEventType.DELETE_ORGANIZATION)<br/>MOVE_ORGANIZATION -(AccountEventType.MOVE_ORGANIZATION)<br/>CREATE_ORGANIZATION_MEMBER -(AccountEventType.CREATE_ORGANIZATION_MEMBER)<br/>CREATE_USER -(AccountEventType.CREATE_USER)<br/>UPDATE_USER -(AccountEventType.UPDATE_USER)<br/>DELETE_USER -(AccountEventType.DELETE_USER)<br/>ENABLE_USER -(AccountEventType.ENABLE_USER)<br/>DISABLE_USER -(AccountEventType.DISABLE_USER)<br/>USER_RESIGN -(AccountEventType.USER_RESIGN)<br/>MODIFY_USER_PHONE -(AccountEventType.MODIFY_USER_PHONE)<br/>MODIFY_USER_PASSWORD -(AccountEventType.MODIFY_USER_PASSWORD)<br/>UNLOCK_USER -(AccountEventType.UNLOCK_USER)<br/>MODIFY_ACCOUNT_INFO_PORTAL -(PortalEventType.MODIFY_ACCOUNT_INFO)<br/>MODIFY_USER_PASSWORD_PORTAL -(PortalEventType.MODIFY_USER_PASSWORD)<br/>MODIFY_USER_PHONE_PORTAL -(PortalEventType.MODIFY_USER_PHONE)<br/>MODIFY_USER_EMAIL_PORTAL -(PortalEventType.MODIFY_USER_EMAIL)<br/>BIND_MFA -(PortalEventType.BIND_MFA)<br/>UNBIND_MFA -(PortalEventType.UNBIND_MFA)<br/>BIND_IDP_USER -(PortalEventType.BIND_IDP_USER)<br/>UNBIND_IDP_USER -(PortalEventType.UNBIND_IDP_USER)<br/>PREPARE_MODIFY_PASSWORD -(PortalEventType.PREPARE_MODIFY_PASSWORD)<br/>PREPARE_MODIFY_MOBILE -(PortalEventType.PREPARE_MODIFY_MOBILE)<br/>PREPARE_MODIFY_EMAIL -(PortalEventType.PREPARE_MODIFY_EMAIL)<br/>PREPARE_BIND_MFA -(PortalEventType.PREPARE_BIND_MFA)<br/>CREATE_USER_GROUP -(AccountEventType.CREATE_USER_GROUP)<br/>UPDATE_USER_GROUP -(AccountEventType.UPDATE_USER_GROUP)<br/>DELETE_USER_GROUP -(AccountEventType.DELETE_USER_GROUP)<br/>ADD_USER_GROUP_MEMBER -(AccountEventType.ADD_USER_GROUP_MEMBER)<br/>REMOVE_USER_GROUP_MEMBER -(AccountEventType.REMOVE_USER_GROUP_MEMBER)<br/>CREATE_USER_DYNAMIC_GROUP -(AccountEventType.CREATE_USER_DYNAMIC_GROUP)<br/>UPDATE_USER_DYNAMIC_GROUP -(AccountEventType.UPDATE_USER_DYNAMIC_GROUP)<br/>DELETE_USER_DYNAMIC_GROUP -(AccountEventType.DELETE_USER_DYNAMIC_GROUP)<br/>UPDATE_USER_DYNAMIC_GROUP_FILTER_RULE -(AccountEventType.UPDATE_USER_DYNAMIC_GROUP_FILTER_RULE)<br/>CREATE_IDENTITY_RESOURCE -(AccountEventType.CREATE_IDENTITY_RESOURCE)<br/>UPDATE_IDENTITY_RESOURCE -(AccountEventType.UPDATE_IDENTITY_RESOURCE)<br/>DELETE_IDENTITY_RESOURCE -(AccountEventType.DELETE_IDENTITY_RESOURCE)<br/>ENABLE_IDENTITY_RESOURCE -(AccountEventType.ENABLE_IDENTITY_RESOURCE)<br/>DISABLE_IDENTITY_RESOURCE -(AccountEventType.DISABLE_IDENTITY_RESOURCE)<br/>IDENTITY_RESOURCE_SYNC -(AccountEventType.IDENTITY_RESOURCE_SYNC)<br/>ADD_IDP -(AuthenticationEventType.ADD_IDP)<br/>UPDATE_IDP -(AuthenticationEventType.UPDATE_IDP)<br/>DELETE_IDP -(AuthenticationEventType.DELETE_IDP)<br/>ENABLE_IDP -(AuthenticationEventType.ENABLE_IDP)<br/>DISABLE_IDP -(AuthenticationEventType.DISABLE_IDP)<br/>APP_SSO -(PortalEventType.APP_SSO)<br/>APP_SLO -(PortalEventType.APP_SLO)<br/>ADD_APP -(AppEventType.ADD_APP)<br/>ENABLE_APP -(AppEventType.ENABLE_APP)<br/>DISABLE_APP -(AppEventType.DISABLE_APP)<br/>UPDATE_APP -(AppEventType.UPDATE_APP)<br/>SAVE_APP_CONFIG -(AppEventType.SAVE_APP_CONFIG)<br/>DELETE_APP -(AppEventType.DELETE_APP)<br/>APP_AUTHORIZATION -(AppEventType.APP_ACCESS_POLICY)<br/>ENABLE_APP_ACCESS_POLICY -(AppEventType.ENABLE_APP_ACCESS_POLICY)<br/>DISABLE_APP_ACCESS_POLICY -(AppEventType.DISABLE_APP_ACCESS_POLICY)<br/>APP_CANCEL_ACCESS_POLICY -(AppEventType.APP_CANCEL_ACCESS_POLICY)<br/>ADD_APP_ACCOUNT -(AppEventType.ADD_APP_ACCOUNT)<br/>UPDATE_DEFAULT_APP_ACCOUNT -(AppEventType.UPDATE_DEFAULT_APP_ACCOUNT)<br/>DELETE_APP_ACCOUNT -(AppEventType.DELETE_APP_ACCOUNT)<br/>SAVE_SSO_CONFIG -(AppEventType.SAVE_SSO_CONFIG)<br/>SAVE_APP_PERMISSION_RESOURCE -(AppEventType.SAVE_APP_PERMISSION_RESOURCE)<br/>UPDATE_APP_PERMISSION_RESOURCE -(AppEventType.UPDATE_APP_PERMISSION_RESOURCE)<br/>DELETE_APP_PERMISSION_RESOURCE -(AppEventType.DELETE_APP_PERMISSION_RESOURCE)<br/>ENABLE_APP_PERMISSION_RESOURCE -(AppEventType.ENABLE_APP_PERMISSION_RESOURCE)<br/>DISABLE_APP_PERMISSION_RESOURCE -(AppEventType.DISABLE_APP_PERMISSION_RESOURCE)<br/>DELETE_USER_IDP_BIND -(AppEventType.DELETE_USER_IDP_BIND)<br/>SAVE_APP_PERMISSION_ROLE -(AppEventType.SAVE_APP_PERMISSION_ROLE)<br/>UPDATE_APP_PERMISSION_ROLE -(AppEventType.UPDATE_APP_PERMISSION_ROLE)<br/>DELETE_APP_PERMISSION_ROLE -(AppEventType.DELETE_APP_PERMISSION_ROLE)<br/>SAVE_LOGIN_SECURITY_BASIC_SETTINGS -(SettingEventType.SAVE_LOGIN_SECURITY_BASIC_SETTINGS)<br/>SAVE_SECURITY_POLICY_SETTINGS -(SettingEventType.SAVE_SECURITY_POLICY_SETTINGS)<br/>SAVE_PASSWORD_POLICY -(SettingEventType.SAVE_PASSWORD_POLICY)<br/>UPDATE_PASSWORD_POLICY -(SettingEventType.UPDATE_PASSWORD_POLICY)<br/>DELETE_PASSWORD_POLICY -(SettingEventType.DELETE_PASSWORD_POLICY)<br/>ENABLE_PASSWORD_POLICY -(SettingEventType.ENABLE_PASSWORD_POLICY)<br/>DISABLE_PASSWORD_POLICY -(SettingEventType.DISABLE_PASSWORD_POLICY)<br/>SORT_PASSWORD_POLICY -(SettingEventType.SORT_PASSWORD_POLICY)<br/>ADD_ADMINISTRATOR -(SettingEventType.ADD_ADMINISTRATOR)<br/>UPDATE_ADMINISTRATOR -(SettingEventType.UPDATE_ADMINISTRATOR)<br/>DELETE_ADMINISTRATOR -(SettingEventType.DELETE_ADMINISTRATOR)<br/>ENABLE_ADMINISTRATOR -(SettingEventType.ENABLE_ADMINISTRATOR)<br/>DISABLE_ADMINISTRATOR -(SettingEventType.DISABLE_ADMINISTRATOR)<br/>RESET_ADMINISTRATOR_PASSWORD -(SettingEventType.RESET_ADMINISTRATOR_PASSWORD)<br/>ADD_ADMINISTRATOR_ROLE -(SettingEventType.ADD_ADMINISTRATOR_ROLE)<br/>UPDATE_ADMINISTRATOR_ROLE -(SettingEventType.UPDATE_ADMINISTRATOR_ROLE)<br/>DELETE_ADMINISTRATOR_ROLE -(SettingEventType.DELETE_ADMINISTRATOR_ROLE)<br/>ENABLE_ADMINISTRATOR_ROLE -(SettingEventType.ENABLE_ADMINISTRATOR_ROLE)<br/>DISABLE_ADMINISTRATOR_ROLE -(SettingEventType.DISABLE_ADMINISTRATOR_ROLE)<br/>SAVE_MAIL_TEMPLATE -(SettingEventType.SAVE_MAIL_TEMPLATE)<br/>SAVE_MAIL_SERVICE -(SettingEventType.SAVE_MAIL_SERVICE)<br/>OFF_MAIL_SERVICE -(SettingEventType.OFF_MAIL_SERVICE)<br/>SAVE_SMS_SERVICE -(SettingEventType.SAVE_SMS_SERVICE)<br/>OFF_SMS_SERVICE -(SettingEventType.OFF_SMS_SERVICE)<br/>SAVE_GEO_LOCATION_SERVICE -(SettingEventType.SAVE_GEO_LOCATION_SERVICE)<br/>OFF_GEO_LOCATION_SERVICE -(SettingEventType.OFF_GEO_LOCATION_SERVICE)<br/>SAVE_STORAGE_SERVICE -(SettingEventType.SAVE_STORAGE_SERVICE)<br/>OFF_STORAGE_SERVICE -(SettingEventType.OFF_STORAGE_SERVICE)<br/>DOWN_LINE_SESSION -(MonitorEventType.DOWN_LINE_SESSION)<br/>ADD_APP_GROUP -(AppEventType.ADD_APP_GROUP)<br/>UPDATE_APP_GROUP -(AppEventType.UPDATE_APP_GROUP)<br/>DELETE_APP_GROUP -(AppEventType.DELETE_APP_GROUP)<br/>REMOVE_APP_GROUP_ASSOCIATION -(AppEventType.REMOVE_APP_GROUP_ASSOCIATION)<br/>SIGN_OUT_APP -(AppEventType.APP_SSO_LOGOUT)<br/>SAVE_PASSWORD_POLICY_SETTINGS -(SettingEventType.SAVE_PASSWORD_POLICY)<br/>SAVE_MFA_SETTINGS -(SettingEventType.SAVE_MFA_SETTINGS)<br/>SAVE_CAPTCHA_PROVIDER -(SettingEventType.SAVE_CAPTCHA_PROVIDER)<br/>|-
└─eventParam|string|事件参数|-
└─eventContent|string|事件内容|-
└─eventResult|string|事件结果|-
└─eventTime|string|事件时间|-
└─eventStatus|enum|事件状态<br/>SUCCESS -("success","成功")<br/>FAIL -("fail","失败")<br/>|-
└─actorId|string|操作者ID|-
└─actorType|string|操作者类型|-
└─actorAuthType|string|操作者认证类型|-
└─target|array|操作目标|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─id|string|目标 ID|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─name|string|目标名称|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─type|enum|目标类型<br/>USER -("user","用户")<br/>USER_DETAIL -("user_detail","用户详情")<br/>USER_GROUP -("user_group","用户组")<br/>USER_GROUP_MEMBER -("user_group_member","用户组成员")<br/>IDENTITY_SOURCE -("identity_source","身份源")<br/>ORGANIZATION -("organization","组织机构")<br/>APPLICATION -("application","应用")<br/>APPLICATION_ACCOUNT -("application_account","应用账户")<br/>SESSION -("session","会话管理")<br/>APP_PERMISSION_RESOURCE -("app_permission_resource","应用权限")<br/>APP_PERMISSION_POLICY -("app_permission_policy","应用权限策略")<br/>APP_PERMISSION_ROLE -("app_permission_role","应用权限角色")<br/>APP_GROUP -("app_group","应用分组")<br/>ROLE -("role","系统角色")<br/>ADMINISTRATOR -("administrator","管理员")<br/>PASSWORD_POLICY -("password_policy","密码策略")<br/>MAIL_TEMPLATE -("mail_template","邮件模版")<br/>IDENTITY_PROVIDER -("identity_provider","身份认证提供商")<br/>CONSOLE -("console","控制台")<br/>PORTAL -("portal","门户端")<br/>USER_IDP -("user_idp","用户绑定三方用户")<br/>|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─typeName|string|目标类型名称|-
└─userAgent|object|用户代理|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─deviceType|string|设备类型|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─platform|string|系统平台|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─platformVersion|string|平台版本|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─browser|string|浏览器|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─browserType|string|浏览器类型|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─browserMajorVersion|string|浏览器主版本|-
└─geoLocation|object|地理位置|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─ip|string|IP|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─continentCode|string|continent code|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─continentName|string|continent Name|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─countryCode|string|国家code|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─countryName|string|国家名称|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─provinceCode|string|省份code|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─provinceName|string|省份|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─cityCode|string|城市code|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─cityName|string|城市名称|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─point|object|地理坐标|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─lat|double|经度|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─lon|double|维度|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─provider|object|提供商|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─code|string|code|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─name|string|名称|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─downloadUrl|string|库文件下载地址|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─sha256Url|string|sha256校验文件下载地址|-
└─delFlag|int32|删除标记，0未删除1已删除|-
└─description|string|备注|-
└─spare1|string|备用1|-
└─spare2|string|备用2|-
requestId|string|请求ID|-
timestamp|string|返回时间戳字符串|-

**Response-example:**
```
{
  "success": true,
  "code": "0",
  "msg": "正常",
  "data": {
    "creatorId": "19",
    "createTime": "2025-10-11 22:39:19",
    "lastTime": "2025-10-11 22:39:19",
    "id": "19",
    "foreignId": "19",
    "requestId": "19",
    "sessionId": "19",
    "eventType": "LOGIN_PORTAL",
    "eventParam": "jdnqbl",
    "eventContent": "r9lad7",
    "eventResult": "hzr0sp",
    "eventTime": "2025-10-11 22:39:19",
    "eventStatus": "SUCCESS",
    "actorId": "19",
    "actorType": "3ivrp9",
    "actorAuthType": "z1pero",
    "target": [
      {
        "id": "19",
        "name": "lorine.rohan",
        "type": "USER",
        "typeName": "lorine.rohan"
      }
    ],
    "userAgent": {
      "deviceType": "5pic9v",
      "platform": "wbnufb",
      "platformVersion": "0.61",
      "browser": "o73nfq",
      "browserType": "f4z5wt",
      "browserMajorVersion": "0.61"
    },
    "geoLocation": {
      "ip": "207.171.28.69",
      "continentCode": "59282",
      "continentName": "lorine.rohan",
      "countryCode": "59282",
      "countryName": "lorine.rohan",
      "provinceCode": "59282",
      "provinceName": "lorine.rohan",
      "cityCode": "59282",
      "cityName": "lorine.rohan",
      "point": {
        "lat": 82.17,
        "lon": 9.69
      },
      "provider": {
        "code": "59282",
        "name": "lorine.rohan",
        "downloadUrl": "www.vida-hills.name",
        "sha256Url": "www.vida-hills.name"
      }
    },
    "delFlag": 4,
    "description": "llsanu",
    "spare1": "vbooy0",
    "spare2": "z3q5dd"
  },
  "requestId": "19",
  "timestamp": "2025-10-11 22:39:08"
}
```

### AuditInfo Operation. 新增行为审计数据操作逻辑，根据操作类型，执行新增操作.
**URL:** https://linlan.net/emis_portal/api/audit/info/save.do

**Type:** POST

**Author:** Linlan
CreateTime 2025-09-06 15:55:04

**Content-Type:** application/json; charset=utf-8

**Description:** AuditInfo Operation. 新增行为审计数据操作逻辑，根据操作类型，执行新增操作.

**Request-headers:**

Header | Type|Description|Required|Since
---|---|---|---|----
token|string|令牌|true|-


**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
data|object|请求数据对象，放入RequestBody内|false|-
└─creatorId|string|创建人id|false|-
└─createTime|string|创建时间|false|-
└─lastTime|string|最后修改时间|false|-
└─id|string|主键ID|false|-
└─foreignId|string|外键ID|false|-
└─requestId|string|请求ID|false|-
└─sessionId|string|会话ID|false|-
└─eventType|enum|事件类型<br/>LOGIN_PORTAL -(PortalEventType.LOGIN_PORTAL)<br/>LOGIN_CONSOLE -(AuthenticationEventType.LOGIN_CONSOLE)<br/>LOGOUT_PORTAL -(PortalEventType.LOGOUT_PORTAL)<br/>LOGOUT_CONSOLE -(AuthenticationEventType.LOGOUT_CONSOLE)<br/>CREATE_ORG -(AccountEventType.CREATE_ORG)<br/>UPDATE_ORG -(AccountEventType.UPDATE_ORG)<br/>DELETE_ORG -(AccountEventType.DELETE_ORGANIZATION)<br/>MOVE_ORGANIZATION -(AccountEventType.MOVE_ORGANIZATION)<br/>CREATE_ORGANIZATION_MEMBER -(AccountEventType.CREATE_ORGANIZATION_MEMBER)<br/>CREATE_USER -(AccountEventType.CREATE_USER)<br/>UPDATE_USER -(AccountEventType.UPDATE_USER)<br/>DELETE_USER -(AccountEventType.DELETE_USER)<br/>ENABLE_USER -(AccountEventType.ENABLE_USER)<br/>DISABLE_USER -(AccountEventType.DISABLE_USER)<br/>USER_RESIGN -(AccountEventType.USER_RESIGN)<br/>MODIFY_USER_PHONE -(AccountEventType.MODIFY_USER_PHONE)<br/>MODIFY_USER_PASSWORD -(AccountEventType.MODIFY_USER_PASSWORD)<br/>UNLOCK_USER -(AccountEventType.UNLOCK_USER)<br/>MODIFY_ACCOUNT_INFO_PORTAL -(PortalEventType.MODIFY_ACCOUNT_INFO)<br/>MODIFY_USER_PASSWORD_PORTAL -(PortalEventType.MODIFY_USER_PASSWORD)<br/>MODIFY_USER_PHONE_PORTAL -(PortalEventType.MODIFY_USER_PHONE)<br/>MODIFY_USER_EMAIL_PORTAL -(PortalEventType.MODIFY_USER_EMAIL)<br/>BIND_MFA -(PortalEventType.BIND_MFA)<br/>UNBIND_MFA -(PortalEventType.UNBIND_MFA)<br/>BIND_IDP_USER -(PortalEventType.BIND_IDP_USER)<br/>UNBIND_IDP_USER -(PortalEventType.UNBIND_IDP_USER)<br/>PREPARE_MODIFY_PASSWORD -(PortalEventType.PREPARE_MODIFY_PASSWORD)<br/>PREPARE_MODIFY_MOBILE -(PortalEventType.PREPARE_MODIFY_MOBILE)<br/>PREPARE_MODIFY_EMAIL -(PortalEventType.PREPARE_MODIFY_EMAIL)<br/>PREPARE_BIND_MFA -(PortalEventType.PREPARE_BIND_MFA)<br/>CREATE_USER_GROUP -(AccountEventType.CREATE_USER_GROUP)<br/>UPDATE_USER_GROUP -(AccountEventType.UPDATE_USER_GROUP)<br/>DELETE_USER_GROUP -(AccountEventType.DELETE_USER_GROUP)<br/>ADD_USER_GROUP_MEMBER -(AccountEventType.ADD_USER_GROUP_MEMBER)<br/>REMOVE_USER_GROUP_MEMBER -(AccountEventType.REMOVE_USER_GROUP_MEMBER)<br/>CREATE_USER_DYNAMIC_GROUP -(AccountEventType.CREATE_USER_DYNAMIC_GROUP)<br/>UPDATE_USER_DYNAMIC_GROUP -(AccountEventType.UPDATE_USER_DYNAMIC_GROUP)<br/>DELETE_USER_DYNAMIC_GROUP -(AccountEventType.DELETE_USER_DYNAMIC_GROUP)<br/>UPDATE_USER_DYNAMIC_GROUP_FILTER_RULE -(AccountEventType.UPDATE_USER_DYNAMIC_GROUP_FILTER_RULE)<br/>CREATE_IDENTITY_RESOURCE -(AccountEventType.CREATE_IDENTITY_RESOURCE)<br/>UPDATE_IDENTITY_RESOURCE -(AccountEventType.UPDATE_IDENTITY_RESOURCE)<br/>DELETE_IDENTITY_RESOURCE -(AccountEventType.DELETE_IDENTITY_RESOURCE)<br/>ENABLE_IDENTITY_RESOURCE -(AccountEventType.ENABLE_IDENTITY_RESOURCE)<br/>DISABLE_IDENTITY_RESOURCE -(AccountEventType.DISABLE_IDENTITY_RESOURCE)<br/>IDENTITY_RESOURCE_SYNC -(AccountEventType.IDENTITY_RESOURCE_SYNC)<br/>ADD_IDP -(AuthenticationEventType.ADD_IDP)<br/>UPDATE_IDP -(AuthenticationEventType.UPDATE_IDP)<br/>DELETE_IDP -(AuthenticationEventType.DELETE_IDP)<br/>ENABLE_IDP -(AuthenticationEventType.ENABLE_IDP)<br/>DISABLE_IDP -(AuthenticationEventType.DISABLE_IDP)<br/>APP_SSO -(PortalEventType.APP_SSO)<br/>APP_SLO -(PortalEventType.APP_SLO)<br/>ADD_APP -(AppEventType.ADD_APP)<br/>ENABLE_APP -(AppEventType.ENABLE_APP)<br/>DISABLE_APP -(AppEventType.DISABLE_APP)<br/>UPDATE_APP -(AppEventType.UPDATE_APP)<br/>SAVE_APP_CONFIG -(AppEventType.SAVE_APP_CONFIG)<br/>DELETE_APP -(AppEventType.DELETE_APP)<br/>APP_AUTHORIZATION -(AppEventType.APP_ACCESS_POLICY)<br/>ENABLE_APP_ACCESS_POLICY -(AppEventType.ENABLE_APP_ACCESS_POLICY)<br/>DISABLE_APP_ACCESS_POLICY -(AppEventType.DISABLE_APP_ACCESS_POLICY)<br/>APP_CANCEL_ACCESS_POLICY -(AppEventType.APP_CANCEL_ACCESS_POLICY)<br/>ADD_APP_ACCOUNT -(AppEventType.ADD_APP_ACCOUNT)<br/>UPDATE_DEFAULT_APP_ACCOUNT -(AppEventType.UPDATE_DEFAULT_APP_ACCOUNT)<br/>DELETE_APP_ACCOUNT -(AppEventType.DELETE_APP_ACCOUNT)<br/>SAVE_SSO_CONFIG -(AppEventType.SAVE_SSO_CONFIG)<br/>SAVE_APP_PERMISSION_RESOURCE -(AppEventType.SAVE_APP_PERMISSION_RESOURCE)<br/>UPDATE_APP_PERMISSION_RESOURCE -(AppEventType.UPDATE_APP_PERMISSION_RESOURCE)<br/>DELETE_APP_PERMISSION_RESOURCE -(AppEventType.DELETE_APP_PERMISSION_RESOURCE)<br/>ENABLE_APP_PERMISSION_RESOURCE -(AppEventType.ENABLE_APP_PERMISSION_RESOURCE)<br/>DISABLE_APP_PERMISSION_RESOURCE -(AppEventType.DISABLE_APP_PERMISSION_RESOURCE)<br/>DELETE_USER_IDP_BIND -(AppEventType.DELETE_USER_IDP_BIND)<br/>SAVE_APP_PERMISSION_ROLE -(AppEventType.SAVE_APP_PERMISSION_ROLE)<br/>UPDATE_APP_PERMISSION_ROLE -(AppEventType.UPDATE_APP_PERMISSION_ROLE)<br/>DELETE_APP_PERMISSION_ROLE -(AppEventType.DELETE_APP_PERMISSION_ROLE)<br/>SAVE_LOGIN_SECURITY_BASIC_SETTINGS -(SettingEventType.SAVE_LOGIN_SECURITY_BASIC_SETTINGS)<br/>SAVE_SECURITY_POLICY_SETTINGS -(SettingEventType.SAVE_SECURITY_POLICY_SETTINGS)<br/>SAVE_PASSWORD_POLICY -(SettingEventType.SAVE_PASSWORD_POLICY)<br/>UPDATE_PASSWORD_POLICY -(SettingEventType.UPDATE_PASSWORD_POLICY)<br/>DELETE_PASSWORD_POLICY -(SettingEventType.DELETE_PASSWORD_POLICY)<br/>ENABLE_PASSWORD_POLICY -(SettingEventType.ENABLE_PASSWORD_POLICY)<br/>DISABLE_PASSWORD_POLICY -(SettingEventType.DISABLE_PASSWORD_POLICY)<br/>SORT_PASSWORD_POLICY -(SettingEventType.SORT_PASSWORD_POLICY)<br/>ADD_ADMINISTRATOR -(SettingEventType.ADD_ADMINISTRATOR)<br/>UPDATE_ADMINISTRATOR -(SettingEventType.UPDATE_ADMINISTRATOR)<br/>DELETE_ADMINISTRATOR -(SettingEventType.DELETE_ADMINISTRATOR)<br/>ENABLE_ADMINISTRATOR -(SettingEventType.ENABLE_ADMINISTRATOR)<br/>DISABLE_ADMINISTRATOR -(SettingEventType.DISABLE_ADMINISTRATOR)<br/>RESET_ADMINISTRATOR_PASSWORD -(SettingEventType.RESET_ADMINISTRATOR_PASSWORD)<br/>ADD_ADMINISTRATOR_ROLE -(SettingEventType.ADD_ADMINISTRATOR_ROLE)<br/>UPDATE_ADMINISTRATOR_ROLE -(SettingEventType.UPDATE_ADMINISTRATOR_ROLE)<br/>DELETE_ADMINISTRATOR_ROLE -(SettingEventType.DELETE_ADMINISTRATOR_ROLE)<br/>ENABLE_ADMINISTRATOR_ROLE -(SettingEventType.ENABLE_ADMINISTRATOR_ROLE)<br/>DISABLE_ADMINISTRATOR_ROLE -(SettingEventType.DISABLE_ADMINISTRATOR_ROLE)<br/>SAVE_MAIL_TEMPLATE -(SettingEventType.SAVE_MAIL_TEMPLATE)<br/>SAVE_MAIL_SERVICE -(SettingEventType.SAVE_MAIL_SERVICE)<br/>OFF_MAIL_SERVICE -(SettingEventType.OFF_MAIL_SERVICE)<br/>SAVE_SMS_SERVICE -(SettingEventType.SAVE_SMS_SERVICE)<br/>OFF_SMS_SERVICE -(SettingEventType.OFF_SMS_SERVICE)<br/>SAVE_GEO_LOCATION_SERVICE -(SettingEventType.SAVE_GEO_LOCATION_SERVICE)<br/>OFF_GEO_LOCATION_SERVICE -(SettingEventType.OFF_GEO_LOCATION_SERVICE)<br/>SAVE_STORAGE_SERVICE -(SettingEventType.SAVE_STORAGE_SERVICE)<br/>OFF_STORAGE_SERVICE -(SettingEventType.OFF_STORAGE_SERVICE)<br/>DOWN_LINE_SESSION -(MonitorEventType.DOWN_LINE_SESSION)<br/>ADD_APP_GROUP -(AppEventType.ADD_APP_GROUP)<br/>UPDATE_APP_GROUP -(AppEventType.UPDATE_APP_GROUP)<br/>DELETE_APP_GROUP -(AppEventType.DELETE_APP_GROUP)<br/>REMOVE_APP_GROUP_ASSOCIATION -(AppEventType.REMOVE_APP_GROUP_ASSOCIATION)<br/>SIGN_OUT_APP -(AppEventType.APP_SSO_LOGOUT)<br/>SAVE_PASSWORD_POLICY_SETTINGS -(SettingEventType.SAVE_PASSWORD_POLICY)<br/>SAVE_MFA_SETTINGS -(SettingEventType.SAVE_MFA_SETTINGS)<br/>SAVE_CAPTCHA_PROVIDER -(SettingEventType.SAVE_CAPTCHA_PROVIDER)<br/>|false|-
└─eventParam|string|事件参数|false|-
└─eventContent|string|事件内容|false|-
└─eventResult|string|事件结果|false|-
└─eventTime|string|事件时间|false|-
└─eventStatus|enum|事件状态<br/>SUCCESS -("success","成功")<br/>FAIL -("fail","失败")<br/>|false|-
└─actorId|string|操作者ID|false|-
└─actorType|string|操作者类型|false|-
└─actorAuthType|string|操作者认证类型|false|-
└─target|array|操作目标|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─id|string|目标 ID|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─name|string|目标名称|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─type|enum|目标类型<br/>USER -("user","用户")<br/>USER_DETAIL -("user_detail","用户详情")<br/>USER_GROUP -("user_group","用户组")<br/>USER_GROUP_MEMBER -("user_group_member","用户组成员")<br/>IDENTITY_SOURCE -("identity_source","身份源")<br/>ORGANIZATION -("organization","组织机构")<br/>APPLICATION -("application","应用")<br/>APPLICATION_ACCOUNT -("application_account","应用账户")<br/>SESSION -("session","会话管理")<br/>APP_PERMISSION_RESOURCE -("app_permission_resource","应用权限")<br/>APP_PERMISSION_POLICY -("app_permission_policy","应用权限策略")<br/>APP_PERMISSION_ROLE -("app_permission_role","应用权限角色")<br/>APP_GROUP -("app_group","应用分组")<br/>ROLE -("role","系统角色")<br/>ADMINISTRATOR -("administrator","管理员")<br/>PASSWORD_POLICY -("password_policy","密码策略")<br/>MAIL_TEMPLATE -("mail_template","邮件模版")<br/>IDENTITY_PROVIDER -("identity_provider","身份认证提供商")<br/>CONSOLE -("console","控制台")<br/>PORTAL -("portal","门户端")<br/>USER_IDP -("user_idp","用户绑定三方用户")<br/>|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─typeName|string|目标类型名称|false|-
└─userAgent|object|用户代理|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─deviceType|string|设备类型|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─platform|string|系统平台|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─platformVersion|string|平台版本|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─browser|string|浏览器|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─browserType|string|浏览器类型|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─browserMajorVersion|string|浏览器主版本|false|-
└─geoLocation|object|地理位置|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─ip|string|IP|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─continentCode|string|continent code|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─continentName|string|continent Name|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─countryCode|string|国家code|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─countryName|string|国家名称|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─provinceCode|string|省份code|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─provinceName|string|省份|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─cityCode|string|城市code|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─cityName|string|城市名称|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─point|object|地理坐标|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─lat|double|经度|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─lon|double|维度|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─provider|object|提供商|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─code|string|code|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─name|string|名称|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─downloadUrl|string|库文件下载地址|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─sha256Url|string|sha256校验文件下载地址|false|-
└─delFlag|int32|删除标记，0未删除1已删除|false|-
└─description|string|备注|false|-
└─spare1|string|备用1|false|-
└─spare2|string|备用2|false|-

**Request-example:**
```
curl -X POST -k -H 'Content-Type: application/json; charset=utf-8' -H 'token:${token}' -i https://linlan.net/emis_portal/api/audit/info/save.do --data '{
  "data": {
    "creatorId": "19",
    "createTime": "2025-10-11 22:39:19",
    "lastTime": "2025-10-11 22:39:19",
    "id": "19",
    "foreignId": "19",
    "requestId": "19",
    "sessionId": "19",
    "eventType": "LOGIN_PORTAL",
    "eventParam": "93p62k",
    "eventContent": "1zb0wu",
    "eventResult": "6ou9ew",
    "eventTime": "2025-10-11 22:39:19",
    "eventStatus": "SUCCESS",
    "actorId": "19",
    "actorType": "y2wyqb",
    "actorAuthType": "gs1nm6",
    "target": [
      {
        "id": "19",
        "name": "lorine.rohan",
        "type": "USER",
        "typeName": "lorine.rohan"
      }
    ],
    "userAgent": {
      "deviceType": "h3maap",
      "platform": "b3zq5t",
      "platformVersion": "0.61",
      "browser": "83ckwd",
      "browserType": "u7h1uz",
      "browserMajorVersion": "0.61"
    },
    "geoLocation": {
      "ip": "207.171.28.69",
      "continentCode": "59282",
      "continentName": "lorine.rohan",
      "countryCode": "59282",
      "countryName": "lorine.rohan",
      "provinceCode": "59282",
      "provinceName": "lorine.rohan",
      "cityCode": "59282",
      "cityName": "lorine.rohan",
      "point": {
        "lat": 35.34,
        "lon": 56.67
      },
      "provider": {
        "code": "59282",
        "name": "lorine.rohan",
        "downloadUrl": "www.vida-hills.name",
        "sha256Url": "www.vida-hills.name"
      }
    },
    "delFlag": 4,
    "description": "tor04o",
    "spare1": "eu3mxf",
    "spare2": "7j2bd5"
  }
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
success|boolean|成功标记:正常为true,错误为false|-
code|string|返回状态码:正常状态为0|-
msg|string|返回消息|-
data|object|返回数据对象|-
requestId|string|请求ID|-
timestamp|string|返回时间戳字符串|-

**Response-example:**
```
{
  "success": true,
  "code": "0",
  "msg": "正常",
  "data": "e8caji",
  "requestId": "19",
  "timestamp": "2025-10-11 22:39:08"
}
```

### AuditInfo Operation. 修改行为审计数据操作逻辑，根据操作类型，执行修改操作.
**URL:** https://linlan.net/emis_portal/api/audit/info/update.do

**Type:** POST

**Author:** Linlan
CreateTime 2025-09-06 15:55:04

**Content-Type:** application/json; charset=utf-8

**Description:** AuditInfo Operation. 修改行为审计数据操作逻辑，根据操作类型，执行修改操作.

**Request-headers:**

Header | Type|Description|Required|Since
---|---|---|---|----
token|string|令牌|true|-


**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
data|object|请求数据对象，放入RequestBody内|false|-
└─creatorId|string|创建人id|false|-
└─createTime|string|创建时间|false|-
└─lastTime|string|最后修改时间|false|-
└─id|string|主键ID|false|-
└─foreignId|string|外键ID|false|-
└─requestId|string|请求ID|false|-
└─sessionId|string|会话ID|false|-
└─eventType|enum|事件类型<br/>LOGIN_PORTAL -(PortalEventType.LOGIN_PORTAL)<br/>LOGIN_CONSOLE -(AuthenticationEventType.LOGIN_CONSOLE)<br/>LOGOUT_PORTAL -(PortalEventType.LOGOUT_PORTAL)<br/>LOGOUT_CONSOLE -(AuthenticationEventType.LOGOUT_CONSOLE)<br/>CREATE_ORG -(AccountEventType.CREATE_ORG)<br/>UPDATE_ORG -(AccountEventType.UPDATE_ORG)<br/>DELETE_ORG -(AccountEventType.DELETE_ORGANIZATION)<br/>MOVE_ORGANIZATION -(AccountEventType.MOVE_ORGANIZATION)<br/>CREATE_ORGANIZATION_MEMBER -(AccountEventType.CREATE_ORGANIZATION_MEMBER)<br/>CREATE_USER -(AccountEventType.CREATE_USER)<br/>UPDATE_USER -(AccountEventType.UPDATE_USER)<br/>DELETE_USER -(AccountEventType.DELETE_USER)<br/>ENABLE_USER -(AccountEventType.ENABLE_USER)<br/>DISABLE_USER -(AccountEventType.DISABLE_USER)<br/>USER_RESIGN -(AccountEventType.USER_RESIGN)<br/>MODIFY_USER_PHONE -(AccountEventType.MODIFY_USER_PHONE)<br/>MODIFY_USER_PASSWORD -(AccountEventType.MODIFY_USER_PASSWORD)<br/>UNLOCK_USER -(AccountEventType.UNLOCK_USER)<br/>MODIFY_ACCOUNT_INFO_PORTAL -(PortalEventType.MODIFY_ACCOUNT_INFO)<br/>MODIFY_USER_PASSWORD_PORTAL -(PortalEventType.MODIFY_USER_PASSWORD)<br/>MODIFY_USER_PHONE_PORTAL -(PortalEventType.MODIFY_USER_PHONE)<br/>MODIFY_USER_EMAIL_PORTAL -(PortalEventType.MODIFY_USER_EMAIL)<br/>BIND_MFA -(PortalEventType.BIND_MFA)<br/>UNBIND_MFA -(PortalEventType.UNBIND_MFA)<br/>BIND_IDP_USER -(PortalEventType.BIND_IDP_USER)<br/>UNBIND_IDP_USER -(PortalEventType.UNBIND_IDP_USER)<br/>PREPARE_MODIFY_PASSWORD -(PortalEventType.PREPARE_MODIFY_PASSWORD)<br/>PREPARE_MODIFY_MOBILE -(PortalEventType.PREPARE_MODIFY_MOBILE)<br/>PREPARE_MODIFY_EMAIL -(PortalEventType.PREPARE_MODIFY_EMAIL)<br/>PREPARE_BIND_MFA -(PortalEventType.PREPARE_BIND_MFA)<br/>CREATE_USER_GROUP -(AccountEventType.CREATE_USER_GROUP)<br/>UPDATE_USER_GROUP -(AccountEventType.UPDATE_USER_GROUP)<br/>DELETE_USER_GROUP -(AccountEventType.DELETE_USER_GROUP)<br/>ADD_USER_GROUP_MEMBER -(AccountEventType.ADD_USER_GROUP_MEMBER)<br/>REMOVE_USER_GROUP_MEMBER -(AccountEventType.REMOVE_USER_GROUP_MEMBER)<br/>CREATE_USER_DYNAMIC_GROUP -(AccountEventType.CREATE_USER_DYNAMIC_GROUP)<br/>UPDATE_USER_DYNAMIC_GROUP -(AccountEventType.UPDATE_USER_DYNAMIC_GROUP)<br/>DELETE_USER_DYNAMIC_GROUP -(AccountEventType.DELETE_USER_DYNAMIC_GROUP)<br/>UPDATE_USER_DYNAMIC_GROUP_FILTER_RULE -(AccountEventType.UPDATE_USER_DYNAMIC_GROUP_FILTER_RULE)<br/>CREATE_IDENTITY_RESOURCE -(AccountEventType.CREATE_IDENTITY_RESOURCE)<br/>UPDATE_IDENTITY_RESOURCE -(AccountEventType.UPDATE_IDENTITY_RESOURCE)<br/>DELETE_IDENTITY_RESOURCE -(AccountEventType.DELETE_IDENTITY_RESOURCE)<br/>ENABLE_IDENTITY_RESOURCE -(AccountEventType.ENABLE_IDENTITY_RESOURCE)<br/>DISABLE_IDENTITY_RESOURCE -(AccountEventType.DISABLE_IDENTITY_RESOURCE)<br/>IDENTITY_RESOURCE_SYNC -(AccountEventType.IDENTITY_RESOURCE_SYNC)<br/>ADD_IDP -(AuthenticationEventType.ADD_IDP)<br/>UPDATE_IDP -(AuthenticationEventType.UPDATE_IDP)<br/>DELETE_IDP -(AuthenticationEventType.DELETE_IDP)<br/>ENABLE_IDP -(AuthenticationEventType.ENABLE_IDP)<br/>DISABLE_IDP -(AuthenticationEventType.DISABLE_IDP)<br/>APP_SSO -(PortalEventType.APP_SSO)<br/>APP_SLO -(PortalEventType.APP_SLO)<br/>ADD_APP -(AppEventType.ADD_APP)<br/>ENABLE_APP -(AppEventType.ENABLE_APP)<br/>DISABLE_APP -(AppEventType.DISABLE_APP)<br/>UPDATE_APP -(AppEventType.UPDATE_APP)<br/>SAVE_APP_CONFIG -(AppEventType.SAVE_APP_CONFIG)<br/>DELETE_APP -(AppEventType.DELETE_APP)<br/>APP_AUTHORIZATION -(AppEventType.APP_ACCESS_POLICY)<br/>ENABLE_APP_ACCESS_POLICY -(AppEventType.ENABLE_APP_ACCESS_POLICY)<br/>DISABLE_APP_ACCESS_POLICY -(AppEventType.DISABLE_APP_ACCESS_POLICY)<br/>APP_CANCEL_ACCESS_POLICY -(AppEventType.APP_CANCEL_ACCESS_POLICY)<br/>ADD_APP_ACCOUNT -(AppEventType.ADD_APP_ACCOUNT)<br/>UPDATE_DEFAULT_APP_ACCOUNT -(AppEventType.UPDATE_DEFAULT_APP_ACCOUNT)<br/>DELETE_APP_ACCOUNT -(AppEventType.DELETE_APP_ACCOUNT)<br/>SAVE_SSO_CONFIG -(AppEventType.SAVE_SSO_CONFIG)<br/>SAVE_APP_PERMISSION_RESOURCE -(AppEventType.SAVE_APP_PERMISSION_RESOURCE)<br/>UPDATE_APP_PERMISSION_RESOURCE -(AppEventType.UPDATE_APP_PERMISSION_RESOURCE)<br/>DELETE_APP_PERMISSION_RESOURCE -(AppEventType.DELETE_APP_PERMISSION_RESOURCE)<br/>ENABLE_APP_PERMISSION_RESOURCE -(AppEventType.ENABLE_APP_PERMISSION_RESOURCE)<br/>DISABLE_APP_PERMISSION_RESOURCE -(AppEventType.DISABLE_APP_PERMISSION_RESOURCE)<br/>DELETE_USER_IDP_BIND -(AppEventType.DELETE_USER_IDP_BIND)<br/>SAVE_APP_PERMISSION_ROLE -(AppEventType.SAVE_APP_PERMISSION_ROLE)<br/>UPDATE_APP_PERMISSION_ROLE -(AppEventType.UPDATE_APP_PERMISSION_ROLE)<br/>DELETE_APP_PERMISSION_ROLE -(AppEventType.DELETE_APP_PERMISSION_ROLE)<br/>SAVE_LOGIN_SECURITY_BASIC_SETTINGS -(SettingEventType.SAVE_LOGIN_SECURITY_BASIC_SETTINGS)<br/>SAVE_SECURITY_POLICY_SETTINGS -(SettingEventType.SAVE_SECURITY_POLICY_SETTINGS)<br/>SAVE_PASSWORD_POLICY -(SettingEventType.SAVE_PASSWORD_POLICY)<br/>UPDATE_PASSWORD_POLICY -(SettingEventType.UPDATE_PASSWORD_POLICY)<br/>DELETE_PASSWORD_POLICY -(SettingEventType.DELETE_PASSWORD_POLICY)<br/>ENABLE_PASSWORD_POLICY -(SettingEventType.ENABLE_PASSWORD_POLICY)<br/>DISABLE_PASSWORD_POLICY -(SettingEventType.DISABLE_PASSWORD_POLICY)<br/>SORT_PASSWORD_POLICY -(SettingEventType.SORT_PASSWORD_POLICY)<br/>ADD_ADMINISTRATOR -(SettingEventType.ADD_ADMINISTRATOR)<br/>UPDATE_ADMINISTRATOR -(SettingEventType.UPDATE_ADMINISTRATOR)<br/>DELETE_ADMINISTRATOR -(SettingEventType.DELETE_ADMINISTRATOR)<br/>ENABLE_ADMINISTRATOR -(SettingEventType.ENABLE_ADMINISTRATOR)<br/>DISABLE_ADMINISTRATOR -(SettingEventType.DISABLE_ADMINISTRATOR)<br/>RESET_ADMINISTRATOR_PASSWORD -(SettingEventType.RESET_ADMINISTRATOR_PASSWORD)<br/>ADD_ADMINISTRATOR_ROLE -(SettingEventType.ADD_ADMINISTRATOR_ROLE)<br/>UPDATE_ADMINISTRATOR_ROLE -(SettingEventType.UPDATE_ADMINISTRATOR_ROLE)<br/>DELETE_ADMINISTRATOR_ROLE -(SettingEventType.DELETE_ADMINISTRATOR_ROLE)<br/>ENABLE_ADMINISTRATOR_ROLE -(SettingEventType.ENABLE_ADMINISTRATOR_ROLE)<br/>DISABLE_ADMINISTRATOR_ROLE -(SettingEventType.DISABLE_ADMINISTRATOR_ROLE)<br/>SAVE_MAIL_TEMPLATE -(SettingEventType.SAVE_MAIL_TEMPLATE)<br/>SAVE_MAIL_SERVICE -(SettingEventType.SAVE_MAIL_SERVICE)<br/>OFF_MAIL_SERVICE -(SettingEventType.OFF_MAIL_SERVICE)<br/>SAVE_SMS_SERVICE -(SettingEventType.SAVE_SMS_SERVICE)<br/>OFF_SMS_SERVICE -(SettingEventType.OFF_SMS_SERVICE)<br/>SAVE_GEO_LOCATION_SERVICE -(SettingEventType.SAVE_GEO_LOCATION_SERVICE)<br/>OFF_GEO_LOCATION_SERVICE -(SettingEventType.OFF_GEO_LOCATION_SERVICE)<br/>SAVE_STORAGE_SERVICE -(SettingEventType.SAVE_STORAGE_SERVICE)<br/>OFF_STORAGE_SERVICE -(SettingEventType.OFF_STORAGE_SERVICE)<br/>DOWN_LINE_SESSION -(MonitorEventType.DOWN_LINE_SESSION)<br/>ADD_APP_GROUP -(AppEventType.ADD_APP_GROUP)<br/>UPDATE_APP_GROUP -(AppEventType.UPDATE_APP_GROUP)<br/>DELETE_APP_GROUP -(AppEventType.DELETE_APP_GROUP)<br/>REMOVE_APP_GROUP_ASSOCIATION -(AppEventType.REMOVE_APP_GROUP_ASSOCIATION)<br/>SIGN_OUT_APP -(AppEventType.APP_SSO_LOGOUT)<br/>SAVE_PASSWORD_POLICY_SETTINGS -(SettingEventType.SAVE_PASSWORD_POLICY)<br/>SAVE_MFA_SETTINGS -(SettingEventType.SAVE_MFA_SETTINGS)<br/>SAVE_CAPTCHA_PROVIDER -(SettingEventType.SAVE_CAPTCHA_PROVIDER)<br/>|false|-
└─eventParam|string|事件参数|false|-
└─eventContent|string|事件内容|false|-
└─eventResult|string|事件结果|false|-
└─eventTime|string|事件时间|false|-
└─eventStatus|enum|事件状态<br/>SUCCESS -("success","成功")<br/>FAIL -("fail","失败")<br/>|false|-
└─actorId|string|操作者ID|false|-
└─actorType|string|操作者类型|false|-
└─actorAuthType|string|操作者认证类型|false|-
└─target|array|操作目标|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─id|string|目标 ID|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─name|string|目标名称|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─type|enum|目标类型<br/>USER -("user","用户")<br/>USER_DETAIL -("user_detail","用户详情")<br/>USER_GROUP -("user_group","用户组")<br/>USER_GROUP_MEMBER -("user_group_member","用户组成员")<br/>IDENTITY_SOURCE -("identity_source","身份源")<br/>ORGANIZATION -("organization","组织机构")<br/>APPLICATION -("application","应用")<br/>APPLICATION_ACCOUNT -("application_account","应用账户")<br/>SESSION -("session","会话管理")<br/>APP_PERMISSION_RESOURCE -("app_permission_resource","应用权限")<br/>APP_PERMISSION_POLICY -("app_permission_policy","应用权限策略")<br/>APP_PERMISSION_ROLE -("app_permission_role","应用权限角色")<br/>APP_GROUP -("app_group","应用分组")<br/>ROLE -("role","系统角色")<br/>ADMINISTRATOR -("administrator","管理员")<br/>PASSWORD_POLICY -("password_policy","密码策略")<br/>MAIL_TEMPLATE -("mail_template","邮件模版")<br/>IDENTITY_PROVIDER -("identity_provider","身份认证提供商")<br/>CONSOLE -("console","控制台")<br/>PORTAL -("portal","门户端")<br/>USER_IDP -("user_idp","用户绑定三方用户")<br/>|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─typeName|string|目标类型名称|false|-
└─userAgent|object|用户代理|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─deviceType|string|设备类型|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─platform|string|系统平台|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─platformVersion|string|平台版本|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─browser|string|浏览器|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─browserType|string|浏览器类型|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─browserMajorVersion|string|浏览器主版本|false|-
└─geoLocation|object|地理位置|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─ip|string|IP|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─continentCode|string|continent code|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─continentName|string|continent Name|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─countryCode|string|国家code|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─countryName|string|国家名称|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─provinceCode|string|省份code|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─provinceName|string|省份|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─cityCode|string|城市code|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─cityName|string|城市名称|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─point|object|地理坐标|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─lat|double|经度|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─lon|double|维度|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─provider|object|提供商|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─code|string|code|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─name|string|名称|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─downloadUrl|string|库文件下载地址|false|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─sha256Url|string|sha256校验文件下载地址|false|-
└─delFlag|int32|删除标记，0未删除1已删除|false|-
└─description|string|备注|false|-
└─spare1|string|备用1|false|-
└─spare2|string|备用2|false|-

**Request-example:**
```
curl -X POST -k -H 'Content-Type: application/json; charset=utf-8' -H 'token:${token}' -i https://linlan.net/emis_portal/api/audit/info/update.do --data '{
  "data": {
    "creatorId": "19",
    "createTime": "2025-10-11 22:39:19",
    "lastTime": "2025-10-11 22:39:19",
    "id": "19",
    "foreignId": "19",
    "requestId": "19",
    "sessionId": "19",
    "eventType": "LOGIN_PORTAL",
    "eventParam": "4bkybb",
    "eventContent": "31s8yv",
    "eventResult": "x24q2h",
    "eventTime": "2025-10-11 22:39:19",
    "eventStatus": "SUCCESS",
    "actorId": "19",
    "actorType": "jos6td",
    "actorAuthType": "cdjlzf",
    "target": [
      {
        "id": "19",
        "name": "lorine.rohan",
        "type": "USER",
        "typeName": "lorine.rohan"
      }
    ],
    "userAgent": {
      "deviceType": "hv33jl",
      "platform": "ggxboa",
      "platformVersion": "0.61",
      "browser": "5osiqh",
      "browserType": "plmdci",
      "browserMajorVersion": "0.61"
    },
    "geoLocation": {
      "ip": "207.171.28.69",
      "continentCode": "59282",
      "continentName": "lorine.rohan",
      "countryCode": "59282",
      "countryName": "lorine.rohan",
      "provinceCode": "59282",
      "provinceName": "lorine.rohan",
      "cityCode": "59282",
      "cityName": "lorine.rohan",
      "point": {
        "lat": 49.91,
        "lon": 84.36
      },
      "provider": {
        "code": "59282",
        "name": "lorine.rohan",
        "downloadUrl": "www.vida-hills.name",
        "sha256Url": "www.vida-hills.name"
      }
    },
    "delFlag": 4,
    "description": "69w1ek",
    "spare1": "vcg27m",
    "spare2": "0oamlo"
  }
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
success|boolean|成功标记:正常为true,错误为false|-
code|string|返回状态码:正常状态为0|-
msg|string|返回消息|-
data|object|返回数据对象|-
requestId|string|请求ID|-
timestamp|string|返回时间戳字符串|-

**Response-example:**
```
{
  "success": true,
  "code": "0",
  "msg": "正常",
  "data": "ba04on",
  "requestId": "19",
  "timestamp": "2025-10-11 22:39:08"
}
```

### AuditInfo Operation. 删除行为审计数据操作逻辑，根据操作类型，执行删除操作.
**URL:** https://linlan.net/emis_portal/api/audit/info/delete/{ids}.do

**Type:** POST

**Author:** Linlan
CreateTime 2025-09-06 15:55:04

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** AuditInfo Operation. 删除行为审计数据操作逻辑，根据操作类型，执行删除操作.

**Request-headers:**

Header | Type|Description|Required|Since
---|---|---|---|----
token|string|令牌|true|-


**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
ids|array|主键ID:null;1;1,2,3,[array of string]|true|

**Request-example:**
```
curl -X POST -k -H 'token:${token}' -i https://linlan.net/emis_portal/api/audit/info/delete/.do
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
success|boolean|成功标记:正常为true,错误为false|-
code|string|返回状态码:正常状态为0|-
msg|string|返回消息|-
data|object|返回数据对象|-
requestId|string|请求ID|-
timestamp|string|返回时间戳字符串|-

**Response-example:**
```
{
  "success": true,
  "code": "0",
  "msg": "正常",
  "data": "yxgbp6",
  "requestId": "19",
  "timestamp": "2025-10-11 22:39:08"
}
```

### 获取审计字典类型
**URL:** https://linlan.net/emis_portal/api/audit/info/types/{user_type}.do

**Type:** GET

**Author:** Linlan
CreateTime 2025-09-06 15:55:04

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 获取审计字典类型

**Request-headers:**

Header | Type|Description|Required|Since
---|---|---|---|----
token|string|令牌|true|-


**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
user_type|string|No comments found.|true|-

**Request-example:**
```
curl -X GET -k -H 'token:${token}' -i https://linlan.net/emis_portal/api/audit/info/types/y9cq6k.do
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
success|boolean|成功标记:正常为true,错误为false|-
code|string|返回状态码:正常状态为0|-
msg|string|返回消息|-
data|array|返回数据对象|-
└─name|string|No comments found.|-
└─code|string|No comments found.|-
└─types|array|No comments found.|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─name|string|名称|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─code|string|代码|-
requestId|string|请求ID|-
timestamp|string|返回时间戳字符串|-

**Response-example:**
```
{
  "success": true,
  "code": "0",
  "msg": "正常",
  "data": [
    {
      "name": "lorine.rohan",
      "code": "59282",
      "types": [
        {
          "name": "lorine.rohan",
          "code": "59282"
        }
      ]
    }
  ],
  "requestId": "19",
  "timestamp": "2025-10-11 22:39:08"
}
```

## 存储配置
### 上传文件
**URL:** https://linlan.net/emis_portal/STORAGE_PATH/upload.do

**Type:** POST

**Author:** Linlan

**Content-Type:** multipart/form-data

**Description:** 上传文件

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
file|file| 上传文件|true|-

**Request-example:**
```
curl -X POST -k -H 'Content-Type: multipart/form-data' -i https://linlan.net/emis_portal/STORAGE_PATH/upload.do
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
success|boolean|成功标记:正常为true,错误为false|-
code|string|返回状态码:正常状态为0|-
msg|string|返回消息|-
data|object|返回数据对象|-
└─any object|object|any object.|-
requestId|string|请求ID|-
timestamp|string|返回时间戳字符串|-

**Response-example:**
```
{
  "success": true,
  "code": "0",
  "msg": "正常",
  "data": {},
  "requestId": "19",
  "timestamp": "2025-10-11 22:39:08"
}
```

### 获取存储文件路径
**URL:** https://linlan.net/emis_portal/STORAGE_PATH/get.do

**Type:** GET

**Author:** Linlan

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 获取存储文件路径

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
path|string| 存储文件路径|true|-

**Request-example:**
```
curl -X GET -k -i https://linlan.net/emis_portal/STORAGE_PATH/get.do?path=4g584a
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
success|boolean|成功标记:正常为true,错误为false|-
code|string|返回状态码:正常状态为0|-
msg|string|返回消息|-
data|object|返回数据对象|-
└─any object|object|any object.|-
requestId|string|请求ID|-
timestamp|string|返回时间戳字符串|-

**Response-example:**
```
{
  "success": true,
  "code": "0",
  "msg": "正常",
  "data": {},
  "requestId": "19",
  "timestamp": "2025-10-11 22:39:08"
}
```

## 获取加密key
### 获取加密key
**URL:** https://linlan.net/emis_portal/api/V1/public/secret.do

**Type:** GET

**Author:** Linlan

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 获取加密key

**Request-headers:**

Header | Type|Description|Required|Since
---|---|---|---|----
token|string|令牌|true|-


**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
type|enum|LOGIN -(login,EmisConstants.LOGIN_SECRET,登录)<br/>ENCRYPT -(encrypt,EmisConstants.ENCRYPT_SECRET,加密)<br/>|true|-

**Request-example:**
```
curl -X GET -k -H 'token:${token}' -i https://linlan.net/emis_portal/api/V1/public/secret.do?type=LOGIN
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
success|boolean|成功标记:正常为true,错误为false|-
code|string|返回状态码:正常状态为0|-
msg|string|返回消息|-
data|object|返回数据对象|-
└─secret|string|秘钥|-
requestId|string|请求ID|-
timestamp|string|返回时间戳字符串|-

**Response-example:**
```
{
  "success": true,
  "code": "0",
  "msg": "正常",
  "data": {
    "secret": "ff7hyv"
  },
  "requestId": "19",
  "timestamp": "2025-10-11 22:39:08"
}
```

## 跳转
### jump
**URL:** https://linlan.net/emis_portal/EmisConstants.V1_API_PATH  /jump/.do

**Type:** GET

**Author:** Linlan

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** jump

**Request-example:**
```
curl -X GET -k -i https://linlan.net/emis_portal/EmisConstants.V1_API_PATH  /jump/.do
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
success|boolean|成功标记:正常为true,错误为false|-
code|string|返回状态码:正常状态为0|-
msg|string|返回消息|-
data|object|返回数据对象|-
└─view|object|No comments found.|-
└─model|object|No comments found.|-
└─status|enum|null<br/>CONTINUE<br/>SWITCHING_PROTOCOLS<br/>PROCESSING<br/>CHECKPOINT<br/>OK<br/>CREATED<br/>ACCEPTED<br/>NON_AUTHORITATIVE_INFORMATION<br/>NO_CONTENT<br/>RESET_CONTENT<br/>PARTIAL_CONTENT<br/>MULTI_STATUS<br/>ALREADY_REPORTED<br/>IM_USED<br/>MULTIPLE_CHOICES<br/>MOVED_PERMANENTLY<br/>FOUND<br/>MOVED_TEMPORARILY<br/>SEE_OTHER<br/>NOT_MODIFIED<br/>USE_PROXY<br/>TEMPORARY_REDIRECT<br/>PERMANENT_REDIRECT<br/>BAD_REQUEST<br/>UNAUTHORIZED<br/>PAYMENT_REQUIRED<br/>FORBIDDEN<br/>NOT_FOUND<br/>METHOD_NOT_ALLOWED<br/>NOT_ACCEPTABLE<br/>PROXY_AUTHENTICATION_REQUIRED<br/>REQUEST_TIMEOUT<br/>CONFLICT<br/>GONE<br/>LENGTH_REQUIRED<br/>PRECONDITION_FAILED<br/>PAYLOAD_TOO_LARGE<br/>REQUEST_ENTITY_TOO_LARGE<br/>URI_TOO_LONG<br/>REQUEST_URI_TOO_LONG<br/>UNSUPPORTED_MEDIA_TYPE<br/>REQUESTED_RANGE_NOT_SATISFIABLE<br/>EXPECTATION_FAILED<br/>I_AM_A_TEAPOT<br/>INSUFFICIENT_SPACE_ON_RESOURCE<br/>METHOD_FAILURE<br/>DESTINATION_LOCKED<br/>UNPROCESSABLE_ENTITY<br/>LOCKED<br/>FAILED_DEPENDENCY<br/>TOO_EARLY<br/>UPGRADE_REQUIRED<br/>PRECONDITION_REQUIRED<br/>TOO_MANY_REQUESTS<br/>REQUEST_HEADER_FIELDS_TOO_LARGE<br/>UNAVAILABLE_FOR_LEGAL_REASONS<br/>INTERNAL_SERVER_ERROR<br/>NOT_IMPLEMENTED<br/>BAD_GATEWAY<br/>SERVICE_UNAVAILABLE<br/>GATEWAY_TIMEOUT<br/>HTTP_VERSION_NOT_SUPPORTED<br/>VARIANT_ALSO_NEGOTIATES<br/>INSUFFICIENT_STORAGE<br/>LOOP_DETECTED<br/>BANDWIDTH_LIMIT_EXCEEDED<br/>NOT_EXTENDED<br/>NETWORK_AUTHENTICATION_REQUIRED<br/>|-
└─cleared|boolean|No comments found.|-
requestId|string|请求ID|-
timestamp|string|返回时间戳字符串|-

**Response-example:**
```
{"success":true,"code":"0","msg":"正常","data":Forward or redirect to a page view.,"requestId":"19","timestamp":"2025-10-11 22:39:08"}
```

## 错误码列表
Error code |Description
---|---
200|Success[正常]
201|None[未查询到数据]
400|Bad Request[请求错误]
401|Unauthorized[TOKEN失效，请重新登录]
403|Forbidden[页面禁止访问]
404|Not Found[页面未找到]
415|Unsupported Media Type[数据类型不支持]
500|Internal Server Error[未知错误]
501|Not Login[用户未登录]
502|Bad Gateway[网关错误]
503|Service Unavailable[服务不可用]
700|Token Error[TOKEN信息错误]
701|Input Error Code[信息传入有误(code)]
702|Input Error Source[信息传入有误(source)]
703|Account Not Exist[对接平台账户不存在]
704|Channel Not Exist[该栏目版面不存在]
711|Parameter Error[传递参数信息错误]
712|Network Error[网络异常，请联系系统管理员]
721|Third Info Error[第三方数据信息验证失败]
731|IP Whitelist Error[白名单校验失败]
741|Info Send Fail[消息推送失败]
1001|Error User Info[用户数据信息验证失败]
1002|Error User SSO[SSO用户数据信息验证失败]
1003|Error User APP[APP用户数据信息验证失败]
1004|Error User APP Step1[APP用户在调取第三方接口时失败]
1005|Error User APP Step2[APP用户数据调取第三方接口成功但是返回的数据不正确]
1006|Error User Out[外部用户数据信息验证失败]
1011|Error User Key[用户关键信息错误]
1012|Error User Password[用户密码信息错误]
1013|Error User Real Name[需要实名认证绑定]
1014|Error User Need Login[请用户重新登录]
1015|Error User Need Face[需要人脸识别二次认证]
1016|Error User Identity[认证失败，请重新认证]
1017|Error User Face[人脸识别信息与系统不一致]
1018|Error User Need Reg[用户信息未注册，请注册]
1019|Error Params Mate[传参不匹配]
1020|Error Return Many[查询出多条数据]
1021|Error Number Format[数字格式转换异常]
1022|Error Data Duplicate[数据重复]
1023|Error Data Format[数据格式错误]
1024|Error Data[数据错误]
1101|Error Address Invalid[无效访问地址]
1102|Error With Upload[上传出现错误，请联系管理员]
1103|Error With Input填写信息错误，请检查]
1104|Error With Password[密码必须为大小写字母,数字,特殊字符中的至少3种且8位-20位之内]
1201|Error With Enterprise Info[企业关键信息错误]
1202|Error With Not Initial[关键信息未初始化]
1301|Error Template Info[模板信息错误]

## 数据字典
### 目录树结构字典

Code |Type|Description
---|---|---
ROLE|string|角色树
MENU|string|菜单树
ALL_POSITION|string|全部岗位树
POSITION|string|岗位树
SINGLE_POSITION|string|单表岗位树
ALL_MENU|string|全部菜单树
ALL_DEPT|string|全部部门树
BASE_XZQH|string|行政区划树
COM_DIC_ECOINDUSTRY|string|所属行业树
CORE_DEPTTYPE|string|条线部门树
CORE_ORGTYPE|string|机构分类树
