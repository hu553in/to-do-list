#!/bin/bash

# deploy PostgreSQL

pushd ./postgresql
minikube kubectl -- apply -f ./postgresql-config.yaml
minikube kubectl -- apply -f ./postgresql-secret.yaml
minikube kubectl -- apply -f ./postgresql-pv.yaml
minikube kubectl -- apply -f ./postgresql-pvc.yaml
minikube kubectl -- apply -f ./postgresql-deployment.yaml
minikube kubectl -- apply -f ./postgresql-service.yaml
popd

# build, tag and push application Docker image

pushd ../..
./gradlew bootBuildImage
popd

docker tag to-do-list:1.0.0 hu553in/to-do-list
docker push hu553in/to-do-list

# deploy application

pushd ./app
minikube kubectl -- apply -f ./app-config.yaml
minikube kubectl -- apply -f ./app-secret.yaml
minikube kubectl -- apply -f ./app-deployment.yaml
minikube kubectl -- apply -f ./app-service.yaml
popd

# deploy Ingress

pushd ./ingress
minikube kubectl -- apply -f ./ingress.yaml
popd
