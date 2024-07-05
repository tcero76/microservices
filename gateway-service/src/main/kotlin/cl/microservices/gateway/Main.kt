package cl.microservices.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["cl.microservices"])
class Main

fun main(args:Array<String>) {
    runApplication<Main>(*args)
}