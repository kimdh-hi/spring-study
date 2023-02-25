create table tb_user (
    id varchar(255) not null,
    name varchar(255) not null,
    primary key (id)
) engine=InnoDB;

alter table tb_user
    add constraint UK_jus3xa0l3009mkpbt9i5ilxx3 unique (name);