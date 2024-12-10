#!/bin/bash
# check-resource-server-started.sh

apt-get update -y

yes | apt-get install curl

curlResult=$(curl -GET -s -o /dev/null -w "%{http_code}" http://resource-server:8081/actuator/health)

echo "result status code:" "$curlResult"

while [[ ! $curlResult == "200" ]]; do
  >&2 echo "Resource server is not up yet!"
  sleep 2
  curlResult=$(curl -GET -s -o /dev/null -w "%{http_code}" http://resource-server:8081/actuator/health)
done

npm run build
/cnb/process/web