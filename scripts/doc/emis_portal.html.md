# emis_portal
Version |  Update Time  | Status | Author |  Description
---|---|---|---|---
1.0|2025-08-24 10:30|update|Linlan|desc



# default
# 平台通用接口API
# EMIS平台接口API
## EmisIdentityProvider控制域:身份提供商读写控制类
### Get EmisIdentityProvider list. 获取身份提供商列表或分页, 返回EmisIdentityProvider对象.
**URL:** https://linlan.net/emis_portal/api/provider/identity/list.do

**Type:** GET

**Author:** Linlan
CreateTime 2025-08-31 23:07:10<br>

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** Get EmisIdentityProvider list. 获取身份提供商列表或分页, 返回EmisIdentityProvider对象.

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
id|string|主键ID|false|-
foreignId|string|外键ID|false|-
name|string|名称|false|-
code|string|编码|false|-
type|string|提供者类型|false|-
config|string|配置JSON|false|-
category|string|认证源分类（社交、企业）|false|-
isEnabled|boolean|是否启用，0否1是|false|-
isDisplayed|boolean|是否显示，0否1是|false|-
creatorId|string|创建人ID|false|-
delFlag|int32|删除标记，0未删除1已删除|false|-
description|string|备注|false|-
spare1|string|备用1|false|-
spare2|string|备用2|false|-

**Request-example:**
```
curl -X GET -k -H 'token:${token}' -i https://linlan.net/emis_portal/api/provider/identity/list.do?orderBy=318&isDisplayed=true&foreignId=33&creatorId=33&endTime=2025-09-05 23:52:20&isEnabled=true&delFlag=8&description=4ntv38&spare2=ope208&name=renee.stehr&id=33&category=9hsryr&code=86144&limit=10&startTime=2025-09-05 23:52:20&q=tp5ybu&page=1&type=ujxt2g&spare1=myn3pv&config=ox4cst
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
└─name|string|名称|-
└─code|string|编码|-
└─type|string|提供者类型|-
└─config|string|配置JSON|-
└─category|string|认证源分类（社交、企业）|-
└─isEnabled|boolean|是否启用，0否1是|-
└─isDisplayed|boolean|是否显示，0否1是|-
└─priority|int32|排序码|-
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
      "creatorId": "33",
      "createTime": "2025-09-05 23:52:28",
      "lastTime": "2025-09-05 23:52:28",
      "id": "33",
      "foreignId": "33",
      "name": "renee.stehr",
      "code": "86144",
      "type": "92i0sy",
      "config": "hm4m4s",
      "category": "mwgmfo",
      "isEnabled": true,
      "isDisplayed": true,
      "priority": 103,
      "delFlag": 8,
      "description": "zmq1qi",
      "spare1": "cdeths",
      "spare2": "el9dfa"
    }
  ],
  "requestId": "33",
  "timestamp": "2025-09-05 23:52:20"
}
```

### Get EmisIdentityProvider by id. 主键获取身份提供商详细信息.
**URL:** https://linlan.net/emis_portal/api/provider/identity/{id}.do

**Type:** GET

