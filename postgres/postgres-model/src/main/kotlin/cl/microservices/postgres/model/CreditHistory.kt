package cl.microservices.postgres.model

import cl.microservices.postgres.enums.Transaction_Type
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "credit_history")
data class CreditHistory constructor (
    @Id var id: UUID = UUID.randomUUID(),
    @Column(name = "customer_id") var customerid: UUID = UUID.randomUUID(),
    @Column(name = "amount") var amount:Int = 0,
    @Column(name = "type") @Enumerated(EnumType.STRING) var typecredit:Transaction_Type? = null
) {
}