package cl.sugarfever.authorization.server.service.adapter

import cl.microservices.postgres.model.Customer
import cl.sugarfever.authorization.server.service.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class PostgresToUserDetail {
    fun CustomerToUserDetail(loadUserByUsername: Customer): User {
        return User(username = loadUserByUsername.username,
            password = loadUserByUsername.password,
            authorities = loadUserByUsername.authorities.split(",").map { SimpleGrantedAuthority(it) }.toMutableList()
        )
    }
}