create table message (
    id int8 not null,
    text varchar(1024) not null,
    view boolean not null,
    user_id int8,
    primary key (id)
);

alter table if exists message
    add constraint message_user_fk
    foreign key (user_id) references usr;