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
                                      `type` tinyint(4) DEFAULT NULL,
                                      `title` varchar(128) DEFAULT NULL,
                                      `description` varchar(255) DEFAULT NULL,
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
                               `description` varchar(255) DEFAULT NULL,
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