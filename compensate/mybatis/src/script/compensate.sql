

create table cm_compensate
(
   id                   bigint unsigned not null auto_increment comment 'id',
   compensate_key       varchar(100) not null comment '补偿key',
   biz_id               varchar(50) not null default '' comment '业务id',
   compensate_type      varchar(50) not null default '' comment '补偿类型',
   method_args          varchar(5000) not null default '' comment '方法参数',
   next_trigger_time    datetime not null comment '下次触发时间',
   retry_count          int unsigned not null default 0 comment '重试次数',
   compensate_status    tinyint unsigned not null default 0 comment '补偿状态: 0:补偿中, 1:失败; 2: 成功',
   alarm_count          int unsigned not null default 0 comment '告警次数',
   has_args_ext         tinyint(1) unsigned not null default 0 comment '是否有扩展参数(当method_args存储长度不足时,将吧多余的数据用compensate_method_args_ext表存储)',
   is_delete            tinyint(1) unsigned not null default 0 comment '是否删除',
   create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   update_time          timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
   primary key (id),
   index idx_next_trigger_time_is_delete (next_trigger_time, is_delete)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='补偿表';


create table cm_compensate_args_ext
(
   id                   bigint unsigned not null auto_increment comment 'id',
   compensate_id        bigint unsigned not null comment '补偿id 关联补偿表',
   method_args          varchar(5000) not null default '' comment '方法参数',
   args_order           int unsigned not null default 0 comment '参数排序(值越小越靠前)',
   create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   update_time          timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
   primary key (id),
   index idx_compensate_id(compensate_id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='补偿表方法参数扩展表';

create table cm_compensate_log
(
   id                   bigint unsigned not null auto_increment comment 'id',
   compensate_id        bigint unsigned not null comment '补偿id 关联补偿表',
   trigger_type         varchar(30) not null comment '触发方式(SYSTEM:系统触发; FORCE:人工强制触发 )',
   compensate_state     varchar(30) not null comment '补偿状态(INIT:初始态;RETRY_ABLE:可以重试; ALARM_ABLE:可以报警; OVER_ALARM_COUNT:超出报警次数)',
   compensate_result    varchar(30) not null default '' comment '补偿结果: (RETRY_FAIL:补偿重试失败; RETRY_FAIL_RECOVER_FAIL:重试失败,触发恢复方法失败; RETRY_FAIL_RECOVER_SUCCESS:重试失败,触发恢复方法成功 ;RETRY_SUCCESS:补偿重试成功; ALARM:报警; OVER_ALARM_COUNT:超出报警上限 补偿失败)',
   exception_info       varchar(5000) not null default '' comment '异常信息',
   create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   update_time          timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
   primary key (id),
   index idx_compensate_id(compensate_id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='补偿日志表';


