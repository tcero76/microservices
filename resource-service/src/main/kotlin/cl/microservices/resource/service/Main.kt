package cl.microservices.resource.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@ComponentScan(basePackages = ["cl.microservices"])
@EnableJpaRepositories(basePackages = ["cl.microservices.postgres"])
@EntityScan(basePackages = ["cl.microservices.postgres"])
class Main

fun main(args:Array<String>) {
    runApplication<Main>(*args)
}