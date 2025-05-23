![Topologia del proyecto](./img/demo.gif)

# Resumen
Template para microservicios. Tiene un loggin OAuth2 y landing page, y está listo para crear nuevos microservicios.
Está programado en Kotlin y React-typescript. Adicionalmente, está listo para registrar logs en elasticsearch y monitorear variables con Prometheus.
# ¿Porqué una organización le debería llamar la atención este demo?
 Porque muchas organizaciones necesitan distribuir sus servicios, pero se enfrentan a duras barreras para dar el paso ya que esto involucra conocer manejo de dependencias (gradle o maven), prácticas SOLID, patrones de microservicios, seguridad (OAuth2), criptografía, etc.\
 Consideré valioso este demo porque es un pie inicial para implementar otros patrones más simples, pero requeridos en la industria como CQRS, Outbox, Saga, etc.
# Características
- Patrón BFF para ocultar Refresh Token  y Access Token generados en el proceso de autenticación.
- Testing con pruebas unitarias e integradas [resource-service](https://github.com/tcero76/microservices/tree/master/resource-service/src/test/kotlin/cl/microservices/resource/service) y [postgres](https://github.com/tcero76/microservices/tree/master/postgres/postgres-services/src/test/kotlin/cl/microservices/postgres/services)
- Bastión de seguridad OAuth2. [auth-server](https://github.com/tcero76/microservices/tree/master/authorization-server-service)
- Programado en Kotlin y manejado con Gradle.
- Código agrupado en módulos. Modelos relacionales se comparten entre los módulos que lo requieren.
- Monitoreo Prometheus
- Loggins acumulados en Elasticstack

![Topologia del proyecto](./img/topologia.svg)

# Arranque

Debido a la configuración de seguridad que requiere oauth2-client se debe agregar la siguiente registro en el archivo `/etc/hosts`:
```
127.0.1.1   auth-server
```

## Perfil Dev

### Instrucciones

Este perfil se configura con las siguientes variables de entorno creando el archivo siguiente ```./infraestructura/.env```:
```
#Variables
GLOBAL_NETWORK=application
COMPOSE_PATH_SEPARATOR=:
VERSION=1.1.1
ELASTIC_VERSION=7.9.1
POSTGRES_VERSION=16.6
EXTERNAL_PORT=80
EXTERNAL_HOST=localhost
ENCRYPT_KEY=Demo_Pwd!2020
PUBLICKEY="-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs5tltmTigSt03X6EjWAwZFnNzj0lfuQ1BcBTqCF1O5dMHnDmS+9+fY467ZEqgTIVzG55oGNxMz0rsDji1WHoPtQ4mGZ7TUssB7eSrtVB1J/ykW9IiuexDEn26MWBhwJaDvYWhhTjZwLIqTcalcvPvVBMAGKkGXODgcujzHco3sYwASNg9sSu0kEGOCMX9QRChd5BUvuZURdnI836z/tcOtWkUMkZYyutz1xnOj+2GQEs9E4NDm4eYemqL+mRszZoLhyTZ3zY02ImaErCgqqGibb5jgNQgUIHAbzpUp0J6jN69k0yKlPjw8WMQ7qTI3GkQYU11YmEuG6EcpO8IJOKJwIDAQAB-----END PUBLIC KEY-----"
PRIVATEKEY="-----BEGIN PRIVATE KEY-----MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCzm2W2ZOKBK3TdfoSNYDBkWc3OPSV+5DUFwFOoIXU7l0wecOZL7359jjrtkSqBMhXMbnmgY3EzPSuwOOLVYeg+1DiYZntNSywHt5Ku1UHUn/KRb0iK57EMSfboxYGHAloO9haGFONnAsipNxqVy8+9UEwAYqQZc4OBy6PMdyjexjABI2D2xK7SQQY4Ixf1BEKF3kFS+5lRF2cjzfrP+1w61aRQyRljK63PXGc6P7YZASz0Tg0Obh5h6aov6ZGzNmguHJNnfNjTYiZoSsKCqoaJtvmOA1CBQgcBvOlSnQnqM3r2TTIqU+PDxYxDupMjcaRBhTXViYS4boRyk7wgk4onAgMBAAECggEADJoYWTjbj6vzPOCgqk7CdngP8armqPBC1s7Mz9zbyJJFZqljRJa0QwOqF3slBqrzTpm9x5wn7YFwQaLBf66pSKdD+qV40fk2nf//txatfMllkADFOpcmcVygihU5kQsq3dE0eV9D1A7PnNffR1K6lmeTkuoDkOmYTjtx7b8P9g7G+jNduxkuqD2eGFPlBGNjZVla2M1uQaMljIOZo/3awFzujKONS+XSehJGtmjVB9gDA+rW7HM1Jsh/CNVb9O1zUn8iZwpreaH0dP1QcdZ00o8g7Jter09LX8GansfZFQ2r0c+cKcwHG//OzN/5oRR11hix67wlay/OsYEsWEQXyQKBgQDokJqBngIKVlBB3B9lA9F61LVugdDzJAiauejCeLXcvHblqLwuU9g3pj1qaZsyrPL3/jgKP0CPNeIKCUDKhSGmlQDAMmyub0ssyQnphV/7bp59rStofQ5MhQ83V9bO769Vjw0lyIN59p3Adr/H+rLqUE+Mbh3NFHi2mta3WnlVlQKBgQDFtKiSh7crR8wdrfI3nLiJHMT5OOMk3SoDDA+Fv4lp/fOeEy1N9u9XgnhozLvaSftN/8yzCINfXlcPxjZ+kTq6vBnmkk4/p6brk8KrfySmQSjhXXo2Sn+zovH3Nu4Zc2UDhxbjrnbcOaDbhJl8xPmmoUpL8XwFUmy6FIo8PHS5ywKBgFPUI81K7kNZn25N8QcRXEXhPFDL/ExYFb3n+012A1/Akz7vFn3AfTR5+LNNw8J9cssPWJXg/N1iN8sUUMVvPZT1KqMcye4Kcdd/09HX4KQZ2IlGjVoJkcjlQXnacLSA0vuNJ9kRsMqaqXxBkZl89Vbegf4FhzpwAZ8YqwLGWuwNAoGAZkscMXzA/EbQyMZ4neeb322gSObzN96q3xq9aVF6EvpPs4lulnleyyN5k5fi1oYS3f+tdTDBcHXKjCHdjk0sWvP6Xr9ydyMSja9NORi26MKsGTcZhb+0JUj48xVbypVXPaUG5dUeMMIshYgtNk5gcaPgYjzXE5XbJch84b/SEVMCgYAPR2PGayCLeWyKmkwH8MV3Oy73feIUVkvBnrW2q655pctewJIsZSS7X0NO2IIM38QOFznM8b/qVY4brhHPzDkp40QEGj3iyy7i0dvrcOmxM4wa2Ga/1b7u6eIHlrtKrQj8U5O706Hsa8dhd1WWp9T+z3K4GzoB8SqFU3+OIOrR1g==-----END PRIVATE KEY-----"
SERVICIOS=("gateway-service" "authorization-server-service" "resource-service" "discovery-service" "config-server")
DOCKER_HOST_SUBNET=175.0.0.0/8
HOST_DOCKER_INTERNAL=175.0.0.1

# Variables versión Desarrollo
COMPOSE_FILE=common.yml:postgres.yml:services.yml:monitoring.yml:elastic_cluster.yml:develop.yml
#Profiles docker-compose: front,monitoring,prod,db,elastic,security
COMPOSE_PROFILES=db,front,dev
SPRING_PROFILES_ACTIVE=dev
```

## Perfil Prod
Perfil de producción que despliega los servicios completos.


### Instrucciones
Este perfil se configura con las siguientes variables de entorno creando el archivo siguiente ```./infraestructura/.env```:
```
#Variables
GLOBAL_NETWORK=application
COMPOSE_PATH_SEPARATOR=:
VERSION=1.1.1
ELASTIC_VERSION=7.9.1
POSTGRES_VERSION=16.6
EXTERNAL_PORT=80
EXTERNAL_HOST=localhost
ENCRYPT_KEY=Demo_Pwd!2020
PUBLICKEY="-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs5tltmTigSt03X6EjWAwZFnNzj0lfuQ1BcBTqCF1O5dMHnDmS+9+fY467ZEqgTIVzG55oGNxMz0rsDji1WHoPtQ4mGZ7TUssB7eSrtVB1J/ykW9IiuexDEn26MWBhwJaDvYWhhTjZwLIqTcalcvPvVBMAGKkGXODgcujzHco3sYwASNg9sSu0kEGOCMX9QRChd5BUvuZURdnI836z/tcOtWkUMkZYyutz1xnOj+2GQEs9E4NDm4eYemqL+mRszZoLhyTZ3zY02ImaErCgqqGibb5jgNQgUIHAbzpUp0J6jN69k0yKlPjw8WMQ7qTI3GkQYU11YmEuG6EcpO8IJOKJwIDAQAB-----END PUBLIC KEY-----"
PRIVATEKEY="-----BEGIN PRIVATE KEY-----MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCzm2W2ZOKBK3TdfoSNYDBkWc3OPSV+5DUFwFOoIXU7l0wecOZL7359jjrtkSqBMhXMbnmgY3EzPSuwOOLVYeg+1DiYZntNSywHt5Ku1UHUn/KRb0iK57EMSfboxYGHAloO9haGFONnAsipNxqVy8+9UEwAYqQZc4OBy6PMdyjexjABI2D2xK7SQQY4Ixf1BEKF3kFS+5lRF2cjzfrP+1w61aRQyRljK63PXGc6P7YZASz0Tg0Obh5h6aov6ZGzNmguHJNnfNjTYiZoSsKCqoaJtvmOA1CBQgcBvOlSnQnqM3r2TTIqU+PDxYxDupMjcaRBhTXViYS4boRyk7wgk4onAgMBAAECggEADJoYWTjbj6vzPOCgqk7CdngP8armqPBC1s7Mz9zbyJJFZqljRJa0QwOqF3slBqrzTpm9x5wn7YFwQaLBf66pSKdD+qV40fk2nf//txatfMllkADFOpcmcVygihU5kQsq3dE0eV9D1A7PnNffR1K6lmeTkuoDkOmYTjtx7b8P9g7G+jNduxkuqD2eGFPlBGNjZVla2M1uQaMljIOZo/3awFzujKONS+XSehJGtmjVB9gDA+rW7HM1Jsh/CNVb9O1zUn8iZwpreaH0dP1QcdZ00o8g7Jter09LX8GansfZFQ2r0c+cKcwHG//OzN/5oRR11hix67wlay/OsYEsWEQXyQKBgQDokJqBngIKVlBB3B9lA9F61LVugdDzJAiauejCeLXcvHblqLwuU9g3pj1qaZsyrPL3/jgKP0CPNeIKCUDKhSGmlQDAMmyub0ssyQnphV/7bp59rStofQ5MhQ83V9bO769Vjw0lyIN59p3Adr/H+rLqUE+Mbh3NFHi2mta3WnlVlQKBgQDFtKiSh7crR8wdrfI3nLiJHMT5OOMk3SoDDA+Fv4lp/fOeEy1N9u9XgnhozLvaSftN/8yzCINfXlcPxjZ+kTq6vBnmkk4/p6brk8KrfySmQSjhXXo2Sn+zovH3Nu4Zc2UDhxbjrnbcOaDbhJl8xPmmoUpL8XwFUmy6FIo8PHS5ywKBgFPUI81K7kNZn25N8QcRXEXhPFDL/ExYFb3n+012A1/Akz7vFn3AfTR5+LNNw8J9cssPWJXg/N1iN8sUUMVvPZT1KqMcye4Kcdd/09HX4KQZ2IlGjVoJkcjlQXnacLSA0vuNJ9kRsMqaqXxBkZl89Vbegf4FhzpwAZ8YqwLGWuwNAoGAZkscMXzA/EbQyMZ4neeb322gSObzN96q3xq9aVF6EvpPs4lulnleyyN5k5fi1oYS3f+tdTDBcHXKjCHdjk0sWvP6Xr9ydyMSja9NORi26MKsGTcZhb+0JUj48xVbypVXPaUG5dUeMMIshYgtNk5gcaPgYjzXE5XbJch84b/SEVMCgYAPR2PGayCLeWyKmkwH8MV3Oy73feIUVkvBnrW2q655pctewJIsZSS7X0NO2IIM38QOFznM8b/qVY4brhHPzDkp40QEGj3iyy7i0dvrcOmxM4wa2Ga/1b7u6eIHlrtKrQj8U5O706Hsa8dhd1WWp9T+z3K4GzoB8SqFU3+OIOrR1g==-----END PRIVATE KEY-----"
SERVICIOS=("gateway-service" "authorization-server-service" "resource-service" "discovery-service" "config-server")
DOCKER_HOST_SUBNET=175.0.0.0/8
HOST_DOCKER_INTERNAL=175.0.0.1

## Variables versión Producción
#COMPOSE_FILE=common.yml:postgres.yml:services.yml:monitoring.yml:elastic_cluster.yml:prod.yml
##Profiles docker-compose: front,monitoring,prod,db,elastic,security,base
#COMPOSE_PROFILES=db,base,front,prod
#SPRING_PROFILES_ACTIVE=prod
```
