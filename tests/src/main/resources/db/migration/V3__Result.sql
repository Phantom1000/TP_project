create table result (
    id int8 not null,
    rating real not null,
    user_id int8,
    primary key (id)
);

create table answer_result (
    answer_id int8 not null,
    result_id int8 not null,
    primary key (answer_id, result_id)
);

alter table if exists result
    add constraint result_user_fk
    foreign key (user_id) references usr;