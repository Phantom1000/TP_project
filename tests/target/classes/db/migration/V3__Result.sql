create table result (
    id int8 not null,
    rating real not null,
    user_id int8,
    test_id int8,
    primary key (id)
);

create table answer_result (
    id int8 default nextval('hibernate_sequence'),
    answer_id int8 not null,
    result_id int8 not null,
    primary key(id)
);

alter table if exists result
    add constraint result_user_fk
    foreign key (user_id) references usr;

alter table if exists result
    add constraint result_test_fk
    foreign key (test_id) references test;

alter table if exists answer_result
    add constraint answer_result_answer_fk
    foreign key (answer_id) references answer;

alter table if exists answer_result
    add constraint answer_result_result_fk
    foreign key (result_id) references result;