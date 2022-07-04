create table tb_user
(
    id       serial constraint tb_user_pk primary key,
    name     varchar not null,
    username varchar not null,
    password varchar not null
);

alter table tb_user
    owner to root;

create unique index tb_user_id_uindex
    on tb_user (id);