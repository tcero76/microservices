package cl.microservices.postgres.config

import cl.microservices.config.data.PostGresConfigData
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import java.util.*
import javax.sql.DataSource

@Configuration
@EnableConfigurationProperties(PostGresConfigData::class)
class Config(val postGresConfigData: PostGresConfigData) {
    private val log = KotlinLogging.logger {}
    @Bean
    fun dataSource(): DataSource {
        val dataSource = SimpleDriverDataSource()
        log.info { "Los valores de configuración de BD son: ${ postGresConfigData }" }
        var properties = Properties();
        properties.setProperty("stringtype", postGresConfigData.stringtype)
        properties.setProperty("show-sql", postGresConfigData.showSql)
        dataSource.setDriver(org.postgresql.Driver())
        dataSource.connectionProperties = properties
        dataSource.url = postGresConfigData.url
        dataSource.username = postGresConfigData.username
        dataSource.password = postGresConfigData.password
        dataSource.schema = postGresConfigData.schema
        return dataSource
    }
}