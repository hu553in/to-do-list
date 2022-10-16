#!/bin/bash

pushd ../..
./gradlew bootBuildImage
popd

docker tag to-do-list:1.0.0 hu553in/to-do-list
docker push hu553in/to-do-list

minikube start

minikube kubectl -- apply -f ./postgresql
minikube kubectl -- apply -f ./app

PROMETHEUS_CONFIG_PATH=../../prometheus/prometheus.yml
GRAFANA_CONFIG_DATASOURCES_PATH=../../grafana/provisioning/datasources/datasources.yaml

minikube kubectl -- create configmap prometheus-config --from-file $PROMETHEUS_CONFIG_PATH
minikube kubectl -- create configmap grafana-config-datasources --from-file $GRAFANA_CONFIG_DATASOURCES_PATH

minikube kubectl -- apply -f ./prometheus
minikube kubectl -- apply -f ./grafana

minikube addons enable ingress

echo "Waiting 1 min before applying configs related to Ingress..."
sleep 60

minikube kubectl -- apply -f ./ingress
