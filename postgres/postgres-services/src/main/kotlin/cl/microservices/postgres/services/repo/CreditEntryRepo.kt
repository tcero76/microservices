package cl.microservices.postgres.services.repo

import cl.microservices.postgres.model.CreditEntry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CreditEntryRepo:JpaRepository<CreditEntry, UUID> {
    fun findByCustomerid(customerid:UUID):CreditEntry
}