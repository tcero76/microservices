package cl.microservices.discovery.service

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer


@SpringBootApplication
@EnableEurekaServer
class Main
fun main(args: Array<String>) {
//    System.out.println("Java Runtime Version: " + System.getProperty("java.runtime.version"));
//    System.out.println("Java VM Version: " + System.getProperty("java.vm.version"));
//    SpringApplicationBuilder(Main::class.java).web(WebApplicationType.SERVLET).run(*args)
    runApplication<Main>(*args)
}
