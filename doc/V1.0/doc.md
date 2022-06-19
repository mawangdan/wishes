# 项目模块划分

- core 核心模块（包括一些必须的配置和工具类）

- task 任务模块

- user 用户模块（包括用户信息和用户权限）
- admin 管理员模块 (还未使用)

- gateway 网关模块

# 数据库

![image-20220411183107138](C:\Users\wl\AppData\Roaming\Typora\typora-user-images\image-20220411183107138.png)



![image-20220411182923871](C:\Users\wl\AppData\Roaming\Typora\typora-user-images\image-20220411182923871.png)

- wishes_task_type  任务类型表
- wishes_task_draft 草稿任务表
- wishes_task 任务表
- wishes_user 用户表
- wishes_chat_record 聊天记录表
- wishes_auth_privilege 权限表
- wishes_auth_role 角色表
- wishes_auth_role_privilege 角色权限关联表
- wishes_auth_user_role 用户角色关联表



# 项目启动说明

## 所需环境

- nacos
- mysql8.0
- redis

## 具体步骤

### nacos设置

先用单机模式启动nacos(cd 到nacos的bin目录下，输入startup.cmd -m standalone)

![image-20220619180048513](D:\IdeaProjects\wishes\doc\V1.0\doc.assets\image-20220619180048513.png)

然后在浏览器中进入nacos管理界面并登录，默认的账号和密码都是`nacos`,

然后在配置管理的配置列表页面中导入配置

![image-20220619180425443](D:\IdeaProjects\wishes\doc\V1.0\doc.assets\image-20220619180425443.png)



### mysql设置

导入sql目录下的wishes-db.sql, wishes-schema.sql两个sql文件

![image-20220619181410174](D:\IdeaProjects\wishes\doc\V1.0\doc.assets\image-20220619181410174.png)



### 启动项目

启动gateway, task, user模块下的Application启动类，然后从gateway网关访问api





# 项目进度

## 已完成

### 用户模块

- 用户注册、登录、信息查询和修改。
- 用户权限设置(目前需自行在数据插入对应数据)

### 任务模块

- 草稿任务的新增、修改
- 任务的查询

## 待完成

### 1. 去除所有@Audit注解，并删除函数上的@LoginUser所对应的函数参数，用户id的获取方式改为使用UserInfoUtil.getUserId()

![image-20220619182935666](D:\IdeaProjects\wishes\doc\V1.0\doc.assets\image-20220619182935666.png)

![image-20220619183041974](D:\IdeaProjects\wishes\doc\V1.0\doc.assets\image-20220619183041974.png)



修改示例：

![image-20220619183210958](D:\IdeaProjects\wishes\doc\V1.0\doc.assets\image-20220619183210958.png)

### 2.接口相关

#### 用户模块

- 用户登出接口`/logout` 未完成
- 聊天功能

#### 任务模块

- 草稿任务发布接口未完成
- 任务下架接口未完成
- 未将任务类型表与任务关联。目前任务类型字段还是写死的Byte类型
- 获取所有任务类型



# 数据库字段说明

## TaskDraft 草稿任务表

```java
private Long taskId;    // 对应正式任务id, 草稿任务未发布时为null

private Long initiatorId; // 发起者id

private Long receiverId;  // 接收者id

private Byte type;        // 任务类型

private String title;     // 任务标题
 
private String description; // 任务描述

private String location;   // 任务地址

private String imageUrl;  // 任务url。多个 url以','分割

private String price;     // 价格

private Byte state;       // 任务状态
```



## AuthPrivilege表(权限表)

```java
private String name;  // 权限名
private String url;   // 权限对应url
private RequestType requestType; // url对应的方法，有“GET", "POST", "PUT", "DELETE"
private Type state;   // 权限状态(目前未使用)
```

## AuthRole表(角色表)

```java
private String name;  // 角色名
private String descr; // 角色描述
private Type state;   // 状态(目前未使用)
```

## AuthRolePrivilege表(角色权限关联表)

