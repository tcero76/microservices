package cl.microservices.authorization.server.service.adapter

import cl.microservices.postgres.model.Customer
import cl.microservices.authorization.server.service.model.User
import com.nimbusds.jose.crypto.PasswordBasedDecrypter
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.factory.PasswordEncoderFactories

class PostgresToUserDetail {
    fun CustomerToUserDetail(loadUserByUsername: Customer): User {
        return User(username = loadUserByUsername.username,
            password = loadUserByUsername.password,
            user_id = loadUserByUsername.customer_id,
            authorities = loadUserByUsername.authorities.split(",").map { SimpleGrantedAuthority("ROLE_" + it) }.toMutableList()
        )
    }
}