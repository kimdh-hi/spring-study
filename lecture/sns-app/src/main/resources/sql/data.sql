insert into tb_user
    (id, username, password, role, register_at, updated_at, deleted_at)
values
    ('user-01', 'test-username', '{noop}test1234', 'USER', now(), now(), null),
    ('user-02', 'test2-username', '{noop}test1234', 'USER', now(), now(), null),
    ('user-03', 'test3-username', '{noop}test1234', 'USER', now(), now(), null)
;

insert into tb_post
    (id, title, body, user_id, register_at, updated_at, deleted_at)
values
    ('post-01', 'test-title', 'test-body', 'user-01', now(), null, null),
    ('post-02', 'test2-title', 'test2-body', 'user-02', now(), null, null)
;


insert into tb_like
    (id, user_id, post_id, register_at, updated_at, deleted_at)
values
    ('like-01', 'user-01', 'post-02', now(), null, null),
    ('like-02', 'user-03', 'post-02', now(), null, null)
;


insert into tb_comment
    (id, user_id, post_id, comment, register_at, updated_at, deleted_at)
values
    ('comment-01', 'user-01', 'post-01', 'test-comment-01', now(), null, null),
    ('comment-02', 'user-02', 'post-01', 'test-comment-02', now(), null, null),
    ('comment-03', 'user-02', 'post-01', 'test-comment-03', now(), null, null),
    ('comment-04', 'user-01', 'post-01', 'test-comment-04', now(), null, null),
    ('comment-05', 'user-01', 'post-01', 'test-comment-05', now(), null, null),
    ('comment-06', 'user-01', 'post-01', 'test-comment-06', now(), null, null),
    ('comment-07', 'user-03', 'post-01', 'test-comment-07', now(), null, null)
;