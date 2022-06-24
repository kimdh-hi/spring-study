insert into tb_company(id, name) values('comp-01', 'test-comp');

insert into tb_user(id, username, company_id) values('user-01', 'test-user', 'comp-01');

insert into tb_email_authentication(id, user_id) values('auth-01', 'user-01');