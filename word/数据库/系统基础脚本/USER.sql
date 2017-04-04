prompt PL/SQL Developer import file
prompt Created on 2017年4月4日 by MinMin
set feedback off
set define off
prompt Creating KBMS_USER...
create table KBMS_USER
(
  id           VARCHAR2(36) default sys_guid() not null,
  user_name    VARCHAR2(20) not null,
  password     VARCHAR2(50) not null,
  real_name    VARCHAR2(10),
  login_time   VARCHAR2(19),
  locked       VARCHAR2(1),
  expired_date VARCHAR2(10),
  logout_time  VARCHAR2(19),
  update_time  DATE,
  update_by    VARCHAR2(20),
  ws_key       VARCHAR2(50)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on column KBMS_USER.id
  is 'ID';
comment on column KBMS_USER.user_name
  is '用户名';
comment on column KBMS_USER.password
  is '密码';
comment on column KBMS_USER.real_name
  is '真实姓名';
comment on column KBMS_USER.login_time
  is '登录时间(YYYY-MM-DD hh:mm:ss)';
comment on column KBMS_USER.locked
  is '是否被锁';
comment on column KBMS_USER.expired_date
  is '失效日期(YYYY-MM-DD)';
comment on column KBMS_USER.logout_time
  is '注销时间(YYYY-MM-DD hh:mm:ss)';
comment on column KBMS_USER.ws_key
  is '访问WebService的密匙';

prompt Disabling triggers for KBMS_USER...
alter table KBMS_USER disable all triggers;
prompt Deleting KBMS_USER...
delete from KBMS_USER;
commit;
prompt Loading KBMS_USER...
insert into KBMS_USER (id, user_name, password, real_name, login_time, locked, expired_date, logout_time, update_time, update_by, ws_key)
values ('f070f5bb-fd66-48a8-8604-6735cae2a65a', 'qiushengming', 'BmoSFnGIeq63+n1H0KsBEwY/Pf90uN/OKbCvj5ovJ8w=', '邱胜明', null, '0', null, null, to_date('24-05-2016 09:46:54', 'dd-mm-yyyy hh24:mi:ss'), 'admin', null);
commit;
prompt 49 records loaded
prompt Enabling triggers for KBMS_USER...
alter table KBMS_USER enable all triggers;
set feedback on
set define on
prompt Done.
