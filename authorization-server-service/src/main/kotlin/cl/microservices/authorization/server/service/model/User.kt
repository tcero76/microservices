package cl.sugarfever.authorization.server.service.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class User(
    private var username:String,
    private var password:String,
    private var authorities:MutableList<GrantedAuthority>):UserDetails {
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