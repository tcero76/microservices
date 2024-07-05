#!/bin/bash
curl -X POST -d 'grant_type=password&username=app_user&password=Reaktor6_&client_id=frontend-service' http://localhost:9091/auth/realms/master/protocol/openid-connect/token