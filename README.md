# To-do list

[![CI](https://github.com/hu553in/to-do-list/actions/workflows/ci.yml/badge.svg)](https://github.com/hu553in/to-do-list/actions/workflows/ci.yml)
[![Maintainability](https://api.codeclimate.com/v1/badges/f48e2fa500475ffcaef6/maintainability)](https://codeclimate.com/github/hu553in/to-do-list/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/f48e2fa500475ffcaef6/test_coverage)](https://codeclimate.com/github/hu553in/to-do-list/test_coverage)

## Table of contents

* [Description](#description)
* [Tech stack](#tech-stack)
* [How to configure](#how-to-configure)
* [How to run](#how-to-run)

## Description

This project is the backend part of an app for managing tasks.

## Tech stack

* Java 17
* Spring Boot
* PostgreSQL

## How to configure

You can perform a base app configuration using the following [.env file](./.env).

Also, you can change the default user set in the following
[Flyway migration](./src/main/resources/db/migration/repeatable/R__create_users.sql).\
Note that the `password` column of the `app_user` table stores password hashes generated using
[Bcrypt](https://en.wikipedia.org/wiki/Bcrypt) (10 rounds).\
[Here](https://www.browserling.com/tools/bcrypt) you can generate hashes for your custom user set.

**Note:** be careful when changing default configuration values.\
Remember that you will need to use your custom values instead of used in instructions below.

## How to run

1. Install Docker, Docker Compose, OpenJDK (≥ 17), GNU Make
2. Run following commands to get the ability to use Docker as non-root user:

    ```
    groupadd docker
    usermod -aG docker $(id -un)
    systemctl enable docker
    ```

3. Reboot your machine to apply changes performed in the previous step
4. Run `make`

When the server is started, you can open [Swagger UI](http://localhost:8080/swagger-ui.html) to see API docs.
