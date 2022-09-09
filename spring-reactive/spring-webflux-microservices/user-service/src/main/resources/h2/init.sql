create table tb_users(
    id bigint auto_increment,
    name varchar(50),
    balance int,
    primary key(id)
);

create table tb_user_transaction(
    id bigint auto_increment,
    user_id bigint,
    amount int,
    created_date timestamp,
    foreign key (user_id) references tb_users(id) on delete cascade
);

insert into tb_users (name, balance)
values
    ('kim', 20000),
    ('lee', 15000),
    ('park', 5000),
    ('jeon', 50000)
;