create table Member
(
    id int auto_increment,
    email varchar(20) not null,
    nickname varchar(20) not null,
    birthday date not null,
    createdAt datetime not null,
    constraint member_id_uindex
    primary key (id)
);

create table MemberNicknameHistory
(
    id int auto_increment,
    memberId int not null,
    nickname varchar(20) not null,
    createdAt datetime not null,
    constraint memberNicknameHistory_id_uindex
    primary key (id)
);

create table Follow
(
    id int auto_increment,
    fromMemberId int not null,
    toMemberId int not null,
    createdAt datetime not null,
    constraint Follow_id_uindex
        primary key (id)
);

create unique index Follow_fromMemberId_toMemberId_uindex
    on Follow (fromMemberId, toMemberId);

create table Post(
    id int auto_increment,
    memberId int not null,
    content varchar(255),
    createdAt datetime not null,
    constraint Post_id_uindex
        primary key (id)
);

# 분포도가 매우 낮은 인덱스
create index Post__index_member_id
    on Post (memberId);

# 분포도가 높은 인덱스
create index Post__index_created_at
    on Post (createdAt);

# 복합 인덱스
create index Post_index_member_id_created_at
    on Post (memberId, createdAt);

alter table Post add column likeCount int;

alter table Post add column version int default 0;

create table PostLike(
     id int auto_increment,
     memberId int not null,
     postId int not null,
     createdAt datetime not null,
    constraint PostLike_id_uindex primary key (id)
);