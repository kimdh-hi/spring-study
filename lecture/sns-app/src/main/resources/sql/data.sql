insert into tb_user
    (id, username, password, role, register_at, updated_at, deleted_at)
values
    ('user-01', 'test-username', '{noop}test1234', 'USER', now(), now(), null)
;

insert into tb_post
    (id, title, body, user_id, register_at, updated_at, deleted_at)
values
    ('post-01', 'test-title', 'test-body', 'user-01', now(), now(), now())
;