package cl.microservices.postgres.services.repo

import cl.microservices.postgres.model.CreditEntry
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CreditEntryRepo:JpaRepository<CreditEntry, UUID> {
    fun findByCustomerid(customerid:UUID):CreditEntry
    @Lock(LockModeType.WRITE)
    fun save(entity: CreditEntry): CreditEntry
}