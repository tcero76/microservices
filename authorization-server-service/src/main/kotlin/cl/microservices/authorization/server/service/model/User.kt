package cl.microservices.authorization.server.service.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

class User(
    private var username:String,
    private var password:String,
    private var authorities:MutableList<GrantedAuthority>,
    var user_id:UUID):UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }
}