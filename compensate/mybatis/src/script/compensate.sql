

/*==============================================================*/
/* Table: compensate                                            */
/*==============================================================*/
create table compensate
(
   id                   bigint unsigned not null auto_increment comment 'id',
   compensate_key       varchar(255) not null comment '补偿key',
   biz_id               varchar(100) not null default '' comment '业务id',
   compensate_type      varchar(50) not null default '' comment '补偿类型',
   method_args          varchar(5000) not null default '' comment '方法参数',
   next_trigger_time    datetime not null comment '下次触发时间',
   retry_count          tinyint unsigned not null default 0 comment '重试次数',
   compensate_status    tinyint unsigned not null default 0 comment '补偿状态: 0:补偿中, 1:失败; 2: 成功',
   compensate_result    tinyint unsigned not null default 0 comment '补偿结果: 0:失败; 1: 成功',
   alarm_count          tinyint unsigned not null default 0 comment '告警次数',
   has_args_ext         tinyint(1) not null default 0 comment '是否有扩展参数(当method_args存储长度不足时,将吧多余的数据用compensate_method_args_ext表存储)',
   is_delete            tinyint(1) unsigned not null default 0 comment '是否删除',
   create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   update_time          timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
   primary key (id),
   index idx_next_trigger_time_is_delete (next_trigger_time, is_delete)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='补偿表';



/*==============================================================*/
/* Table: compensate_method_args_ext                            */
/*==============================================================*/
create table compensate_method_args_ext
(
   id                   bigint unsigned not null auto_increment comment 'id',
   compensate_id        bigint not null comment '补偿id 关联补偿表',
   method_args          varchar(5000) not null default '' comment '方法参数',
   args_order           tinyint unsigned not null default 0 comment '参数排序(值越小越靠前)',
   create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   update_time          timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
   primary key (id),
   index idx_compensate_id(compensate_id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='补偿表方法参数扩展表';


