insert
into app_user (email, password, admin)
values ('admin@to-do-list',
        '$2a$10$2fF0HKjsMzwx2PUdr0WyDupIfCEjluQxov.cx7iTk8tBpARXdCvE6', -- password is the same as email
        true) on conflict do nothing;
