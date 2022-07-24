insert into tb_role(id, name) values
    ('role-01', 'user'),
    ('role-09', 'admin')
;

insert into tb_role_authorities(role_id, authority) values
    ('role-01', 'USER'),
    ('role-09', 'ADMIN'), ('role-09', 'USER')
;

insert into tb_company(id, name) values
    ('comp-01', 'comp01'),
    ('comp-02', 'comp02')
;

insert into tb_user(id, name, username, password, company_id, role_id) values
    ('user-01', 'kim', 'kim@gmail.com', 'pass1234', 'comp-01', 'role-09'),
    ('user-02', 'testname1', 'testname1@gmail.com', 'pass1234', 'comp-01', 'role-09'),
    ('user-03', 'testname2', 'testname2@gmail.com', 'pass1234', 'comp-01', 'role-01'),
    ('user-04', 'testname3', 'testname3@gmail.com', 'pass1234', 'comp-02', 'role-09'),
    ('user-05', 'testname4', 'testname4@gmail.com', 'pass1234', 'comp-02', 'role-09'),
    ('user-06', 'testname5', 'testname5@gmail.com', 'pass1234', 'comp-02', 'role-01')
;