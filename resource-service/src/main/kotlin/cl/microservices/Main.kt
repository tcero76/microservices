package cl.microservices

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
//@ComponentScan(basePackages = ["cl.microservices.postgres.config","cl.microservices.resource.service", "cl.microservices.config.data"])
//@EnableJpaRepositories(basePackages = ["cl.microservices.postgres.services"])
//@EntityScan(basePackages = ["cl.microservices.postgres.model", "cl.microservices.postgres.enums"])
class Main
fun main(args:Array<String>) {
    runApplication<Main>(*args)
}
