create sequence hibernate_sequence start 1 increment 1;

create table answer (
    id int8 not null,
    text varchar(2048) not null,
    correct boolean not null,
    question_id int8,
    primary key (id)
);

create table question (
    id int8 not null,
    text varchar(2048) not null,
    test_id int8,
    primary key (id)
);

create table test (
    id int8 not null,
    position varchar(255) not null,
    primary key (id)
);

create table usr (
    id int8 not null,
    username varchar(255) not null,
    surname varchar(255) not null,
    firstname varchar(255) not null,
    patronymic varchar(255) not null,
    password varchar(255) not null,
    primary key (id)
);

create table user_role (
    user_id int8 not null,
    roles varchar(255)
);

alter table if exists answer
    add constraint answer_question_fk
    foreign key (question_id) references question;

alter table if exists question
    add constraint question_test_fk
    foreign key (test_id) references test;

alter table if exists user_role
    add constraint user_role_user_fk
    foreign key (user_id) references usr;