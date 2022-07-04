-- create table tb_role (
--                          id varchar(50) not null,
--                          name varchar(200) not null,
--                          primary key (id)
-- );

create table tb_user (
                         id varchar(50) identity not null,
                         name varchar(200) not null,
                         password varchar(255),
                         username varchar(100) not null,
--                          role_id varchar(50) not null,

                         created_date datetime(6),
                         created_by varchar(50),
                         updated_by varchar(50),
                         updated_date datetime(6),

                         primary key (id),
);

insert into tb_user(id, name, password, username, created_date, created_by, updated_date, updated_by) values
    ('user-01', 'user', 'abcd1234', 'user@gmail.com', now(), null, now(), null);

-- create table tb_notice (
--                            id varchar(50) not null,
--                            title varchar(255) not null,
--                            content varchar(500) not null,
--                            user_id varchar(50) not null,
--
--                            created_date datetime(6),
--                            created_by varchar(50),
--                            updated_by varchar(50),
--                            updated_date datetime(6),
--
--                            primary key (id),
--                            FOREIGN KEY (user_id) references tb_user(id)
-- );