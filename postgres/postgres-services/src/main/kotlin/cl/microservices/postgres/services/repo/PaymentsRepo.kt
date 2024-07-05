package cl.microservices.postgres.services.repo

import cl.microservices.postgres.model.Payments
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PaymentsRepo: JpaRepository<Payments, UUID> {
    fun findByCustomerid(customer_id: UUID): List<Payments>
}