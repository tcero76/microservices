plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "microservices"

include("frontend")
include("frontend:frontend-service")
include("gateway-service")
include("resource-service")
include("postgres:postgres-config")
include("postgres:postgres-model")
include("postgres:postgres-services")
include("authorization-server-service")
include("discovery-service")
include("app-config-data")
include("config-server")
