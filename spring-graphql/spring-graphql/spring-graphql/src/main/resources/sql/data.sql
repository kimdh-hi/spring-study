insert into tb_group(id, name)
values
    ('g1', 'g1-name')
;

insert into tb_user(id, name, username, group_id)
values
    ('u1', 'u1-name', 'u1-username', 'g1'),
    ('u2', 'u2-name', 'u2-username', 'g1')
;