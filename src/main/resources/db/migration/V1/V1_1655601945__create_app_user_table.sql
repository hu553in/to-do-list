create table app_user
(
    id       serial primary key,
    email    varchar(320) not null unique, -- 64 + 1 for delimiter + 255
    password varchar(255) not null,
    admin    boolean default false
);
