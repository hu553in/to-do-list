create table task
(
    id         serial primary key,
    text       varchar(255) not null,
    status     varchar(255) default 'TO_DO',
    owner_id   integer references app_user (id) on delete cascade,
    created_at timestamp    not null,
    updated_at timestamp    not null
);
