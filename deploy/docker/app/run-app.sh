#!/bin/sh

until pg_isready -h ${POSTGRES_HOST} -U ${POSTGRES_USER}; do
  echo 'Retrying in 10 sec...'
  sleep 10
done

java -jar /opt/app.jar
