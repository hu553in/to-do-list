# To-do list

[![CI](https://github.com/hu553in/to-do-list/actions/workflows/ci.yml/badge.svg)](https://github.com/hu553in/to-do-list/actions/workflows/ci.yml)
[![Maintainability](https://api.codeclimate.com/v1/badges/f48e2fa500475ffcaef6/maintainability)](https://codeclimate.com/github/hu553in/to-do-list/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/f48e2fa500475ffcaef6/test_coverage)](https://codeclimate.com/github/hu553in/to-do-list/test_coverage)

## Table of contents

* [Description](#description)
* [Tech stack](#tech-stack)
* [How to configure](#how-to-configure)
* [How to change the default user set](#how-to-change-the-default-user-set)
* [How to run](#how-to-run)
* [Deploy to Kubernetes](#deploy-to-kubernetes)
   * [How to use](#how-to-use)
* [Grafana](#grafana)
    * [Data sources](#data-sources)
    * [Dashboards](#dashboards)

## Description

This project is the backend part of an app for managing tasks.

## Tech stack

* Java 17
* Spring Boot 2.7.4
* PostgreSQL 14
* Prometheus 2.39.1
* Grafana 9.1.8

## How to configure

You can perform a base app configuration using the following [.env file](./.env).\
At least, you should change a Sentry DSN.

**Note:** this .env file works only for app running in Docker.\
If you want to configure the app for deploying to Kubernetes,
go to [Deploy to Kubernetes](#deploy-to-kubernetes) section.

## How to change the default user set

You can change the default user set in the following
[Flyway migration](./src/main/resources/db/migration/repeatable/R__create_users.sql).\
Note that the `password` column of the `app_user` table stores password hashes generated using
[Bcrypt](https://en.wikipedia.org/wiki/Bcrypt) (10 rounds).\
[Here](https://www.browserling.com/tools/bcrypt) you can generate hashes for your custom user set.

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

When the server is started, you can open:

* [Swagger UI](http://localhost:8080/swagger-ui.html)
* [Prometheus UI](http://localhost:9090)
* [Grafana UI](http://localhost:3000)

## Deploy to Kubernetes

You can find a simple Kubernetes deployment example (using minikube) in `./deploy/minikube` directory.

### How to use

1. Do steps 1-3 from [How to run](#how-to-run) section
2. Log in into Docker
3. Install minikube (≥ 1.27.0)
4. Run `minikube start`
5. Review all configs and scripts in `./deploy/minikube` directory and change what is required, at least:
    * Sentry DSN
    * Spring Boot application Docker image tag
6. Run `pushd ./deploy/minikube && ./minikube-deploy.sh && popd`
7. Wait until all pods in `minikube kubectl -- get pods` command output have `Running` status and all replicas ready
8. Run `minikube ip` to get an exposed IP address of minikube VM
9. Run `minikube kubectl -- get service` to get exposed ports of services

After doing that, you can open:

* `http://${EXPOSED_IP}:${APP_PORT_8080}/swagger-ui.html` – Swagger UI
* `http://${EXPOSED_IP}:${PROMETHEUS_PORT}` – Prometheus UI
* `http://${EXPOSED_IP}:${GRAFANA_PORT}` – Grafana UI

## Grafana

### Data sources

There is a single provisioned data source called `Prometheus`.

### Dashboards

Unfortunately, there are no provisioned dashboards at the moment.\
You can try to create them yourself using data received from provisioned `Prometheus` data source.
