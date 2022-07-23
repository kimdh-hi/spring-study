insert into role(id, name) values
    ('role-01', 'user'),
    ('role-09', 'admin')
;

insert into role_authorities(role_id, authority) values
    ('role-01', 'USER'),
    ('role-09', 'ADMIN'), ('role-09', 'USER')
;

insert into company(id, name, created_date, updated_date) values
    ('comp-01', 'comp01', now(), now()),
    ('comp-02', 'comp02', now(), now())
;

insert into tb_user(id, name, username, password, company_id, role_id, created_date, updated_date, created_by, updated_by) values
    ('user-01', 'kim', 'kim@gmail.com', '{noop}pass1234', 'comp-01', 'role-09', now(), now(), null, null),
    ('user-02', 'testname1', 'testname1@gmail.com', '{noop}pass1234', 'comp-01', 'role-09', now(), now(), null, null),
    ('user-03', 'testname2', 'testname2@gmail.com', '{noop}pass1234', 'comp-01', 'role-01', now(), now(), null, null),
    ('user-04', 'testname3', 'testname3@gmail.com', '{noop}pass1234', 'comp-02', 'role-09', now(), now(), null, null),
    ('user-05', 'testname4', 'testname4@gmail.com', '{noop}pass1234', 'comp-02', 'role-09', now(), now(), null, null),
    ('user-06', 'testname5', 'testname5@gmail.com', '{noop}pass1234', 'comp-02', 'role-01', now(), now(), null, null)
;