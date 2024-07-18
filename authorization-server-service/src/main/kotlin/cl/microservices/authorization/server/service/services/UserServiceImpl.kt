package cl.microservices.authorization.server.service.services

import cl.microservices.postgres.services.services.PostgresUserService
import cl.microservices.authorization.server.service.adapter.PostgresToUserDetail
import cl.microservices.authorization.server.service.model.User
import org.springframework.stereotype.Service
@Service
class UserServiceImpl(val postgresUserService: PostgresUserService):UserService {
    override fun loadUserByUsername(username: String?):User {
        val name:String = username?:""
        return PostgresToUserDetail().CustomerToUserDetail(postgresUserService.loadUserByUsername(name))
    }
}
