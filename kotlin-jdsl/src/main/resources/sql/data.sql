insert into tb_company(id, name) values
    ('comp-01', 'comp01')
;

insert into tb_user(id, name, username, password, company_id) values
    ('user-01', 'kim', 'kim@gmail.com', 'pass1234', 'comp-01'),
    ('user-02', 'testname1', 'testname1@gmail.com', 'pass1234', 'comp-01'),
    ('user-03', 'testname2', 'testname2@gmail.com', 'pass1234', 'comp-01'),
    ('user-04', 'testname3', 'testname3@gmail.com', 'pass1234', 'comp-01'),
    ('user-05', 'testname4', 'testname4@gmail.com', 'pass1234', 'comp-01'),
    ('user-06', 'testname5', 'testname5@gmail.com', 'pass1234', 'comp-01')
;