```java
String role_id // 角色id
String privilege_id // 权限id
```

## AuthUserRole表(用户角色关联表)
```java
String role_id      // 角色id
String user_id      // 用户id
```


# 已完成的api接口

**从gateway网关访问api**

## User模块

### 用户登录

**请求URL**

/oauth/token `POST` 

**请求参数**

该api为oauth2认证api。请求方式为`post`，以下是该api的请求参数说明

**params中必带的参数**

```java
username: 用户名
password: 密码
scope: all(固定值)
grant_type: password(固定值)
client_id: app(固定值)
client_secret: bkdwln231-23(固定值)
```
![image-20220619175442085](D:\IdeaProjects\wishes\doc\V1.0\doc.assets\image-20220619175442085.png)

**Headers中的必带的参数**

```java
Content-Type: application/json;charset=UTF-8
Authorization: Basic YXBwOmJrZHdsbjIzMS0yMw==
```

![image-20220619175822827](D:\IdeaProjects\wishes\doc\V1.0\doc.assets\image-20220619175822827.png)



**返回结果**

```json
{
    "errno": "int", //错误代码
    "errmsg": "string", //错误信息
    "data":
    {
        "access_token": "string", // 访问用的token
        "refresh_token": "long", // 用于刷新token的token
    }
}
```

### 用户注册

先使用该api获取一个发往用户邮箱的验证码，然后再使用下面的上传注册验证码的api来完成用户的注册。

**请求URL**

/users `POST` 

**请求体**

```json
{
	"userName":"string【必须】",  // 账号
	"password":"string【必须】",  // 密码
    "email":"string【必须】",     //邮箱
	"mobile":"string",
}
```

**返回结果**

```json
{
    "errno": "int", //错误代码
    "errmsg": "string", //错误信息
}
```

### 上传注册验证码

**请求URL**

/users/captcha `POST` 

**请求体**

```json
{
	"captcha":"string【必须】", // 验证码
	"email":"string【必须】"    // 用户邮箱
}
```

**返回结果**

```json
{
    "errno": "int", //错误代码
    "errmsg": "string", //错误信息
}
```



### 用户查询信息

**请求URL**

/self `GET` 

**请求参数**

无

**返回结果**

```json
{
    "errno": "int", //错误代码
    "errmsg": "string", //错误信息
    "data":
    {
        "userName": "string", // 用户名
        "sign": "string", // 用户签名
        "address": "string", // 用户地址
        "mobile": "string", // 用户电话
        "email": "string" // 用户邮箱
    }
}
```

### 修改用户信息

**请求URL**

/self `PUT` 

**请求体**

body

```json
{
    "password": "string", // 密码
    "sign": "string", // 签名
    "address": "string" // 地址
}
```




**返回结果**

```json
{
    "errno": "int", //错误代码
    "errmsg": "string", //错误信息
    "data":
    {
        "userName": "string", // 用户名
        "sign": "string", // 用户签名
        "address": "string", // 用户地址
        "mobile": "string", // 用户电话
        "email": "string" // 用户邮箱
    }
}
```

### 修改用户密码

**请求URL**

/password `PUT` 

**请求体**

body

```json
{
	"oldPassword":"string",
	"newPassword":"string"
}
```

**返回结果**

```json
{
    "errno": "int", //错误代码
    "errmsg": "string", //错误信息
}
```

## Task

### 新增草稿任务

**请求URL**

/taskdraft/add `POST` 

**请求体**

```json
{
	"initiatorId":"long",
	"type":"byte",
	"title":"string【必须】",
	"description":"string",
	"imageUrl":"string",
	"price":"string"
}
```

**返回结果**

```json
{
    "errno": "int", //错误代码
    "errmsg": "string", //错误信息
    "data":
    {
        "id": "long", // 任务id
        "initiatorId": "long", // 发起人id
        "type": "byte", // 任务类型
        "title": "string", // 任务标题
        "description": "string", // 任务描述
        "location": "string", // 任务地址
        "imageUrl": "string", // 任务图片url
        "price": "string", // 任务金额
        "state": "byte" // 任务状态
    }
}
```

