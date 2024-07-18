package cl.microservices.postgres.services.services

import cl.microservices.postgres.model.Customer
import cl.microservices.postgres.services.repo.CustomerRepo
import org.springframework.stereotype.Service

@Service
class PostgresUserService(val customerRepo:CustomerRepo) {
    fun loadUserByUsername(username: String):Customer {
        return customerRepo.findByUsername(username)
    }
}