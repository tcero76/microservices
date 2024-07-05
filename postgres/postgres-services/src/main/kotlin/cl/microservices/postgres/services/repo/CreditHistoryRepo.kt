package cl.microservices.postgres.services.repo

import cl.microservices.postgres.model.CreditHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CreditHistoryRepo:JpaRepository<CreditHistory, UUID> {
    fun findByCustomerid(customerid:UUID):MutableList<CreditHistory>;
}