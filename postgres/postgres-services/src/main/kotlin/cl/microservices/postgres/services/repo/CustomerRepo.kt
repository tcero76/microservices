package cl.microservices.postgres.services.repo

import cl.microservices.postgres.model.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CustomerRepo:JpaRepository<Customer,UUID> {
    fun findByUsername(username: String): Customer
}
