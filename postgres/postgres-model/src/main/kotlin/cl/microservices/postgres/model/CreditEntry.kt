package cl.microservices.postgres.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "credit_entry")
data class CreditEntry (
    @Id val id: UUID? = UUID.randomUUID(),
    @Column(name = "customer_id") val customerid: UUID? = UUID.randomUUID(),
    @Column(name = "total_credit_amount") var totalcreditamount:Int = 0
)