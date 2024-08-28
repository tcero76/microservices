package cl.microservices.authorization.server.service.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController {
    val log = KotlinLogging.logger {  }
    @GetMapping("/isAuthenticated")
    fun isAuthenticated():ResponseEntity<Boolean> {
        val authentication = SecurityContextHolder.getContext().authentication

        if(authentication.principal::class.simpleName == "User") {
            return ResponseEntity.ok(authentication.isAuthenticated)
        }
        return ResponseEntity.ok(false)
    }
}