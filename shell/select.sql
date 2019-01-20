create table if not exists tc_account_diff_detail
(
  id BIGINT(19) auto_increment
    primary key,
  code int not null ,
  charge INT(10) not null,
  period_id INT(10) not null,
  diff_type INT(10) not null,
  sharding_key INT(10) not null
);