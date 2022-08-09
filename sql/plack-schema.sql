DROP TABLE IF EXISTS `news_connect`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `news_connect` (
                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                 `news_id` bigint DEFAULT NULL,
                                 `user_id` bigint DEFAULT NULL,
                                 `connect_type` varchar(128) DEFAULT NULL,
                                 `creator_id` bigint DEFAULT NULL COMMENT '创建用户id',
                                 `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `modifier_id` bigint DEFAULT NULL COMMENT '修改用户id',
                                 `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
                                 `creator_name` varchar(128) DEFAULT NULL COMMENT '创建用户名',
                                 `modifier_name` varchar(128) DEFAULT NULL COMMENT '修改用户名',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=241 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='新闻浏览表,新闻喜欢表,新闻收藏表';


DROP TABLE IF EXISTS `news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `news` (
                                         `id` bigint NOT NULL AUTO_INCREMENT,
                                         `news_title` varchar(128) DEFAULT NULL,
                                         `news_type` varchar(128) DEFAULT NULL,
                                         `author` varchar(128) DEFAULT NULL,
                                         `content` varchar(2048) DEFAULT NULL,
                                         'favor_count' bigint DEFAULT  '0' COMMENT '喜欢数',
                                         'collect_count' bigint DEFAULT  '0' COMMENT '收藏数',
                                         'browse_count' bigint DEFAULT  '0' COMMENT '浏览数',
                                         `creator_id` bigint DEFAULT NULL COMMENT '创建用户id',
                                         `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         `modifier_id` bigint DEFAULT NULL COMMENT '修改用户id',
                                         `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
                                         `creator_name` varchar(128) DEFAULT NULL COMMENT '创建用户名',
                                         `modifier_name` varchar(128) DEFAULT NULL COMMENT '修改用户名',
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=241 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='新闻表';

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT,
                               `user_name` varchar(128) DEFAULT NULL,
                               `password` varchar(255) DEFAULT NULL,
                               `state` tinyint(4) DEFAULT '0',
                               `email` varchar(255) DEFAULT NULL,
                               `mobile` varchar(255) DEFAULT NULL,
                               `be_deleted` tinyint(4) DEFAULT '0',
                               `creator_id` bigint(20) DEFAULT NULL,
                               `creator_name` varchar(128) DEFAULT NULL,
                               `modifier_id` bigint(20) DEFAULT NULL,
                               `modifier_name` varchar(128) DEFAULT NULL,
                               `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               `gmt_modified` datetime DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT,
                               `user_name` varchar(128) DEFAULT NULL,
                               `password` varchar(255) DEFAULT NULL,
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