package org.example.cl.microservices.security.config

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import java.util.stream.Collectors

class AuthServerRoleConverter : Converter<Jwt, Collection<GrantedAuthority>> {

    override fun convert(jwt: Jwt): List<SimpleGrantedAuthority> {
        val realmAccess = jwt.claims.get("roles") as Collection<String>
        return (jwt.claims.get("roles")  as ArrayList<String>).stream()
            .map(::SimpleGrantedAuthority)
            .collect(Collectors.toList())
    }

}