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

insert into tb_member(id, name)
values
    ('member-01', 'm1'),
    ('member-02', 'm2'),
    ('member-03', 'm3'),
    ('member-04', 'm4'),
    ('member-05', 'm5'),
    ('member-06', 'm6'),
    ('member-07', 'm7')
;

insert into tb_group(id, name)
values
    ('group-01', 'g1'),
    ('group-02', 'g2'),
    ('group-03', 'g3')
;

-- insert into tb_group_member(id, member_id, group_id, sb_application_id, sb_api_token)
-- values
--     ('group-member-1', 'member-01', 'group-01', 'sb-appId-1111', 'sb-api-token1-1'),
--     ('group-member-2', 'member-01', 'group-02', 'sb-appId-2222', 'sb-api-token2-1'),
--     ('group-member-3', 'member-01', 'group-03', 'sb-appId-3333', 'sb-api-token3-1')
-- ;