insert into tb_role(id, name)
values
    ('role-01', '관리자'),
    ('role-02', '사용자')
;

insert into tb_join_role_authorities(role_id, authority)
values
    ('role-01', 'ADMIN'), ('role-01', 'USER'),
    ('role-02', 'USER')
;

insert into tb_company(id, name) values('comp-01', 'test-comp');

insert into tb_user(id, username, company_id, role_id)
values
    ('admin-01', 'test-admin', 'comp-01', 'role-01'),
    ('user-01', 'test-user', 'comp-01', 'role-02')
;

insert into tb_email_authentication(id, user_id) values('auth-01', 'user-01');