DROP TABLE IF EXISTS users;

CREATE TABLE users(
    id bigint not null auto_increment,
    username varchar(50),
    name varchar(50),
    password varchar(255),
    profile_url varchar(255),
    created_at timestamp default now(),
    updated_at timestamp default now(),
    primary key (id)
);