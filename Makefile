include .env
export

###

all: up_dev_env build run

.PHONY: build
build:
	./gradlew clean build

.PHONY: run
run:
	./dev-env/wait-for-it.sh -s -t 30 ${POSTGRES_HOST}:${POSTGRES_PORT}
	java -jar ./build/libs/to-do-list-*.jar

.PHONY: up_dev_env
up_dev_env:
	docker-compose -f ./dev-env/docker-compose.yml up -d --build

.PHONY: down_dev_env
down_dev_env:
	docker-compose -f ./dev-env/docker-compose.yml down

.PHONY: down_dev_env_rm_images
down_dev_env_rm_images:
	docker-compose -f ./dev-env/docker-compose.yml down --rmi all

.PHONY: connect_to_psql_dev_env
connect_to_psql_dev_env:
	docker-compose exec to-do-list-postgresql psql ${POSTGRES_USER} -U ${POSTGRES_PASSWORD}
