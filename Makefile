include .env
export

###

all: build run

.PHONY: build
build:
	./gradlew clean build
	mv ./build/libs/to-do-list-*.jar ./build/libs/app.jar

run:
	docker-compose -f ./deploy/docker/docker-compose.yml up -d

run_with_docker_image_rebuild:
	docker-compose -f ./deploy/docker/docker-compose.yml up -d --build

check:
	./gradlew check

test:
	./gradlew test

stop:
	docker-compose -f ./deploy/docker/docker-compose.yml down

stop_rm_images:
	docker-compose -f ./deploy/docker/docker-compose.yml down --rmi all

generate_api_docs: build run
	./gradlew clean generateOpenApiDocs

generate_javadoc:
	./gradlew javadoc

###

CONTAINER?=app
COMMAND?=bash

run_command_in_docker_container:
	docker-compose -f ./deploy/docker/docker-compose.yml exec $(CONTAINER) $(COMMAND)

logs_of_docker_container:
	docker-compose -f ./deploy/docker/docker-compose.yml logs $(CONTAINER)
