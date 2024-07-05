package cl.microservices.config.server.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.config.server.EnableConfigServer

@SpringBootApplication
@EnableConfigServer
class Main

fun main(args:Array<String>) {
    runApplication<Main>(*args)
}