**Author:** Linlan
CreateTime 2025-08-31 23:07:10<br>

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** Get EmisIdentityProvider by id. 主键获取身份提供商详细信息.

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
curl -X GET -k -H 'token:${token}' -i https://linlan.net/emis_portal/api/provider/identity/1.do
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
└─name|string|名称|-
└─code|string|编码|-
└─type|string|提供者类型|-
└─config|string|配置JSON|-
└─category|string|认证源分类（社交、企业）|-
└─isEnabled|boolean|是否启用，0否1是|-
└─isDisplayed|boolean|是否显示，0否1是|-
└─priority|int32|排序码|-
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
    "creatorId": "33",
    "createTime": "2025-09-05 23:52:28",
    "lastTime": "2025-09-05 23:52:28",
    "id": "33",
    "foreignId": "33",
    "name": "renee.stehr",
    "code": "86144",
    "type": "9q9f8p",
    "config": "30iyot",
    "category": "hv2zi0",
    "isEnabled": true,
    "isDisplayed": true,
    "priority": 169,
    "delFlag": 8,
    "description": "p1zzh9",
    "spare1": "0sadbq",
    "spare2": "wz756s"
  },
  "requestId": "33",
  "timestamp": "2025-09-05 23:52:20"
}
```

### EmisIdentityProvider Operation. 新增身份提供商数据操作逻辑，根据操作类型，执行新增操作.
**URL:** https://linlan.net/emis_portal/api/provider/identity/save.do

**Type:** POST

**Author:** Linlan
CreateTime 2025-08-31 23:07:10<br>

**Content-Type:** application/json; charset=utf-8

**Description:** EmisIdentityProvider Operation. 新增身份提供商数据操作逻辑，根据操作类型，执行新增操作.

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
└─name|string|名称|false|-
└─code|string|编码|false|-
└─type|string|提供者类型|false|-
└─config|string|配置JSON|false|-
└─category|string|认证源分类（社交、企业）|false|-
└─isEnabled|boolean|是否启用，0否1是|false|-
└─isDisplayed|boolean|是否显示，0否1是|false|-
└─priority|int32|排序码|false|-
└─delFlag|int32|删除标记，0未删除1已删除|false|-
└─description|string|备注|false|-
└─spare1|string|备用1|false|-
└─spare2|string|备用2|false|-

**Request-example:**
```
curl -X POST -k -H 'Content-Type: application/json; charset=utf-8' -H 'token:${token}' -i https://linlan.net/emis_portal/api/provider/identity/save.do --data '{
  "data": {
    "creatorId": "33",
    "createTime": "2025-09-05 23:52:28",
    "lastTime": "2025-09-05 23:52:28",
    "id": "33",
    "foreignId": "33",
    "name": "renee.stehr",
    "code": "86144",
    "type": "pz6r80",
    "config": "pmw27k",
    "category": "tonrat",
    "isEnabled": true,
    "isDisplayed": true,
    "priority": 122,
    "delFlag": 8,
    "description": "rntzzd",
    "spare1": "605mh2",
    "spare2": "j6brnx"
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
  "data": "9xaaoq",
  "requestId": "33",
  "timestamp": "2025-09-05 23:52:20"
}
```

### EmisIdentityProvider Operation. 修改身份提供商数据操作逻辑，根据操作类型，执行修改操作.
**URL:** https://linlan.net/emis_portal/api/provider/identity/update.do

**Type:** POST

**Author:** Linlan
CreateTime 2025-08-31 23:07:10<br>

**Content-Type:** application/json; charset=utf-8

**Description:** EmisIdentityProvider Operation. 修改身份提供商数据操作逻辑，根据操作类型，执行修改操作.

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
└─name|string|名称|false|-
└─code|string|编码|false|-
└─type|string|提供者类型|false|-
└─config|string|配置JSON|false|-
└─category|string|认证源分类（社交、企业）|false|-
└─isEnabled|boolean|是否启用，0否1是|false|-
└─isDisplayed|boolean|是否显示，0否1是|false|-
└─priority|int32|排序码|false|-
└─delFlag|int32|删除标记，0未删除1已删除|false|-
└─description|string|备注|false|-
└─spare1|string|备用1|false|-
└─spare2|string|备用2|false|-

**Request-example:**
```
curl -X POST -k -H 'Content-Type: application/json; charset=utf-8' -H 'token:${token}' -i https://linlan.net/emis_portal/api/provider/identity/update.do --data '{
  "data": {
    "creatorId": "33",
    "createTime": "2025-09-05 23:52:28",
    "lastTime": "2025-09-05 23:52:28",
    "id": "33",
    "foreignId": "33",
    "name": "renee.stehr",
    "code": "86144",
    "type": "utxtgy",
    "config": "plze5f",
    "category": "7uvpjf",
    "isEnabled": true,
    "isDisplayed": true,
    "priority": 173,
    "delFlag": 8,
    "description": "cvo7o5",
    "spare1": "vpf3md",
    "spare2": "i10ox3"
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
  "data": "va11hh",
  "requestId": "33",
  "timestamp": "2025-09-05 23:52:20"
}
```

### EmisIdentityProvider Operation. 删除身份提供商数据操作逻辑，根据操作类型，执行删除操作.
**URL:** https://linlan.net/emis_portal/api/provider/identity/delete/{ids}.do

**Type:** POST

**Author:** Linlan
CreateTime 2025-08-31 23:07:10<br>

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** EmisIdentityProvider Operation. 删除身份提供商数据操作逻辑，根据操作类型，执行删除操作.

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
curl -X POST -k -H 'token:${token}' -i https://linlan.net/emis_portal/api/provider/identity/delete/.do
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
  "data": "s7op5b",
  "requestId": "33",
  "timestamp": "2025-09-05 23:52:20"
}
```

