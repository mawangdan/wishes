DROP TABLE IF EXISTS `wishes_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wishes_user` (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                   `user_name` varchar(128) DEFAULT NULL,
                                   `password` varchar(255) DEFAULT NULL,
                                   `credit_point` int(8) DEFAULT NULL,
                                   `sign` varchar(255) DEFAULT NULL,
                                   `address` varchar(512) DEFAULT NULL,
                                   `real_name` varchar(128) DEFAULT NULL,
                                   `state` tinyint(4) DEFAULT '0',
                                   `email` varchar(255) DEFAULT NULL,
                                   `mobile` varchar(255) DEFAULT NULL,
                                   `student_id` varchar(255) DEFAULT NULL,
                                   `be_deleted` tinyint(4) DEFAULT '0',
                                   `creator_id` bigint(20) DEFAULT NULL,
                                   `creator_name` varchar(128) DEFAULT NULL,
                                   `modifier_id` bigint(20) DEFAULT NULL,
                                   `modifier_name` varchar(128) DEFAULT NULL,
                                   `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   `gmt_modified` datetime DEFAULT NULL,
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `wishes_chat_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wishes_chat_record` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT,
                               `sender_id` bigint(20) DEFAULT NULL,
                               `receiver_id` bigint(20) DEFAULT NULL,
                               `type` tinyint(4) DEFAULT NULL,
                               `content` int(8) DEFAULT NULL,
                               `be_deleted` tinyint(4) DEFAULT '0',
                               `creator_id` bigint(20) DEFAULT NULL,
                               `creator_name` varchar(128) DEFAULT NULL,
                               `modifier_id` bigint(20) DEFAULT NULL,
                               `modifier_name` varchar(128) DEFAULT NULL,
                               `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               `gmt_modified` datetime DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `wishes_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wishes_task` (
                                      `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                      `initiator_id` bigint(20) DEFAULT NULL,
                                      `receiver_id` bigint(20) DEFAULT NULL,
                                      `type_id` bigint(20) DEFAULT NULL,
                                      `title` varchar(128) DEFAULT NULL,
                                      `description` varchar(1022) DEFAULT NULL,
                                      `location` varchar(128) DEFAULT NULL,
                                      `image_url` varchar(255) DEFAULT NULL,
                                      `price` varchar(128) DEFAULT NULL,
                                      `state` tinyint(4) DEFAULT '0',
                                      `be_deleted` tinyint(4) DEFAULT '0',
                                      `creator_id` bigint(20) DEFAULT NULL,
                                      `creator_name` varchar(128) DEFAULT NULL,
                                      `modifier_id` bigint(20) DEFAULT NULL,
                                      `modifier_name` varchar(128) DEFAULT NULL,
                                      `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      `gmt_modified` datetime DEFAULT NULL,
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `wishes_task_draft`;
CREATE TABLE `wishes_task_draft` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT,
                               `task_id` bigint(20) DEFAULT '0',
                               `initiator_id` bigint(20) DEFAULT NULL,
                               `receiver_id` bigint(20) DEFAULT NULL,
                               `type` tinyint(4) DEFAULT NULL,
                               `title` varchar(128) DEFAULT NULL,
                               `description` varchar(1022) DEFAULT NULL,
                               `location` varchar(128) DEFAULT NULL,
                               `image_url` varchar(255) DEFAULT NULL,
                               `price` varchar(128) DEFAULT NULL,
                               `state` tinyint(4) DEFAULT '0',
                               `creator_id` bigint(20) DEFAULT NULL,
                               `creator_name` varchar(128) DEFAULT NULL,
                               `modifier_id` bigint(20) DEFAULT NULL,
                               `modifier_name` varchar(128) DEFAULT NULL,
                               `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               `gmt_modified` datetime DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `wishes_task_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wishes_task_type` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT,
                               `name` varchar(128) DEFAULT NULL,
                               `state` tinyint(4) DEFAULT '0',
                               `be_deleted` tinyint(4) DEFAULT '0',
                               `creator_id` bigint(20) DEFAULT NULL,
                               `creator_name` varchar(128) DEFAULT NULL,
                               `modifier_id` bigint(20) DEFAULT NULL,
                               `modifier_name` varchar(128) DEFAULT NULL,
                               `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               `gmt_modified` datetime DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `wishes_auth_privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wishes_auth_privilege` (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `name` varchar(64) DEFAULT NULL,
                                  `url` varchar(512) DEFAULT NULL,
                                  `request_type` tinyint DEFAULT NULL,
                                  `state` tinyint DEFAULT '0',
                                  `creator_id` bigint DEFAULT NULL COMMENT '创建用户id',
                                  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `modifier_id` bigint DEFAULT NULL COMMENT '修改用户id',
                                  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
                                  `creator_name` varchar(128) DEFAULT NULL COMMENT '创建用户名',
                                  `modifier_name` varchar(128) DEFAULT NULL COMMENT '修改用户名',
                                  PRIMARY KEY (`id`),
                                  UNIQUE KEY `auth_privilege_url_request_type_uindex` (`url`,`request_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='权限表';

DROP TABLE IF EXISTS `wishes_auth_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wishes_auth_role` (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `name` varchar(64) NOT NULL COMMENT '角色名称',
                             `descr` varchar(500) DEFAULT NULL COMMENT '角色描述',
                             `state` tinyint DEFAULT '0',
                             `creator_id` bigint DEFAULT NULL COMMENT '创建用户id',
                             `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `modifier_id` bigint DEFAULT NULL COMMENT '修改用户id',
                             `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
                             `creator_name` varchar(128) DEFAULT NULL COMMENT '创建用户名',
                             `modifier_name` varchar(128) DEFAULT NULL COMMENT '修改用户名',
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';

DROP TABLE IF EXISTS `wishes_auth_role_privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wishes_auth_role_privilege` (
                                       `id` bigint NOT NULL AUTO_INCREMENT,
                                       `role_id` bigint DEFAULT NULL,
                                       `privilege_id` bigint DEFAULT NULL,
                                       `creator_id` bigint DEFAULT NULL COMMENT '创建用户id',
                                       `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `modifier_id` bigint DEFAULT NULL COMMENT '修改用户id',
                                       `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
                                       `creator_name` varchar(128) DEFAULT NULL COMMENT '创建用户名',
                                       `modifier_name` varchar(128) DEFAULT NULL COMMENT '修改用户名',
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `auth_role_privilege_role_id_privilege_id_uindex` (`role_id`,`privilege_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色权限表';

DROP TABLE IF EXISTS `wishes_auth_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wishes_auth_user_role` (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `role_id` bigint DEFAULT NULL,
                                  `user_id` bigint DEFAULT NULL,
                                  `creator_id` bigint DEFAULT NULL COMMENT '创建用户id',
                                  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `modifier_id` bigint DEFAULT NULL COMMENT '修改用户id',
                                  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
                                  `creator_name` varchar(128) DEFAULT NULL COMMENT '创建用户名',
                                  `modifier_name` varchar(128) DEFAULT NULL COMMENT '修改用户名',
                                  PRIMARY KEY (`id`),
                                  UNIQUE KEY `auth_user_role_user_id_role_id_uindex` (`user_id`,`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=241 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色表';