

/*==============================================================*/
/* Table: idempotent                                            */
/*==============================================================*/
create table idempotent
(
   id                   bigint unsigned not null auto_increment comment 'id自增1',
   idem_key             varchar(100) not null comment '幂等key',
   exec_result          varchar(5000) not null default '' comment '执行结果',
   create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   update_time          timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
   primary key (id),
   unique key uk_idem_key (idem_key)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='幂等表';



