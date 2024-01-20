CREATE TABLE `cm_biz_task` (
       `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
       `biz_id` bigint(20) unsigned NOT NULL COMMENT '业务id',
       `biz_type` varchar(50) NOT NULL COMMENT '业务类型',
       `biz_param` varchar(5000) NOT NULL DEFAULT '' COMMENT '业务参数',
       `trigger_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '触发次数',
       `next_trigger_time` datetime NOT NULL COMMENT '下次触发时间',
       `task_partition` int(10) unsigned DEFAULT NULL COMMENT '任务分区',
       `task_status` int(11) NOT NULL COMMENT '任务状态(1: 待执行; 2: 执行中;  3: 执行成功;  4: 执行失败)',
       `biz_status` int(10) unsigned NOT NULL COMMENT '业务状态',
       `is_delete` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
       `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
       `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
       PRIMARY KEY (`id`),
       UNIQUE KEY `uk_biz_id_biz_type` (`biz_id`,`biz_type`),
       index `idx_task_status_next_trigger_time` (`task_status`, `next_trigger_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务表';