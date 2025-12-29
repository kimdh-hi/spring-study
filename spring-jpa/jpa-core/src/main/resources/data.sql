insert into user(id, name, created_date) values
('u1', 'u1', now()),
('u2', 'u2', now());

insert into friend(id, from_user_id, to_user_id) values
('f1', 'u1', 'u2');
-- ('f2', 'u2', 'u1');

insert into device(id, device_key, user_id) values
('d1', 'key1', 'u1'),
('d2', 'key2', 'u2');
