drop table if exists customer;

create table customer (
    id int primary key auto_increment not null,
    name varchar(50),
    age int,
    city varchar(100)
);

insert into customer(name, age, city) values
    ('kim', 28, 'seoul'),
    ('lee', 35, 'bucheon'),
    ('park', 20, 'busan')
;