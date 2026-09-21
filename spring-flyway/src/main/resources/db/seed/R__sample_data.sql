-- Repeatable migration: re-runs whenever this file's checksum changes, so it must be idempotent.
delete from tb_user;

insert into tb_user (id, name, age) values
  ('11111111-1111-1111-1111-111111111111', 'alice', 20),
  ('22222222-2222-2222-2222-222222222222', 'bob', 30),
  ('33333333-3333-3333-3333-333333333333', 'carol', 40);
