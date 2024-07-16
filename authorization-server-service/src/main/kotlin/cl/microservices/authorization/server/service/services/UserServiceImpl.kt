package cl.sugarfever.authorization.server.service.services

import cl.microservices.postgres.services.services.PostgresUserService
import cl.sugarfever.authorization.server.service.adapter.PostgresToUserDetail
import cl.sugarfever.authorization.server.service.model.User
import org.springframework.stereotype.Service
@Service
class UserServiceImpl(val postgresUserService: PostgresUserService):UserService {
    override fun loadUserByUsername(username: String?):User {
        val name:String = username?:""
        return PostgresToUserDetail().CustomerToUserDetail(postgresUserService.loadUserByUsername(name))
    }
}
