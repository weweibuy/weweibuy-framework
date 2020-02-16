
create table compensate
(
   id                   bigint unsigned not null auto_increment comment 'id',
   compensate_key       varchar(255) not null comment '补偿key',
   biz_id               varchar(255) not null default '' comment '业务id',
   method_args          text comment '方法参数',
   next_trigger_time    datetime not null comment '下次触发时间',
   retry_count          tinyint unsigned not null default 0 comment '重试次数',
   alarm_count          tinyint unsigned not null default 0 comment '告警次数',
   is_delete            tinyint(1) unsigned not null default 0 comment '是否删除',
   create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   update_time          timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
   primary key (id)
);

alter table compensate comment '补偿表';

/*==============================================================*/
/* Index: idx_next_trigger_time_is_delete                       */
/*==============================================================*/
create index idx_next_trigger_time_is_delete on compensate
(
   next_trigger_time,
   is_delete
);
