create database auth;

create table role
(
    ROLE_ID   bigint auto_increment
        primary key,
    ROLE_NAME varchar(255) not null,
    constraint ROLE_NAME
        unique (ROLE_NAME)
);

create table user
(
    ID        bigint auto_increment
        primary key,
    NAME      varchar(255) not null,
    USER_NAME varchar(255) not null,
    PASSWORD  varchar(255) null,
    ROLE_ID   bigint       not null,
    EMAIL     varchar(255) null,
    constraint USER_FK1
        foreign key (ROLE_ID) references role (ROLE_ID)
);







