SELECT * FROM `system`.aggregate_function_combinators
SELECT * FROM `system`.asynchronous_metric_log
SELECT * FROM `system`.asynchronous_metrics
SELECT * FROM `system`.build_options
SELECT * FROM `system`.clusters								-- 集群
SELECT * FROM `system`.collations
SELECT * FROM `system`.contributors
SELECT * FROM `system`.current_roles

SELECT * FROM `system`.data_type_families					-- 数据类型族
SELECT * FROM `system`.functions							-- 函数

SELECT * FROM `system`.databases							-- 数据库
SELECT * FROM `system`.tables								-- 表
SELECT * FROM `system`.table_functions						-- 表函数
SELECT * FROM `system`.table_engines						-- 表引擎
SELECT * FROM `system`.columns								-- 列
SELECT * FROM `system`.formats								-- 格式

SELECT * FROM `system`.disks								-- 磁盘
SELECT * FROM `system`.storage_policies						-- 存储策略
SELECT * FROM `system`.parts								-- data parts
SELECT * FROM `system`.parts_columns						-- data part中列
SELECT * FROM `system`.detached_parts

SELECT * FROM `system`.dictionaries
SELECT * FROM `system`.distributed_ddl_queue
SELECT * FROM `system`.distribution_queue
SELECT * FROM `system`.enabled_roles
SELECT * FROM `system`.errors
SELECT * FROM `system`.events
SELECT * FROM `system`.grants
SELECT * FROM `system`.graphite_retentions
SELECT * FROM `system`.licenses
SELECT * FROM `system`.macros
SELECT * FROM `system`.merge_tree_settings
SELECT * FROM `system`.merges
SELECT * FROM `system`.metric_log
SELECT * FROM `system`.metrics
SELECT * FROM `system`.models
SELECT * FROM `system`.mutations

SELECT * FROM `system`.`privileges`
SELECT * FROM `system`.processes
SELECT * FROM `system`.query_log							-- 查询日志
SELECT * FROM `system`.query_thread_log
SELECT * FROM `system`.quota_limits
SELECT * FROM `system`.quota_usage
SELECT * FROM `system`.quotas
SELECT * FROM `system`.quotas_usage
SELECT * FROM `system`.replicas
SELECT * FROM `system`.replicated_fetches
SELECT * FROM `system`.replicated_merge_tree_settings
SELECT * FROM `system`.replication_queue
SELECT * FROM `system`.role_grants
SELECT * FROM `system`.roles
SELECT * FROM `system`.row_policies
SELECT * FROM `system`.settings
SELECT * FROM `system`.settings_profile_elements
SELECT * FROM `system`.settings_profiles
SELECT * FROM `system`.stack_trace
SELECT * FROM `system`.time_zones
SELECT * FROM `system`.trace_log
SELECT * FROM `system`.user_directories
SELECT * FROM `system`.users

SELECT * FROM `system`.zeros
SELECT * FROM `system`.zeros_mt
SELECT * FROM `system`.one
SELECT * FROM `system`.numbers
SELECT * FROM `system`.numbers_mt

GRANT SHOW USERS ON *.*
