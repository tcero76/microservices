#!/bin/bash
# check-auth-server-started.sh

apt-get update -y

yes | apt-get install curl

curlResult=$(curl -GET -s -o /dev/null -w "%{http_code}" http://auth-server:8000/actuator/health)

echo "result status code:" "$curlResult"

while [[ ! $curlResult == "200" ]]; do
  >&2 echo "Auth server is not up yet!"
  sleep 2
  curlResult=$(curl -GET -s -o /dev/null -w "%{http_code}" http://auth-server:8000/actuator/health)
done

/cnb/process/web