#!/bin/bash
set -o allexport
source infraestructure/.env
set +o allexport

docker build --build-arg EXTERNAL_PORT=${EXTERNAL_PORT} \
  -t tcero76/frontend-service:${VERSION} \
  -f infraestructure/Dockerfile/Dockerfile.frontend .
echo "DONE: Build del servicio: frontend-service"

for servicio in "${SERVICIOS[@]}"; do
docker build --build-arg JAR_PATH=$servicio \
  -t tcero76/$servicio:${VERSION} \
  -f infraestructure/Dockerfile/Dockerfile.service .
echo "DONE: Build del servicio: $servicio"
done

echo "Build terminado"
