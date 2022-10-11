include .env
export

###

all: up_dev_env build run

.PHONY: build
build:
	./gradlew clean build

run:
	docker-compose -f ./deploy/docker/docker-compose.yml exec postgresql \
	/bin/bash -c 'until pg_isready; do echo "Retrying after 10 seconds..." && sleep 10; done'
	java -jar ./build/libs/to-do-list-*.jar

check:
	./gradlew check

test:
	./gradlew test

up_dev_env:
	docker-compose -f ./deploy/docker/docker-compose.yml up -d

connect_to_dev_env_postgres:
	docker-compose -f ./deploy/docker/docker-compose.yml exec postgresql bash

down_dev_env:
	docker-compose -f ./deploy/docker/docker-compose.yml down

down_dev_env_rm_images:
	docker-compose -f ./deploy/docker/docker-compose.yml down --rmi all

generate_api_docs: up_dev_env
	./gradlew clean generateOpenApiDocs

generate_javadoc:
	./gradlew javadoc
