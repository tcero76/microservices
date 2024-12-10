#!/bin/bash
set -o allexport
source .env
set +o allexport

docker run -it --rm -v $PWD/../frontend:/app -w /app/frontend/ -e VITE_APP_REDIRECT_URL=http://localhost:${EXTERNAL_PORT} node:22.3.0 npm run build
