insert into users(username, password, enabled) values('user', '$2a$10$qwrPB8l0MEYP2r1Wfu4CmO7.iC48/M3dcW9fRQ56kcpxNYhaSJNvO', true);
insert into users(username, password, enabled) values('admin', '$2a$10$qwrPB8l0MEYP2r1Wfu4CmO7.iC48/M3dcW9fRQ56kcpxNYhaSJNvO', true);
insert into users(username, password, enabled) values('user1', '$2a$10$qwrPB8l0MEYP2r1Wfu4CmO7.iC48/M3dcW9fRQ56kcpxNYhaSJNvO', false);
insert into users(username, password, enabled) values('user2', '$2a$10$qwrPB8l0MEYP2r1Wfu4CmO7.iC48/M3dcW9fRQ56kcpxNYhaSJNvO', true);

insert into authorities(username, authority) values('user', 'ROLE_USER');
insert into authorities(username, authority) values('admin', 'ROLE_ADMIN');
insert into authorities(username, authority) values('user1', 'ROLE_USER');
insert into authorities(username, authority) values('user2', 'ROLE_NULL');

insert into account(name, version, created_by, created_date, last_modified_by, last_modified_date) values('aaa111', 0, 'admin', '1970-01-01 00:00:00.0', 'admin', '1970-01-01 00:00:00.0');
insert into account(name, version, created_by, created_date, last_modified_by, last_modified_date) values('bbb222', 0, 'admin', '1970-01-01 00:00:00.0', 'admin', '1970-01-01 00:00:00.0');
insert into account(name, version, created_by, created_date, last_modified_by, last_modified_date) values('ccc333', 0, 'admin', '1970-01-01 00:00:00.0', 'admin', '1970-01-01 00:00:00.0');
insert into account(name, version, created_by, created_date, last_modified_by, last_modified_date) values('ddd444', 0, 'admin', '1970-01-01 00:00:00.0', 'admin', '1970-01-01 00:00:00.0');
insert into account(name, version, created_by, created_date, last_modified_by, last_modified_date) values('eee555', 0, 'admin', '1970-01-01 00:00:00.0', 'admin', '1970-01-01 00:00:00.0');
insert into account(name, version, created_by, created_date, last_modified_by, last_modified_date) values('fff666', 0, 'admin', '1970-01-01 00:00:00.0', 'admin', '1970-01-01 00:00:00.0');
insert into account(name, version, created_by, created_date, last_modified_by, last_modified_date) values('ggg777', 0, 'admin', '1970-01-01 00:00:00.0', 'admin', '1970-01-01 00:00:00.0');

--DROP PROCEDURE APP.SYS_GC;
--CREATE PROCEDURE APP.SYS_GC() LANGUAGE JAVA PARAMETER STYLE JAVA EXTERNAL NAME 'java.lang.System.gc';