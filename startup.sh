#!/usr/bin/env bash
nohup ./mvnw spring-boot:run \
 -Ddatabase_url="$DATABASE_URL" \
 -Ddatabase_username="$DATABASE_USERNAME" \
 -Ddatabase_password="$DATABASE_PASSWORD" > log.txt 2>&1 &
echo $! > ./pid.file
