# Resumen

Proyecto inicial de sistema web hecho con microservicios. Sólo tiene una landing page y un logging, pero hecho en forma distribuida. Está hecho en Kotlin y React-typescript

# Características

- Testing con pruebas unitarias e integradas [resource-service](https://github.com/tcero76/microservices/tree/master/resource-service/src/test/kotlin/cl/microservices/resource/service) y [postgres](https://github.com/tcero76/microservices/tree/master/postgres/postgres-services/src/test/kotlin/cl/microservices/postgres/services)
- Código agrupado en módulos. Modelos relacionales se comparten entre los módulos que lo requieren.
- Bastión de seguridad OAuth2. [auth-server](https://github.com/tcero76/microservices/tree/master/authorization-server-service)
- Programado en Kotlin y manejado con Gradle.
- Monitoreo Prometheus
- Loggins acumulados con Elasticstack