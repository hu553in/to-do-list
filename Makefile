include .env
export

###

all: up_dev_env build run

.PHONY: build
build:
	./gradlew clean build

run:
	./dev-env/wait-for-it.sh -s -t 30 ${POSTGRES_HOST}:${POSTGRES_PORT}
	java -jar ./build/libs/to-do-list-*.jar

lint:
	./gradlew checkstyleMain checkstyleTest

up_dev_env:
	docker-compose -f ./dev-env/docker-compose.yml up -d

down_dev_env:
	docker-compose -f ./dev-env/docker-compose.yml down

down_dev_env_rm_images:
	docker-compose -f ./dev-env/docker-compose.yml down --rmi all

connect_to_psql_dev_env:
	docker-compose exec to-do-list-postgresql psql ${POSTGRES_USER} -U ${POSTGRES_PASSWORD}

generate_api_docs: up_dev_env
	./gradlew clean generateOpenApiDocs

generate_javadoc:
	./gradlew javadoc