## IdentityProvider控制域:身份提供商读写控制类
### Get IdentityProvider list. 获取身份提供商列表或分页, 返回IdentityProvider对象.
**URL:** https://linlan.net/emis_portal/api/provider/identity/list.do

**Type:** GET

**Author:** Linlan
CreateTime 2025-08-31 23:33:33<br>

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** Get IdentityProvider list. 获取身份提供商列表或分页, 返回IdentityProvider对象.

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
id|string|主键ID|false|-
foreignId|string|外键ID|false|-
name|string|名称|false|-
code|string|编码|false|-
type|string|提供者类型|false|-
config|string|配置JSON|false|-
category|string|认证源分类（社交、企业）|false|-
isEnabled|boolean|是否启用，0否1是|false|-
isDisplayed|boolean|是否显示，0否1是|false|-
creatorId|string|创建人ID|false|-
delFlag|int32|删除标记，0未删除1已删除|false|-
description|string|备注|false|-
spare1|string|备用1|false|-
spare2|string|备用2|false|-

**Request-example:**
```
curl -X GET -k -H 'token:${token}' -i https://linlan.net/emis_portal/api/provider/identity/list.do?q=hnyn5p&startTime=2025-09-05 23:52:20&isDisplayed=true&spare1=mwyiyu&config=r0no6e&isEnabled=true&category=58mt3j&description=2sa6e6&endTime=2025-09-05 23:52:20&code=86144&delFlag=8&creatorId=33&foreignId=33&spare2=t410v3&id=33&page=1&orderBy=319&name=renee.stehr&type=9b41yd&limit=10
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
└─name|string|名称|-
└─code|string|编码|-
└─type|string|提供者类型|-
└─config|string|配置JSON|-
└─category|string|认证源分类（社交、企业）|-
└─isEnabled|boolean|是否启用，0否1是|-
└─isDisplayed|boolean|是否显示，0否1是|-
└─priority|int32|排序码|-
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
      "creatorId": "33",
      "createTime": "2025-09-05 23:52:28",
      "lastTime": "2025-09-05 23:52:28",
      "id": "33",
      "foreignId": "33",
      "name": "renee.stehr",
      "code": "86144",
      "type": "qbycuv",
      "config": "e0s7mx",
      "category": "y0qlkx",
      "isEnabled": true,
      "isDisplayed": true,
      "priority": 381,
      "delFlag": 8,
      "description": "jtigx9",
      "spare1": "tqtmpz",
      "spare2": "86weyl"
    }
  ],
  "requestId": "33",
  "timestamp": "2025-09-05 23:52:20"
}
```

### Get IdentityProvider by id. 主键获取身份提供商详细信息.
**URL:** https://linlan.net/emis_portal/api/provider/identity/{id}.do

**Type:** GET

