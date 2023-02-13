insert into tb_user(id, name, age, created_date) values
    ('u1', 'name1', 20, now()),
    ('u2', 'name2', 21, now())
;

insert into tb_user_some_data1(id, data, user_id) values
    ('usd1-1', 'data1', 'u1'),
    ('usd1-2', 'data2', 'u2')
;

insert into tb_user_some_data2(id, data, user_id) values
    ('usd2-1', 'data1', 'u1'),
    ('usd2-2', 'data2', 'u2')
;

insert into tb_user_some_data3(id, data, user_id) values
    ('usd3-1', 'data1', 'u1'),
    ('usd3-2', 'data2', 'u2')
;