
create database credit_seamiter_local_dev_db;
use credit_seamiter_local_dev_db;

# Dump of table sl_additional_item_tab
# ------------------------------------------------------------

CREATE TABLE `sl_additional_item_tab` (
                                          `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                          `mock_rule_id` bigint(20) NOT NULL,
                                          `ky` varchar(255) NOT NULL DEFAULT '',
                                          `val` varchar(255) NOT NULL DEFAULT '',
                                          PRIMARY KEY (`id`),
                                          UNIQUE KEY `i_m_k_v` (`mock_rule_id`,`ky`,`val`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_alarm_item_tab
# ------------------------------------------------------------

CREATE TABLE `sl_alarm_item_tab` (
                                     `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                     `alarm_rule_id` bigint(20) NOT NULL,
                                     `resource_type` tinyint(4) NOT NULL,
                                     `alarm_type` tinyint(4) NOT NULL,
                                     `check_period_in_min` int(10) NOT NULL,
                                     `effect_type` tinyint(4) NOT NULL,
                                     `alarm_threshold` int(10) NOT NULL,
                                     `wait_period` int(10) NOT NULL,
                                     `gmt_create` datetime DEFAULT NULL,
                                     `gmt_modified` datetime DEFAULT NULL,
                                     PRIMARY KEY (`id`),
                                     UNIQUE KEY `i_alarm_rule_id_resource_type_alarm_type` (`alarm_rule_id`,`resource_type`,`alarm_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_alarm_rule_tab
# ------------------------------------------------------------

CREATE TABLE `sl_alarm_rule_tab` (
                                     `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                     `app` varchar(255) NOT NULL,
                                     `resource_type` tinyint(4) NOT NULL,
                                     `alarm_name` varchar(255) NOT NULL,
                                     `alarm_level` tinyint(4) NOT NULL,
                                     `trigger_condition` tinyint(4) NOT NULL,
                                     `alarm_period_in_min` int(10) NOT NULL,
                                     `open` tinyint(1) NOT NULL DEFAULT '0',
                                     `notify_url` varchar(255) DEFAULT '',
                                     `notify_users` varchar(1024) DEFAULT '',
                                     `resource_ides` varchar(2048) DEFAULT '',
                                     `gmt_create` datetime DEFAULT NULL,
                                     `gmt_modified` datetime DEFAULT NULL,
                                     PRIMARY KEY (`id`),
                                     UNIQUE KEY `i_app_resource_type_alarm_name` (`app`,`resource_type`,`alarm_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_app_info_tab
# ------------------------------------------------------------

CREATE TABLE `sl_app_info_tab` (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                   `app` varchar(255) NOT NULL DEFAULT '',
                                   `app_type` tinyint(4) NOT NULL DEFAULT '0',
                                   `remark` varchar(255) NOT NULL,
                                   `address_list` text,
                                   `online_status` tinyint(4) NOT NULL DEFAULT '0',
                                   `current_access_token` varchar(64) DEFAULT '',
                                   `last_access_token` varchar(64) DEFAULT '',
                                   `token_effective` varchar(64) DEFAULT '',
                                   `token_effective_date` datetime DEFAULT NULL,
                                   `ugroups` varchar(1024) DEFAULT '',
                                   `alarm_seatalk` varchar(1024) DEFAULT '',
                                   `report_receiver` varchar(1024) DEFAULT '',
                                   `gmt_create` datetime DEFAULT NULL,
                                   `gmt_modified` datetime DEFAULT NULL,
                                   `grpc_port` int(10) DEFAULT NULL,
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `app` (`app`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_auth_user_tab
# ------------------------------------------------------------

CREATE TABLE `sl_auth_user_tab` (
                                    `id` int(11) NOT NULL AUTO_INCREMENT,
                                    `username` varchar(64) NOT NULL,
                                    `pwd` varchar(64) DEFAULT NULL,
                                    `email` varchar(64) DEFAULT NULL,
                                    `role_name` varchar(128) DEFAULT NULL,
                                    `permission_groups` varchar(2048) DEFAULT NULL,
                                    `permission_apps` varchar(2048) DEFAULT NULL,
                                    `permission_platforms` varchar(2048) DEFAULT NULL,
                                    `add_time` datetime DEFAULT NULL,
                                    `update_time` datetime DEFAULT NULL,
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `i_username` (`username`) USING BTREE,
                                    KEY `i_role_name` (`role_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_degrade_rule_tab
# ------------------------------------------------------------

CREATE TABLE `sl_degrade_rule_tab` (
                                       `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                       `app` varchar(255) NOT NULL DEFAULT '',
                                       `limit_app` varchar(255) DEFAULT '',
                                       `resource` varchar(255) NOT NULL DEFAULT '',
                                       `threshold` decimal(12,4) NOT NULL DEFAULT '0.0000',
                                       `retry_timeout_ms` int(10) NOT NULL DEFAULT '0',
                                       `strategy` tinyint(4) NOT NULL DEFAULT '0',
                                       `min_request_amount` int(10) NOT NULL DEFAULT '0',
                                       `slow_ratio_threshold` decimal(12,4) NOT NULL DEFAULT '0.0000',
                                       `stat_interval_ms` int(10) DEFAULT '0',
                                       `probe_num` bigint(20) NOT NULL DEFAULT '0',
                                       `gmt_create` datetime DEFAULT NULL,
                                       `gmt_modified` datetime DEFAULT NULL,
                                       `open` tinyint(1) NOT NULL DEFAULT '0',
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `i_app_resource` (`app`,`resource`),
                                       KEY `i_resource` (`resource`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_flow_rule_tab
# ------------------------------------------------------------

CREATE TABLE `sl_flow_rule_tab` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                    `app` varchar(255) NOT NULL DEFAULT '',
                                    `limit_app` varchar(255) DEFAULT '',
                                    `resource` varchar(255) NOT NULL DEFAULT '',
                                    `grade` tinyint(4) NOT NULL DEFAULT '0',
                                    `threshold` decimal(12,4) NOT NULL DEFAULT '0.0000',
                                    `relation_strategy` tinyint(4) NOT NULL DEFAULT '0',
                                    `ref_resource` varchar(255) DEFAULT '',
                                    `control_behavior` tinyint(4) NOT NULL DEFAULT '0',
                                    `warm_up_period_sec` int(10) NOT NULL DEFAULT '0',
                                    `max_queueing_time_ms` int(10) NOT NULL DEFAULT '0',
                                    `cluster_mode` tinyint(1) DEFAULT '0',
                                    `gmt_create` datetime DEFAULT NULL,
                                    `gmt_modified` datetime DEFAULT NULL,
                                    `flow_id` bigint(20) DEFAULT NULL,
                                    `threshold_type` tinyint(4) DEFAULT NULL,
                                    `fallback_to_local_when_fail` tinyint(1) DEFAULT NULL,
                                    `cluster_strategy` tinyint(4) DEFAULT NULL,
                                    `sample_count` int(10) DEFAULT NULL,
                                    `window_interval_ms` int(10) DEFAULT NULL,
                                    `resource_timeout` int(10) DEFAULT NULL,
                                    `resource_timeout_strategy` tinyint(4) DEFAULT NULL,
                                    `client_offline_time` bigint(20) DEFAULT NULL,
                                    `token_calculate_strategy` tinyint(4) NOT NULL DEFAULT '0',
                                    `low_mem_usage_threshold` bigint(20) DEFAULT '0',
                                    `mem_low_water_mark_bytes` bigint(20) DEFAULT '0',
                                    `high_mem_usage_threshold` bigint(20) DEFAULT '0',
                                    `mem_high_water_mark_bytes` bigint(20) DEFAULT '0',
                                    `open` tinyint(1) NOT NULL DEFAULT '0',
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `i_app_resource` (`app`,`resource`),
                                    KEY `i_resource` (`resource`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_global_lock_tab
# ------------------------------------------------------------

CREATE TABLE `sl_global_lock_tab` (
                                      `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                      `lock_name` varchar(255) NOT NULL,
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `i_l` (`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_gray_condition_tab
# ------------------------------------------------------------

CREATE TABLE `sl_gray_condition_tab` (
                                         `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                         `gray_rule_id` bigint(20) NOT NULL DEFAULT '0',
                                         `position` bigint(20) DEFAULT '0',
                                         `effective_addresses` varchar(2048) NOT NULL DEFAULT '',
                                         `target_resource` varchar(255) NOT NULL DEFAULT '',
                                         `target_version` varchar(255) NOT NULL DEFAULT '',
                                         `open` tinyint(1) NOT NULL DEFAULT '0',
                                         `conditions` tinyint(4) NOT NULL DEFAULT '0',
                                         PRIMARY KEY (`id`),
                                         UNIQUE KEY `gray_resource_version` (`gray_rule_id`,`target_resource`,`target_version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_gray_param_tab
# ------------------------------------------------------------

CREATE TABLE `sl_gray_param_tab` (
                                     `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                     `gray_rule_id` bigint(20) NOT NULL DEFAULT '0',
                                     `gray_condition_id` bigint(20) NOT NULL DEFAULT '0',
                                     `router_parameter_type` tinyint(4) NOT NULL DEFAULT '0',
                                     `param_key` varchar(255) NOT NULL DEFAULT '',
                                     `param_value` varchar(255) NOT NULL DEFAULT '',
                                     `op` tinyint(4) NOT NULL DEFAULT '0',
                                     PRIMARY KEY (`id`),
                                     UNIQUE KEY `gray_condition_type_key_value_expression` (`gray_rule_id`,`gray_condition_id`,`router_parameter_type`,`param_key`,`param_value`,`op`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_gray_rule_tab
# ------------------------------------------------------------

CREATE TABLE `sl_gray_rule_tab` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                    `app` varchar(255) NOT NULL DEFAULT '',
                                    `limit_app` varchar(255) NOT NULL DEFAULT '',
                                    `rule_name` varchar(255) NOT NULL DEFAULT '',
                                    `resource` varchar(255) NOT NULL DEFAULT '',
                                    `gray_tag` varchar(255) NOT NULL DEFAULT '',
                                    `link_pass` tinyint(1) NOT NULL DEFAULT '0',
                                    `router_strategy` tinyint(4) NOT NULL DEFAULT '0',
                                    `is_force` tinyint(1) NOT NULL DEFAULT '0',
                                    `black_ip_address` varchar(2048) NOT NULL DEFAULT '',
                                    `white_ip_address` varchar(2048) NOT NULL DEFAULT '',
                                    `open` tinyint(1) NOT NULL DEFAULT '0',
                                    `gmt_create` datetime DEFAULT NULL,
                                    `gmt_modified` datetime DEFAULT NULL,
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `app_resource` (`app`,`resource`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_gray_tag_tab
# ------------------------------------------------------------

CREATE TABLE `sl_gray_tag_tab` (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                   `gray_rule_id` bigint(20) NOT NULL DEFAULT '0',
                                   `tag_key` varchar(255) NOT NULL DEFAULT '',
                                   `tag_value` varchar(255) NOT NULL DEFAULT '',
                                   `effective_addresses` varchar(2048) NOT NULL DEFAULT '',
                                   `target_resource` varchar(255) NOT NULL DEFAULT '',
                                   `target_version` varchar(255) NOT NULL DEFAULT '',
                                   `open` tinyint(1) NOT NULL DEFAULT '0',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `gray_tag_key_name` (`gray_rule_id`,`tag_key`,`tag_value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_gray_weight_tab
# ------------------------------------------------------------

CREATE TABLE `sl_gray_weight_tab` (
                                      `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                      `gray_rule_id` bigint(20) NOT NULL DEFAULT '0',
                                      `target_resource` varchar(255) NOT NULL DEFAULT '',
                                      `target_version` varchar(255) NOT NULL DEFAULT '',
                                      `weight` decimal(12,4) NOT NULL DEFAULT '0.0000',
                                      `effective_addresses` varchar(2048) NOT NULL DEFAULT '',
                                      `open` tinyint(1) NOT NULL DEFAULT '0',
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `gray_resource_version` (`gray_rule_id`,`target_resource`,`target_version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_grpc_file_set_tab
# ------------------------------------------------------------

CREATE TABLE `sl_grpc_file_set_tab` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                        `service_id` bigint(20) NOT NULL DEFAULT '0',
                                        `hash_code` int(10) NOT NULL DEFAULT '0',
                                        `proto_data` mediumblob,
                                        `gmt_create` datetime DEFAULT NULL,
                                        `gmt_modified` datetime DEFAULT NULL,
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `i_service_id_hashcode` (`service_id`,`hash_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_grpc_method_tab
# ------------------------------------------------------------

CREATE TABLE `sl_grpc_method_tab` (
                                      `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                      `service_id` bigint(20) NOT NULL DEFAULT '0',
                                      `grpc_file_set_id` bigint(20) NOT NULL DEFAULT '0',
                                      `service_name` varchar(255) NOT NULL DEFAULT '',
                                      `service_full_name` varchar(255) NOT NULL DEFAULT '',
                                      `method_name` varchar(255) DEFAULT '',
                                      `method_full_name` varchar(255) DEFAULT '',
                                      `method_desc` varchar(255) DEFAULT '',
                                      `request_body` varchar(512) DEFAULT '',
                                      `request_body_detail` varchar(7000) DEFAULT '',
                                      `response` varchar(512) DEFAULT '',
                                      `response_detail` varchar(7000) DEFAULT '',
                                      `gmt_create` datetime DEFAULT NULL,
                                      `version` varchar(64) DEFAULT '',
                                      `child_id` bigint(20) NOT NULL DEFAULT '0',
                                      `parent_id` bigint(20) NOT NULL DEFAULT '0',
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `i_service_id_service_full_name_method_full_name_grpc_file_set_id` (`service_id`,`service_full_name`,`method_full_name`,`grpc_file_set_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_grpc_request_tab
# ------------------------------------------------------------

CREATE TABLE `sl_grpc_request_tab` (
                                       `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                       `service_id` bigint(20) NOT NULL DEFAULT '0',
                                       `grpc_method_id` bigint(20) NOT NULL DEFAULT '0',
                                       `field_name` varchar(255) NOT NULL DEFAULT '',
                                       `field_type` varchar(255) NOT NULL DEFAULT '',
                                       `required` tinyint(1) NOT NULL DEFAULT '0',
                                       `new_added` tinyint(1) NOT NULL DEFAULT '0',
                                       `db_column` varchar(255) NOT NULL DEFAULT '',
                                       `description` varchar(1024) NOT NULL DEFAULT '',
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `method_field` (`grpc_method_id`,`field_name`),
                                       UNIQUE KEY `service_method_field` (`service_id`,`grpc_method_id`,`field_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_grpc_response_tab
# ------------------------------------------------------------

CREATE TABLE `sl_grpc_response_tab` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                        `service_id` bigint(20) NOT NULL DEFAULT '0',
                                        `grpc_method_id` bigint(20) NOT NULL DEFAULT '0',
                                        `field_name` varchar(255) NOT NULL DEFAULT '',
                                        `field_type` varchar(255) NOT NULL DEFAULT '',
                                        `required` tinyint(1) NOT NULL DEFAULT '0',
                                        `new_added` tinyint(1) NOT NULL DEFAULT '0',
                                        `db_column` varchar(255) NOT NULL DEFAULT '',
                                        `description` varchar(1024) NOT NULL DEFAULT '',
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `method_field` (`grpc_method_id`,`field_name`),
                                        UNIQUE KEY `service_method_field` (`service_id`,`grpc_method_id`,`field_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_job_info_tab
# ------------------------------------------------------------

CREATE TABLE `sl_job_info_tab` (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                   `business_id` bigint(20) NOT NULL,
                                   `job_type` varchar(32) NOT NULL,
                                   `app_name` varchar(255) NOT NULL,
                                   `job_name` varchar(512) DEFAULT '',
                                   `schedule_type` varchar(32) NOT NULL,
                                   `schedule_conf` varchar(128) NOT NULL DEFAULT '',
                                   `misfire_strategy` varchar(32) DEFAULT '0',
                                   `trigger_status` tinyint(4) DEFAULT '0',
                                   `trigger_last_time` bigint(20) DEFAULT '0',
                                   `trigger_next_time` bigint(20) DEFAULT '0',
                                   `gmt_create` datetime DEFAULT NULL,
                                   `gmt_modified` datetime DEFAULT NULL,
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `i_app_name_business_id_job_type` (`app_name`,`business_id`,`job_type`),
                                   KEY `i_job_name` (`job_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_job_lock_tab
# ------------------------------------------------------------

CREATE TABLE `sl_job_lock_tab` (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                   `lock_name` varchar(50) NOT NULL,
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `lock_name` (`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_machine_info_tab
# ------------------------------------------------------------

CREATE TABLE `sl_machine_info_tab` (
                                       `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                       `app_id` bigint(20) NOT NULL DEFAULT '0',
                                       `app` varchar(255) NOT NULL,
                                       `app_type` tinyint(4) NOT NULL DEFAULT '0',
                                       `hostname` varchar(255) NOT NULL,
                                       `ip` varchar(128) NOT NULL,
                                       `machine_port` int(11) NOT NULL DEFAULT '0',
                                       `last_heartbeat` bigint(20) NOT NULL DEFAULT '0',
                                       `heartbeat_version` bigint(20) NOT NULL DEFAULT '0',
                                       `version` varchar(255) DEFAULT NULL,
                                       `registry_token` varchar(255) DEFAULT NULL,
                                       `grpc_port` int(10) DEFAULT NULL,
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `app_ip_port_app_type` (`app`,`ip`,`machine_port`,`app_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_menu_tab
# ------------------------------------------------------------

CREATE TABLE `sl_menu_tab` (
                               `id` int(11) NOT NULL AUTO_INCREMENT,
                               `menu_name` varchar(128) NOT NULL,
                               `href` varchar(128) DEFAULT NULL,
                               `title` varchar(128) DEFAULT NULL,
                               `icon` varchar(128) DEFAULT NULL,
                               `image` varchar(128) DEFAULT NULL,
                               `target` varchar(128) DEFAULT NULL,
                               `parent` varchar(128) DEFAULT NULL,
                               `operator` tinyint(1) DEFAULT NULL,
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `i_menu_name` (`menu_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_mock_item_tab
# ------------------------------------------------------------

CREATE TABLE `sl_mock_item_tab` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                    `mock_rule_id` bigint(20) NOT NULL,
                                    `when_param_idx` int(10) DEFAULT '0' COMMENT '参数索引',
                                    `when_param_key` varchar(255) DEFAULT '' COMMENT '参数名称，优先级大于参数索引',
                                    `control_behavior` tinyint(4) DEFAULT '0' COMMENT '0:什么也不做，1:抛出异常，2:返回Mock数据，3:等待,4:等待指定时间后抛出异常,5:等待指定时间后返回数据',
                                    `match_pattern` tinyint(4) DEFAULT '0' COMMENT '0:精确匹配，1:前缀匹配，2:后缀匹配，3:包含匹配,4:正则匹配',
                                    `when_param_kind` varchar(32) NOT NULL DEFAULT '' COMMENT ' 参数类型 ',
                                    `when_param_val` varchar(255) NOT NULL DEFAULT '' COMMENT '参数值',
                                    `then_return_waiting_time_ms` int(10) DEFAULT NULL,
                                    `then_return_mock_data` text,
                                    `then_throw_msg` varchar(255) DEFAULT '' COMMENT '抛出异常',
                                    `mock_replace` tinyint(4) DEFAULT '0' COMMENT '是否开启Mock替换',
                                    `replace_attribute` varchar(255) DEFAULT '' COMMENT '替换属性',
                                    `gmt_create` datetime DEFAULT NULL,
                                    `gmt_modified` datetime DEFAULT NULL,
                                    `position` bigint(20) DEFAULT '0' COMMENT '位置',
                                    `additional_item_key` varchar(255) DEFAULT NULL,
                                    `additional_item_value` varchar(255) DEFAULT NULL,
                                    `tag_name` varchar(255) DEFAULT NULL,
                                    `open` tinyint(1) NOT NULL DEFAULT '0',
                                    `param_op` int(10) DEFAULT '0',
                                    `when_param_key2` varchar(255) DEFAULT '',
                                    `when_param_kind2` varchar(32) NOT NULL DEFAULT '',
                                    `when_param_val2` varchar(255) NOT NULL DEFAULT '',
                                    `when_param_key3` varchar(255) DEFAULT '',
                                    `when_param_kind3` varchar(32) NOT NULL DEFAULT '',
                                    `when_param_val3` varchar(255) NOT NULL DEFAULT '',
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `mock_rule_id_tag_name` (`mock_rule_id`,`tag_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_mock_rule_tab
# ------------------------------------------------------------

CREATE TABLE `sl_mock_rule_tab` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                    `app` varchar(255) NOT NULL,
                                    `limit_app` varchar(255) DEFAULT NULL,
                                    `resource` varchar(255) NOT NULL,
                                    `strategy` tinyint(4) DEFAULT '0' COMMENT '0:Func,1:Param',
                                    `control_behavior` tinyint(4) DEFAULT '0' COMMENT '0:什么也不做，1:抛出异常，2:返回Mock数据，3:等待,4:等待指定时间后抛出异常,5:等待指定时间后返回数据',
                                    `then_return_waiting_time_ms` int(10) DEFAULT NULL,
                                    `then_return_mock_data` text,
                                    `then_throw_msg` varchar(255) NOT NULL DEFAULT '' COMMENT '抛出异常',
                                    `op` tinyint(4) NOT NULL DEFAULT '0',
                                    `open` tinyint(1) NOT NULL DEFAULT '0',
                                    `gmt_create` datetime DEFAULT NULL,
                                    `gmt_modified` datetime DEFAULT NULL,
                                    `rule_name` varchar(255) DEFAULT '' COMMENT '规则名称',
                                    `request_hold` tinyint(1) DEFAULT '0' COMMENT '请求保留',
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `i_app_resource` (`app`,`resource`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_notify_info_tab
# ------------------------------------------------------------

CREATE TABLE `sl_notify_info_tab` (
                                      `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                      `app` varchar(255) NOT NULL,
                                      `alarm_rule_id` bigint(20) NOT NULL,
                                      `alarm_name` varchar(255) NOT NULL,
                                      `alarm_type` tinyint(4) NOT NULL,
                                      `alarm_level` tinyint(4) NOT NULL,
                                      `resource_type_metric` tinyint(4) NOT NULL,
                                      `alarm_obj` varchar(255) NOT NULL,
                                      `notify_content` varchar(1024) NOT NULL DEFAULT '',
                                      `notify_url` varchar(255) NOT NULL DEFAULT '',
                                      `notify_users` varchar(1024) NOT NULL DEFAULT '',
                                      `alarm_status` tinyint(4) NOT NULL DEFAULT '0',
                                      `gmt_create` datetime DEFAULT NULL,
                                      PRIMARY KEY (`id`),
                                      KEY `i_app` (`app`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_param_flow_item_tab
# ------------------------------------------------------------

CREATE TABLE `sl_param_flow_item_tab` (
                                          `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                          `param_flow_id` bigint(20) NOT NULL,
                                          `param_value` varchar(255) NOT NULL DEFAULT '',
                                          `threshold` int(10) NOT NULL DEFAULT '0',
                                          `param_kind` varchar(255) NOT NULL DEFAULT '',
                                          `open` tinyint(1) NOT NULL DEFAULT '0',
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_param_flow_rule_tab
# ------------------------------------------------------------

CREATE TABLE `sl_param_flow_rule_tab` (
                                          `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                          `app` varchar(255) NOT NULL DEFAULT '',
                                          `limit_app` varchar(255) DEFAULT '',
                                          `resource` varchar(255) NOT NULL DEFAULT '',
                                          `metric_type` tinyint(4) NOT NULL DEFAULT '1',
                                          `param_idx` int(10) DEFAULT NULL,
                                          `threshold` decimal(12,4) NOT NULL DEFAULT '0.0000',
                                          `control_behavior` tinyint(4) NOT NULL DEFAULT '0',
                                          `max_queueing_time_ms` int(10) DEFAULT NULL,
                                          `burst_count` int(10) NOT NULL DEFAULT '0',
                                          `duration_in_sec` int(10) NOT NULL DEFAULT '1',
                                          `cluster_mode` tinyint(1) DEFAULT '0',
                                          `gmt_create` datetime DEFAULT NULL,
                                          `gmt_modified` datetime DEFAULT NULL,
                                          `flow_id` bigint(20) DEFAULT NULL,
                                          `threshold_type` tinyint(4) DEFAULT NULL,
                                          `fallback_to_local_when_fail` tinyint(1) DEFAULT NULL,
                                          `sample_count` int(10) DEFAULT NULL,
                                          `window_interval_ms` int(10) DEFAULT NULL,
                                          `open` tinyint(1) NOT NULL DEFAULT '0',
                                          PRIMARY KEY (`id`),
                                          UNIQUE KEY `i_app_resource_param_idx` (`app`,`resource`,`param_idx`),
                                          KEY `i_resource_param_idx` (`resource`,`param_idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_platform_tab
# ------------------------------------------------------------

CREATE TABLE `sl_platform_tab` (
                                   `id` int(11) NOT NULL AUTO_INCREMENT,
                                   `platform_name` varchar(128) NOT NULL,
                                   `platform_desc` varchar(128) NOT NULL,
                                   `platform_address` varchar(255) NOT NULL,
                                   `env` varchar(64) NOT NULL,
                                   `region` varchar(64) NOT NULL,
                                   `plat_status` tinyint(4) NOT NULL DEFAULT '0',
                                   `author` varchar(50) DEFAULT NULL,
                                   `add_time` datetime DEFAULT NULL,
                                   `update_time` datetime DEFAULT NULL,
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `i_p_n` (`platform_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_pressure_execute_log_tab
# ------------------------------------------------------------

CREATE TABLE `sl_pressure_execute_log_tab` (
                                               `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                               `pressure_rule_id` bigint(20) NOT NULL DEFAULT '0',
                                               `pressure_result_id` bigint(20) NOT NULL DEFAULT '0',
                                               `pressure_type` tinyint(4) NOT NULL DEFAULT '0',
                                               `address` varchar(128) DEFAULT '',
                                               `params` varchar(2048) DEFAULT '',
                                               `results` text,
                                               `gmt_create` datetime DEFAULT NULL,
                                               PRIMARY KEY (`id`),
                                               KEY `i_pressure_rule_id` (`pressure_rule_id`),
                                               KEY `i_pressure_result_id` (`pressure_result_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_pressure_rule_tab
# ------------------------------------------------------------

CREATE TABLE `sl_pressure_rule_tab` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                        `scene_name` varchar(512) NOT NULL DEFAULT '',
                                        `app` varchar(255) NOT NULL DEFAULT '',
                                        `addresses` varchar(512) DEFAULT '',
                                        `service_group` varchar(255) DEFAULT '',
                                        `service_version` varchar(255) DEFAULT '',
                                        `service_name` varchar(255) NOT NULL DEFAULT '',
                                        `service_method` varchar(255) NOT NULL DEFAULT '',
                                        `param_types` varchar(512) DEFAULT '',
                                        `params` varchar(1024) DEFAULT '',
                                        `timeout_in_ms` int(10) DEFAULT NULL,
                                        `print_log` tinyint(1) DEFAULT '0',
                                        `pressure_mode` tinyint(4) DEFAULT NULL,
                                        `control_behavior` tinyint(4) DEFAULT NULL,
                                        `tps` int(10) DEFAULT NULL,
                                        `pressure_duration_in_min` int(10) DEFAULT NULL,
                                        `gmt_create` datetime DEFAULT NULL,
                                        `gmt_modified` datetime DEFAULT NULL,
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `i_app_scene_name` (`app`,`scene_name`),
                                        KEY `i_scene_name` (`scene_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_pressure_test_result_tab
# ------------------------------------------------------------

CREATE TABLE `sl_pressure_test_result_tab` (
                                               `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                               `pressure_rule_id` bigint(20) NOT NULL DEFAULT '0',
                                               `scene_name` varchar(512) NOT NULL DEFAULT '',
                                               `addresses` varchar(512) DEFAULT '',
                                               `service_group` varchar(255) DEFAULT '',
                                               `service_version` varchar(255) DEFAULT '',
                                               `service_name` varchar(255) NOT NULL DEFAULT '',
                                               `service_method` varchar(255) NOT NULL DEFAULT '',
                                               `param_types` varchar(512) DEFAULT '',
                                               `params` varchar(2048) DEFAULT '',
                                               `timeout_in_ms` int(10) DEFAULT NULL,
                                               `pressure_mode` tinyint(4) DEFAULT NULL,
                                               `control_behavior` tinyint(4) DEFAULT NULL,
                                               `tps` int(10) DEFAULT NULL,
                                               `pressure_duration_in_min` int(10) DEFAULT NULL,
                                               `total_request` bigint(20) NOT NULL DEFAULT '0',
                                               `avg_rt_in_ms` double(10,2) NOT NULL DEFAULT '0.00',
                                               `min_rt_in_ms` double(10,2) NOT NULL DEFAULT '0.00',
                                               `max_rt_in_ms` double(10,2) NOT NULL DEFAULT '0.00',
                                               `total_error` double(10,2) NOT NULL DEFAULT '0.00',
                                               `error_rate` double(10,2) NOT NULL DEFAULT '0.00',
                                               `tp80_in_ms` double(10,2) NOT NULL DEFAULT '0.00',
                                               `tp95_in_ms` double(10,2) NOT NULL DEFAULT '0.00',
                                               `tp99_in_ms` double(10,2) NOT NULL DEFAULT '0.00',
                                               `gmt_create` datetime DEFAULT NULL,
                                               `exception_rate` decimal(12,4) NOT NULL DEFAULT '0.0000',
                                               PRIMARY KEY (`id`),
                                               KEY `i_pressure_rule_id` (`pressure_rule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_push_rule_lock_tab
# ------------------------------------------------------------

CREATE TABLE `sl_push_rule_lock_tab` (
                                         `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                         `rule_type` varchar(255) NOT NULL,
                                         `trigger_last_time` bigint(13) NOT NULL DEFAULT '0',
                                         `trigger_next_time` bigint(13) NOT NULL DEFAULT '0',
                                         PRIMARY KEY (`id`),
                                         UNIQUE KEY `i_r` (`rule_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_real_resource_metric_tab
# ------------------------------------------------------------

CREATE TABLE `sl_real_resource_metric_tab` (
                                               `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                               `app` varchar(255) NOT NULL DEFAULT '',
                                               `resource` varchar(255) NOT NULL DEFAULT '',
                                               `pass_qps` bigint(20) NOT NULL DEFAULT '0',
                                               `success_qps` bigint(20) NOT NULL DEFAULT '0',
                                               `block_qps` bigint(20) NOT NULL DEFAULT '0',
                                               `exception_qps` bigint(20) NOT NULL DEFAULT '0',
                                               `rt` decimal(12,4) NOT NULL DEFAULT '0.0000',
                                               `concurrency` int(10) NOT NULL DEFAULT '0',
                                               `classification` tinyint(4) NOT NULL DEFAULT '0',
                                               `gmt_create` datetime DEFAULT NULL,
                                               `gmt_modified` datetime DEFAULT NULL,
                                               PRIMARY KEY (`id`),
                                               UNIQUE KEY `i_a_r` (`app`,`resource`),
                                               KEY `i_resource` (`resource`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_resource_metric_tab
# ------------------------------------------------------------

CREATE TABLE `sl_resource_metric_tab` (
                                          `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                          `app_id` bigint(20) DEFAULT '0',
                                          `app` varchar(255) NOT NULL DEFAULT '',
                                          `resource` varchar(255) NOT NULL DEFAULT '',
                                          `ts` datetime NOT NULL,
                                          `pass_qps` bigint(20) NOT NULL DEFAULT '0',
                                          `success_qps` bigint(20) NOT NULL DEFAULT '0',
                                          `block_qps` bigint(20) NOT NULL DEFAULT '0',
                                          `exception_qps` bigint(20) NOT NULL DEFAULT '0',
                                          `rt` bigint(20) NOT NULL DEFAULT '0',
                                          `count` bigint(20) NOT NULL DEFAULT '0',
                                          `resource_code` int(10) NOT NULL DEFAULT '0',
                                          `concurrency` int(10) NOT NULL DEFAULT '0',
                                          `classification` tinyint(4) NOT NULL DEFAULT '0',
                                          `gmt_create` datetime DEFAULT NULL,
                                          `gmt_modified` datetime DEFAULT NULL,
                                          PRIMARY KEY (`id`),
                                          UNIQUE KEY `i_a_r_t` (`app`,`resource`,`ts`),
                                          KEY `i_resource_ts` (`resource`,`ts`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_resource_request_tab
# ------------------------------------------------------------

CREATE TABLE `sl_resource_request_tab` (
                                           `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                           `app` varchar(255) NOT NULL,
                                           `resource_name` varchar(255) NOT NULL DEFAULT '0',
                                           `request_body` text,
                                           `gmt_create` datetime DEFAULT NULL,
                                           PRIMARY KEY (`id`),
                                           KEY `i_app_resource` (`app`,`resource_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_resource_rsp_tab
# ------------------------------------------------------------

CREATE TABLE `sl_resource_rsp_tab` (
                                       `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                       `app` varchar(255) NOT NULL,
                                       `resource_name` varchar(255) NOT NULL DEFAULT '0',
                                       `resource_type` tinyint(4) NOT NULL DEFAULT '0',
                                       `rsp` text,
                                       `gmt_create` datetime DEFAULT NULL,
                                       `gmt_modified` datetime DEFAULT NULL,
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `i_app_resource` (`app`,`resource_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_retry_rule_tab
# ------------------------------------------------------------

CREATE TABLE `sl_retry_rule_tab` (
                                     `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                     `app` varchar(255) NOT NULL DEFAULT '',
                                     `limit_app` varchar(255) NOT NULL DEFAULT '',
                                     `resource` varchar(255) NOT NULL DEFAULT '',
                                     `retry_policy` tinyint(4) NOT NULL DEFAULT '0',
                                     `retry_max_attempts` int(10) NOT NULL DEFAULT '0',
                                     `retry_timeout` bigint(20) NOT NULL DEFAULT '0',
                                     `backoff_policy` tinyint(4) NOT NULL DEFAULT '0',
                                     `fixed_backoff_period_in_ms` bigint(20) NOT NULL DEFAULT '0',
                                     `backoff_delay` bigint(20) NOT NULL DEFAULT '0',
                                     `backoff_max_delay` bigint(20) NOT NULL DEFAULT '0',
                                     `backoff_multiplier` int(10) NOT NULL DEFAULT '0',
                                     `uniform_min_backoff_period` bigint(20) NOT NULL DEFAULT '0',
                                     `uniform_max_backoff_period` bigint(20) NOT NULL DEFAULT '0',
                                     `error_matcher` tinyint(4) NOT NULL DEFAULT '0',
                                     `include_exceptions` varchar(2048) NOT NULL DEFAULT '',
                                     `exclude_exceptions` varchar(2048) NOT NULL DEFAULT '',
                                     `open` tinyint(1) NOT NULL DEFAULT '0',
                                     `gmt_create` datetime DEFAULT NULL,
                                     `gmt_modified` datetime DEFAULT NULL,
                                     `grpc_rsp_key` varchar(512) DEFAULT '',
                                     `grpc_rsp_values` varchar(2048) DEFAULT '',
                                     PRIMARY KEY (`id`),
                                     UNIQUE KEY `app_resource` (`app`,`resource`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_role_tab
# ------------------------------------------------------------

CREATE TABLE `sl_role_tab` (
                               `id` int(11) NOT NULL AUTO_INCREMENT,
                               `role_name` varchar(128) NOT NULL,
                               `author` varchar(64) DEFAULT NULL,
                               `permission_urls` varchar(2048) DEFAULT NULL,
                               `role_desc` varchar(512) DEFAULT NULL,
                               `add_time` datetime DEFAULT NULL,
                               `update_time` datetime DEFAULT NULL,
                               `permission_menus` varchar(2048) DEFAULT NULL,
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `i_role_name` (`role_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_rule_publish_tab
# ------------------------------------------------------------

CREATE TABLE `sl_rule_publish_tab` (
                                       `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                       `app` varchar(255) NOT NULL,
                                       `version` varchar(255) NOT NULL DEFAULT '' COMMENT 'v-time-uuid',
                                       `rule_type` tinyint(4) NOT NULL,
                                       `is_delete` tinyint(1) DEFAULT '0',
                                       `created_by` varchar(244) NOT NULL DEFAULT '',
                                       `gmt_create` datetime DEFAULT NULL,
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `i_app_version_rule_type` (`app`,`version`,`rule_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_system_metric_tab
# ------------------------------------------------------------

CREATE TABLE `sl_system_metric_tab` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                        `app_id` bigint(20) DEFAULT '0',
                                        `app` varchar(255) NOT NULL DEFAULT '',
                                        `ip` varchar(255) NOT NULL DEFAULT '',
                                        `machine_port` int(11) NOT NULL DEFAULT '0',
                                        `resource` varchar(255) NOT NULL DEFAULT '',
                                        `ts` datetime NOT NULL,
                                        `pass_qps` bigint(20) NOT NULL DEFAULT '0',
                                        `classification` tinyint(4) NOT NULL DEFAULT '0',
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `i_a_i_p_r_t` (`app`,`ip`,`machine_port`,`resource`,`ts`),
                                        KEY `i_ts` (`ts`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_system_rule_tab
# ------------------------------------------------------------

CREATE TABLE `sl_system_rule_tab` (
                                      `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                      `app` varchar(255) NOT NULL,
                                      `highest_system_load` decimal(12,4) DEFAULT '0.0000',
                                      `avg_rt` decimal(12,4) DEFAULT '0.0000',
                                      `max_thread` decimal(12,4) DEFAULT '0.0000',
                                      `min_request_amount` int(10) DEFAULT '0',
                                      `qps` decimal(12,4) DEFAULT '0.0000',
                                      `highest_cpu_usage` decimal(12,4) DEFAULT '0.0000',
                                      `gmt_create` datetime DEFAULT NULL,
                                      `gmt_modified` datetime DEFAULT NULL,
                                      `open` tinyint(1) NOT NULL DEFAULT '0',
                                      PRIMARY KEY (`id`),
                                      KEY `i_app` (`app`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sl_user_group_tab
# ------------------------------------------------------------

CREATE TABLE `sl_user_group_tab` (
                                     `id` int(11) NOT NULL AUTO_INCREMENT,
                                     `group_name` varchar(128) NOT NULL,
                                     `permission_apps` varchar(2048) DEFAULT NULL,
                                     `permission_platforms` varchar(2048) DEFAULT NULL,
                                     `group_desc` varchar(512) DEFAULT NULL,
                                     `author` varchar(64) DEFAULT NULL,
                                     `add_time` datetime DEFAULT NULL,
                                     `update_time` datetime DEFAULT NULL,
                                     PRIMARY KEY (`id`),
                                     UNIQUE KEY `i_group_name` (`group_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table SPRING_SESSION
# ------------------------------------------------------------

CREATE TABLE `SPRING_SESSION` (
                                  `PRIMARY_ID` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
                                  `SESSION_ID` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
                                  `CREATION_TIME` bigint(20) NOT NULL,
                                  `LAST_ACCESS_TIME` bigint(20) NOT NULL,
                                  `MAX_INACTIVE_INTERVAL` int(11) NOT NULL,
                                  `EXPIRY_TIME` bigint(20) NOT NULL,
                                  `PRINCIPAL_NAME` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                  PRIMARY KEY (`PRIMARY_ID`),
                                  UNIQUE KEY `SPRING_SESSION_IX1` (`SESSION_ID`),
                                  KEY `SPRING_SESSION_IX2` (`EXPIRY_TIME`),
                                  KEY `SPRING_SESSION_IX3` (`PRINCIPAL_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;



# Dump of table SPRING_SESSION_ATTRIBUTES
# ------------------------------------------------------------

CREATE TABLE `SPRING_SESSION_ATTRIBUTES` (
                                             `SESSION_PRIMARY_ID` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
                                             `ATTRIBUTE_NAME` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
                                             `ATTRIBUTE_BYTES` blob NOT NULL,
                                             PRIMARY KEY (`SESSION_PRIMARY_ID`,`ATTRIBUTE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;



INSERT INTO `sl_auth_user_tab` (`id`, `username`, `pwd`, `email`, `role_name`, `permission_groups`, `permission_apps`, `permission_platforms`, `add_time`, `update_time`)
VALUES
    (2, 'honggang.liu@shopee.com', '21232f297a57a5a743894a0e4a801fc3', 'honggang.liu@shopee.com', 'SYSTEM_MANAGER_ROLE', 'UserCenter,credit_core_group,fast-escrow-group', 'credit-dynamic-gateway-ea-proxy,fast-escrow-loan,fast-escrow-transaction,fast-escrow-order-local,credit_biz_proxy,fast-escrow-order,credit-usercenter-account,credit-usercenter-apply,credit-usercenter-bff,credit-usercenter-member', '', '2022-09-08 16:41:20', '2023-01-31 03:01:25');

INSERT INTO `sl_global_lock_tab` (`id`, `lock_name`)
VALUES
    (1, 'write_metric_lock');

INSERT INTO `sl_platform_tab` (`id`, `platform_name`, `platform_desc`, `platform_address`, `env`, `region`, `plat_status`, `author`, `add_time`, `update_time`)
VALUES
    (1, 'seamiter ph test', '', 'https://seamiter-ph.test.scredit.io', 'Test', 'PH', 1, NULL, '2023-01-31 15:24:10', '2023-01-31 15:24:10'),
    (2, 'seamiter my test', '', 'https://seamiter-my.test.scredit.io', 'Test', 'MY', 1, NULL, '2023-01-31 15:27:16', '2023-01-31 15:27:16'),
    (3, 'seamiter br test', '', 'https://seamiter-br.test.scredit.io', 'Test', 'BR', 1, NULL, '2023-01-31 15:27:48', '2023-01-31 15:27:48'),
    (4, 'seamiter th test', '', 'https://seamiter-th.test.scredit.io', 'Test', 'TH', 1, NULL, '2023-01-31 15:28:18', '2023-01-31 15:28:18'),
    (5, 'seamiter tw test', '', 'https://seamiter-tw.test.scredit.io', 'Test', 'TW', 1, NULL, '2023-01-31 15:28:59', '2023-01-31 15:28:59'),
    (6, 'seamiter sg test', '', 'https://seamiter-sg.test.scredit.io', 'Test', 'SG', 1, NULL, '2023-01-31 15:29:32', '2023-01-31 15:29:32'),
    (7, 'seamiter id test', '', 'https://seamiter-id.test.scredit.io', 'Test', 'ID', 1, NULL, '2023-01-31 15:30:01', '2023-01-31 15:30:01'),
    (8, 'seamiter vn test', '', 'https://seamiter-vn.test.scredit.io', 'Test', 'VN', 1, NULL, '2023-01-31 15:30:37', '2023-01-31 15:30:37'),
    (9, 'seamiter ph dev', '', 'https://seamiter-ph-env3.test.scredit.io/', 'Dev', 'PH', 1, NULL, '2023-01-31 23:53:02', '2023-01-31 23:53:02'),
    (10, 'seamiter my dev', '', 'https://seamiter-my-env3.test.scredit.io', 'Dev', 'MY', 1, NULL, '2023-01-31 23:53:48', '2023-01-31 23:53:48'),
    (11, 'seamiter br dev', '', 'https://seamiter-br-env3.test.scredit.io', 'Dev', 'BR', 1, NULL, '2023-01-31 23:54:32', '2023-01-31 23:54:32'),
    (12, 'seamiter th dev', '', 'https://seamiter-th-env3.test.scredit.io', 'Dev', 'TH', 1, NULL, '2023-01-31 23:55:16', '2023-01-31 23:55:16'),
    (15, 'seamiter tw dev', '', 'https://seamiter-tw-env3.test.scredit.io', 'Dev', 'TW', 1, NULL, '2023-01-31 23:56:16', '2023-01-31 23:56:16'),
    (16, 'seamiter sg dev', '', 'https://seamiter-sg-env3.test.scredit.io/', 'Dev', 'SG', 1, NULL, '2023-01-31 23:56:47', '2023-01-31 23:56:47'),
    (17, 'seamiter id dev', '', 'https://seamiter-id-env3.test.scredit.io/', 'Dev', 'ID', 1, NULL, '2023-01-31 23:57:11', '2023-01-31 23:57:11'),
    (18, 'seamiter vb dev', '', 'https://seamiter-vn-env3.test.scredit.io', 'Dev', 'VN', 1, NULL, '2023-01-31 23:57:45', '2023-01-31 23:57:45'),
    (19, 'seamiter my live', '', 'https://seamiter-my.scredit.io', 'Live', 'MY', 1, NULL, '2023-02-01 11:00:04', '2023-02-01 11:00:04');

INSERT INTO `sl_push_rule_lock_tab` (`id`, `rule_type`, `trigger_last_time`, `trigger_next_time`)
VALUES
    (1, 'FLOW_RULE_TYPE', 0, 0),
    (2, 'DEGRADE_RULE_TYPE', 0, 0),
    (3, 'PARAM_FLOW_RULE_TYPE', 0, 0),
    (4, 'SYSTEM_RULE_TYPE', 0, 0),
    (5, 'METRIC_REST_TYPE', 1679046764913, 1679046767913),
    (6, 'MOCK_RULE_TYPE', 0, 0),
    (7, 'RSP_FETCHER_TYPE', 0, 0);

INSERT INTO `sl_role_tab` (`id`, `role_name`, `author`, `permission_urls`, `role_desc`, `add_time`, `update_time`, `permission_menus`)
VALUES
    (2, 'operate_role', NULL, NULL, '操作角色', '2023-02-18 00:32:34', '2023-03-17 00:51:25', 'common_manager,real_monitor,link,flow_rule,degrade_rule,hotparam_rule,system_rule,service_mock,monitor_rule,pressure_rule,resource_lib,protect,host_list,user_manager,role_manager,menu_manager,ugroup_manager,retry_rule,gray_rule,system_manager,platform_manager,use_tutorial,app_add,retry_add,retry_remove,retry_update,degrade_remove,degrade_add,degrade_update,flow_add,flow_update,flow_remove,gray_add,gray_update,gray_remove,mock_add,mock_update,mock_remove,param_flow_add,param_flow_update,param_flow_remove,app_update,app_remove'),
    (3, 'qa_role', NULL, NULL, '测试角色', '2023-03-17 01:02:09', '2023-03-17 08:43:26', 'common_manager,real_monitor,link,flow_rule,service_mock,resource_lib,host_list,retry_rule,gray_rule,use_tutorial,app_add,retry_add,retry_remove,retry_update,flow_add,flow_update,flow_remove,mock_add,mock_update,mock_remove,app_update,app_remove'),
    (5, 'dev_role', NULL, NULL, '研发角色', '2023-03-17 08:46:45', '2023-03-17 08:46:45', 'common_manager,real_monitor,link,flow_rule,degrade_rule,hotparam_rule,system_rule,service_mock,resource_lib,host_list,user_manager,retry_rule,gray_rule,use_tutorial,app_add,retry_add,retry_remove,retry_update,degrade_remove,degrade_add,degrade_update,flow_add,flow_update,flow_remove,gray_add,gray_update,gray_remove,mock_add,mock_update,mock_remove,param_flow_add,param_flow_update,param_flow_remove,app_update,app_remove'),
    (6, 'readonly_role', NULL, NULL, '只读角色', '2023-03-17 08:49:58', '2023-03-17 08:54:50', 'common_manager,real_monitor,link,flow_rule,degrade_rule,hotparam_rule,system_rule,service_mock,monitor_rule,pressure_rule,resource_lib,protect,host_list,retry_rule,gray_rule,use_tutorial');

INSERT INTO `sl_menu_tab` (`id`, `menu_name`, `href`, `title`, `icon`, `image`, `target`, `parent`, `operator`)
VALUES
    (2, 'common_manager', NULL, '常规管理', 'fa fa-address-book', NULL, '_self', NULL, 0),
    (3, 'real_monitor', 'metric/resource_metric.html', '实时监控', 'fa fa-home', NULL, '_self', 'common_manager', 0),
    (4, 'link', 'resource/resource_list.html', '簇点链路', 'fa fa-address-book', NULL, '_self', 'common_manager', 0),
    (5, 'flow_rule', 'flow/rules_list.html', '流控规则', 'fa fa-anchor', NULL, '_self', 'common_manager', 0),
    (6, 'degrade_rule', 'degrade/rules_list.html', '熔断规则', 'fa fa-leaf', NULL, '_self', 'common_manager', 0),
    (7, 'hotparam_rule', 'param/rules_list.html', '热点规则', 'fa fa-address-book', NULL, '_self', 'common_manager', 0),
    (8, 'system_rule', 'system/rules_list.html', '系统规则', 'fa fa-gears', NULL, '_self', 'common_manager', 0),
    (10, 'service_mock', 'mock/rules_list.html', '服务Mock', 'fa fa-flag', NULL, '_self', 'common_manager', 0),
    (11, 'monitor_rule', 'alarm/rules_list.html', '告警管理', 'fa fa-warning', NULL, '_self', 'common_manager', 0),
    (12, 'pressure_rule', 'pressure/rules_list.html', '应用压测', 'fa fa-flash', NULL, '_self', 'common_manager', 0),
    (13, 'resource_lib', 'lib/resource_list.html', '资源集合', 'fa fa-tree', NULL, '_self', 'common_manager', 0),
    (14, 'protect', 'protect/event_list.html', '防护事件', 'fa fa-flask', NULL, '_self', 'common_manager', 0),
    (15, 'host_list', 'machine/app_list.html', '应用列表', 'fa fa-magnet', NULL, '_self', 'common_manager', 0),
    (17, 'user_manager', 'user/list.html', '用户管理', 'fa fa-tachometer', NULL, '_self', 'system_manager', 0),
    (18, 'role_manager', 'role/list.html', '角色管理', 'fa fa-stumbleupon-circle', NULL, '_self', 'system_manager', 0),
    (19, 'menu_manager', 'menu/list.html', '菜单管理', 'fa fa-viacoin', NULL, '_self', 'system_manager', 0),
    (20, 'ugroup_manager', 'ugroup/list.html', '分组管理', 'fa fa-shield', NULL, '_self', 'system_manager', 0),
    (23, 'retry_rule', 'retry/rules_list.html', '重试规则', 'fa fa-eye', NULL, '_self', 'common_manager', 0),
    (27, 'gray_rule', 'gray/rules_list.html', '灰度规则', 'fa fa-ship', NULL, '_self', 'common_manager', 0),
    (9999, 'system_manager', NULL, '系统管理', 'fa fa-window-maximize', NULL, '_self', 'common_manager', 0),
    (10000, 'platform_manager', 'platform/list.html', '平台管理', 'fa fa-share-alt-square', NULL, '_self', 'system_manager', 0),
    (10001, 'use_tutorial', 'help.html', '使用教程', 'fa fa-child', NULL, '_self', 'common_manager', 0),
    (10002, 'app_add', '/app/add', '应用-增加', '', '', NULL, '', 1),
    (10003, 'retry_add', '/retry/add', '重试-增加', '', '', NULL, 'retry', 1),
    (10004, 'retry_remove', '/retry/remove', '重试删除', '', '', NULL, 'retry_rule', 1),
    (10008, 'retry_update', '/retry/update', '重试-更新', '', '', NULL, 'retry_rule', 1),
    (10009, 'degrade_remove', '/degrade/remove', '熔断删除', '', '', NULL, 'degrade_rule', 1),
    (10010, 'degrade_add', '/degrade/add', '熔断-增加', '', '', NULL, 'degrade_rule', 1),
    (10011, 'degrade_update', '/degrade/update', '熔断-更新', '', '', NULL, 'degrade_rule', 1),
    (10012, 'flow_add', '/flow/add', '流控-增加', '', '', NULL, 'flow_rule', 1),
    (10014, 'flow_update', '/flow/update', '流控-更新', '', '', NULL, 'flow_rule', 1),
    (10015, 'flow_remove', '/flow/remove', '流控-删除', '', '', NULL, 'flow_rule', 1),
    (10016, 'gray_add', '/gray/add', '灰度-增加', '', '', NULL, 'gray_rule', 1),
    (10017, 'gray_update', '/gray/update', '灰度-更新', '', '', NULL, 'gray_rule', 1),
    (10018, 'gray_remove', '/gray/remove', '灰度-删除', '', '', NULL, 'gray_rule', 1),
    (10019, 'mock_add', '/mock/add', 'mock-增加', '', '', NULL, 'mock_rule', 1),
    (10020, 'mock_update', '/mock/update', 'mock-更新', '', '', NULL, 'mock_update', 1),
    (10021, 'mock_remove', '/mock/remove', 'mock-移除', '', '', NULL, 'mock_rule', 1),
    (10022, 'param_flow_add', '/paramFlow/add', '热点流控-增加', '', '', NULL, 'param_rule', 1),
    (10023, 'param_flow_update', '/paramFlow/update', '热点流控-更新', '', '', NULL, 'param_rule', 1),
    (10024, 'param_flow_remove', '/paramFlow/remove', '热点流控-删除', '', '', NULL, 'param_rule', 1),
    (10025, 'app_update', '/app/update', '应用-更新', '', '', NULL, '', 1),
    (10026, 'app_remove', '/app/remove', '应用删除', '', '', NULL, 'app_rule', 1);
