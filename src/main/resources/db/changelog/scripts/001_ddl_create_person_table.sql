drop table if exists person;
create table person (
    id serial primary key not null,
    login varchar(2000),
    password varchar(2000)
);

comment on table person is 'Список пользователей';
comment on column person.id is 'идентификатор пользователя';
comment on column person.login is 'Имя пользователя';
comment on column person.password is 'Пароль пользователя';