**Author:** Linlan
CreateTime 2025-08-31 23:33:33<br>

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** Get IdentityProvider by id. 主键获取身份提供商详细信息.

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
curl -X GET -k -H 'token:${token}' -i https://linlan.net/emis_portal/api/provider/identity/1.do
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
└─name|string|名称|-
└─code|string|编码|-
└─type|string|提供者类型|-
└─config|string|配置JSON|-
└─category|string|认证源分类（社交、企业）|-
└─isEnabled|boolean|是否启用，0否1是|-
└─isDisplayed|boolean|是否显示，0否1是|-
└─priority|int32|排序码|-
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
    "creatorId": "33",
    "createTime": "2025-09-05 23:52:28",
    "lastTime": "2025-09-05 23:52:28",
    "id": "33",
    "foreignId": "33",
    "name": "renee.stehr",
    "code": "86144",
    "type": "id5fde",
    "config": "gjbmgn",
    "category": "2tv5nr",
    "isEnabled": true,
    "isDisplayed": true,
    "priority": 345,
    "delFlag": 8,
    "description": "722aiu",
    "spare1": "ihbj05",
    "spare2": "ly6wio"
  },
  "requestId": "33",
  "timestamp": "2025-09-05 23:52:20"
}
```

### IdentityProvider Operation. 新增身份提供商数据操作逻辑，根据操作类型，执行新增操作.
**URL:** https://linlan.net/emis_portal/api/provider/identity/save.do

**Type:** POST

**Author:** Linlan
CreateTime 2025-08-31 23:33:33<br>

**Content-Type:** application/json; charset=utf-8

**Description:** IdentityProvider Operation. 新增身份提供商数据操作逻辑，根据操作类型，执行新增操作.

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
└─name|string|名称|false|-
└─code|string|编码|false|-
└─type|string|提供者类型|false|-
└─config|string|配置JSON|false|-
└─category|string|认证源分类（社交、企业）|false|-
└─isEnabled|boolean|是否启用，0否1是|false|-
└─isDisplayed|boolean|是否显示，0否1是|false|-
└─priority|int32|排序码|false|-
└─delFlag|int32|删除标记，0未删除1已删除|false|-
└─description|string|备注|false|-
└─spare1|string|备用1|false|-
└─spare2|string|备用2|false|-

**Request-example:**
```
curl -X POST -k -H 'Content-Type: application/json; charset=utf-8' -H 'token:${token}' -i https://linlan.net/emis_portal/api/provider/identity/save.do --data '{
  "data": {
    "creatorId": "33",
    "createTime": "2025-09-05 23:52:28",
    "lastTime": "2025-09-05 23:52:28",
    "id": "33",
    "foreignId": "33",
    "name": "renee.stehr",
    "code": "86144",
    "type": "qcqj3d",
    "config": "0krpki",
    "category": "30938y",
    "isEnabled": true,
    "isDisplayed": true,
    "priority": 10,
    "delFlag": 8,
    "description": "q1khlx",
    "spare1": "948h8v",
    "spare2": "6zrdzq"
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
  "data": "34z3y3",
  "requestId": "33",
  "timestamp": "2025-09-05 23:52:20"
}
```

### IdentityProvider Operation. 修改身份提供商数据操作逻辑，根据操作类型，执行修改操作.
**URL:** https://linlan.net/emis_portal/api/provider/identity/update.do

**Type:** POST

**Author:** Linlan
CreateTime 2025-08-31 23:33:33<br>

**Content-Type:** application/json; charset=utf-8

**Description:** IdentityProvider Operation. 修改身份提供商数据操作逻辑，根据操作类型，执行修改操作.

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
└─name|string|名称|false|-
└─code|string|编码|false|-
└─type|string|提供者类型|false|-
└─config|string|配置JSON|false|-
└─category|string|认证源分类（社交、企业）|false|-
└─isEnabled|boolean|是否启用，0否1是|false|-
└─isDisplayed|boolean|是否显示，0否1是|false|-
└─priority|int32|排序码|false|-
└─delFlag|int32|删除标记，0未删除1已删除|false|-
└─description|string|备注|false|-
└─spare1|string|备用1|false|-
└─spare2|string|备用2|false|-

**Request-example:**
```
curl -X POST -k -H 'Content-Type: application/json; charset=utf-8' -H 'token:${token}' -i https://linlan.net/emis_portal/api/provider/identity/update.do --data '{
  "data": {
    "creatorId": "33",
    "createTime": "2025-09-05 23:52:28",
    "lastTime": "2025-09-05 23:52:28",
    "id": "33",
    "foreignId": "33",
    "name": "renee.stehr",
    "code": "86144",
    "type": "6uv047",
    "config": "5icggl",
    "category": "3j5d99",
    "isEnabled": true,
    "isDisplayed": true,
    "priority": 631,
    "delFlag": 8,
    "description": "cdka0d",
    "spare1": "xu0efg",
    "spare2": "3xm5dg"
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
  "data": "53rxu3",
  "requestId": "33",
  "timestamp": "2025-09-05 23:52:20"
}
```

### IdentityProvider Operation. 删除身份提供商数据操作逻辑，根据操作类型，执行删除操作.
**URL:** https://linlan.net/emis_portal/api/provider/identity/delete/{ids}.do

**Type:** POST

**Author:** Linlan
CreateTime 2025-08-31 23:33:33<br>

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** IdentityProvider Operation. 删除身份提供商数据操作逻辑，根据操作类型，执行删除操作.

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
curl -X POST -k -H 'token:${token}' -i https://linlan.net/emis_portal/api/provider/identity/delete/.do
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
  "data": "ys1r4e",
  "requestId": "33",
  "timestamp": "2025-09-05 23:52:20"
}
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
