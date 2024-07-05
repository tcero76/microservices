package cl.microservices.config.data

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties("postgres-config")
data class PostGresConfigData(
    var url: String?,
    var username: String?,
    var password: String?,
    var databasePlatform: String?,
    var schema: String?,
    var showSql: String?,
    var stringtype: String?
)