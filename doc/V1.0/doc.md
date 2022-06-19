# 项目模块划分

- core 核心模块（包括一些必须的配置和工具类）

- task 任务模块

- user 用户模块（包括用户信息和用户权限）
- admin 管理员模块

- gateway 网关模块

### 进度：任务模块已大部分完成，用户模块已完成，管理员模块未开始，网卡模块已完成

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



# api

## User模块

### 用户登录

*作者: bwly*

**请求URL**

/oauth/token `POST` 


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

**请求URL**

/users `POST` 

**请求体**

```json
{
	"userName":"string【必须】",
	"password":"string【必须】",
	"sign":"string",
	"address":"string",
	"mobile":"string //, message = \"手机号格式错误\")",
	"email":"string //@NotBlank(message = \"邮箱不能为空\")"
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
	"captcha":"string【必须】",
	"email":"string【必须】"
}
```

**返回结果**

```json
{
    "errno": "int", //错误代码
    "errmsg": "string", //错误信息
}
```

### 用户登出

**请求URL**

/logout `POST` 

**请求参数**

| 参数名 | 类型 | 必须 | 描述 |
| -----: | :--: | :--: | :--- |
| userid | long |  否  |      |

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

| 参数名 | 类型 | 必须 | 描述 |
| -----: | :--: | :--: | :--- |
| userId | long |  否  |      |

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

**

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

**

**请求URL**

/taskdraft/{id} `GET` 

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

### 为草稿任务上传图片

**

**请求URL**

/taskdraft/{id}/uploadImg `POST` 

**请求参数**

| 参数名 | 类型 | 必须 | 描述 |
| -----: | :--: | :--: | :--- |
|     id | long |  是  |      |

**返回结果**

```json
{
    "errno": "int", //错误代码
    "errmsg": "string", //错误信息
}
```

### 修改草稿任务

**

**请求URL**

/taskdraft/{id} `PUT` 

**请求参数**

| 参数名 | 类型 | 必须 | 描述 |
| -----: | :--: | :--: | :--- |
|     id | long |  是  |      |

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

### 通过id获取任务

**

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

**

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