### 通过id获取草稿任务

**请求URL**

/taskdraft/{id} `GET` 

**请求参数**

| 参数名 | 类型 |    描述    |
| :----: | :--: | :--------: |
|   id   | long | 草稿任务id |

**返回结果**

```json
{
    "errno": "int", //错误代码
    "errmsg": "string", //错误信息
    "data":
    {
        "id": "long", // 任务id
        "initiatorId": "long", // 发起人id
        "type": "byte", // 任务类型
        "title": "string", // 任务标题
        "description": "string", // 任务描述
        "location": "string", // 任务地址
        "imageUrl": "string", // 任务图片url
        "price": "string", // 任务金额
        "state": "byte" // 任务状态
    }
}
```

### 为草稿任务上传图片

**请求URL**

/taskdraft/{id}/uploadImg `POST` 

**请求参数**

| 参数名 | 类型 |           描述           |
| :----: | :--: | :----------------------: |
|   id   | long | 要上传图片的草稿任务的id |

**返回结果**

```json
{
    "errno": "int", //错误代码
    "errmsg": "string", //错误信息
}
```

### 修改草稿任务

**请求URL**

/taskdraft/{id} `PUT` 

**请求参数**

| 参数名 | 类型 |    描述    |
| :----: | :--: | :--------: |
|   id   | long | 草稿任务id |

**请求体**

body

```json
{
	"initiatorId":"long",
	"type":"byte",
	"title":"string【必须】",
	"description":"string",
	"imageUrl":"string",
	"price":"string"
}
```

**返回结果**

```json
{
    "errno": "int", //错误代码
    "errmsg": "string", //错误信息
    "data":
    {
        "id": "long", // 任务id
        "initiatorId": "long", // 发起人id
        "type": "byte", // 任务类型
        "title": "string", // 任务标题
        "description": "string", // 任务描述
        "location": "string", // 任务地址
        "imageUrl": "string", // 任务图片url
        "price": "string", // 任务金额
        "state": "byte" // 任务状态
    }
}
```



### 通过id获取任务

**请求URL**

/tasks/{id} `GET` 

**请求参数**

| 参数名 | 类型 | 必须 | 描述 |
| -----: | :--: | :--: | :--- |
|     id | long |  否  |      |

**返回结果**

```json
{
    "errno": "int", //错误代码
    "errmsg": "string", //错误信息
    "data":
    {
        "id": "long", // 任务id
        "initiatorId": "long", // 发起人id
        "type": "byte", // 任务类型
        "title": "string", // 任务标题
        "description": "string", // 任务描述
        "location": "string", // 任务地址
        "imageUrl": "string", // 任务图片url
        "price": "string", // 任务金额
        "state": "byte" // 任务状态
    }
}
```

### 通过筛选条件获取分页后的任务

**请求URL**

/tasks `GET` 

**请求参数**

|      参数名 | 类型 | 必须 | 描述     |
| ----------: | :--: | :--: | :------- |
| initiatorId | long |  否  | 发起者id |
|        type | byte |  否  | 类型     |
|        page | int  |  否  | 页号     |
|    pageSize | int  |  否  | 页大小   |

**返回结果**

```json
{
    "errno": "int", //错误代码
    "errmsg": "string", //错误信息
    "data":
    {
        "total": "long", // 任务总数
        "page": "int", // 当前页数
        "pageSize": "int", // 页大小
        "pages": "int", // 总页数
        "list": [
            {
            "id": "long", // 任务id
            "initiatorId": "long", // 发起人id
            "type": "byte", // 任务类型
            "title": "string", // 任务标题
            "description": "string", // 任务描述
            "location": "string", // 任务地址
            "imageUrl": "string", // 任务图片url
            "price": "string", // 任务金额
            "state": "byte" // 任务状态
        	}
        ]
    }
}
```

