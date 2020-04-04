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
    lastname varchar(255) not null,
    patronymic varchar(255) not null,
    password varchar(255) not null,
    primary key (id)
);

alter table if exists answer
    add constraint answer_question_fk
    foreign key (question_id) references question;

alter table if exists question
    add constraint question_test_fk
    foreign key (test_id) references test;
