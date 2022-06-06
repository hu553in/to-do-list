create table app_user
(
    id       serial primary key,
    username varchar(255) not null unique,
    password varchar(255) not null,
    is_admin boolean default false
);
