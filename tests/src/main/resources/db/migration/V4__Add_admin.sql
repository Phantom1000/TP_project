create extension if not exists pgcrypto;

insert into usr values (1, 'aaa', 'a', 'a', 'a', crypt('123', gen_salt('bf', 8)));
insert into user_role values (1, 'USER');
insert into user_role values (1, 'ADMIN');