package cl.microservices.postgres.model

import cl.microservices.postgres.enums.PaymentStatus
import jakarta.persistence.*
import java.sql.Timestamp
import java.util.*

@Entity
@Table(name = "payments")
data class Payments constructor (
    @Id var id: UUID? = null,
    @Column(name = "customer_id") var customerid: UUID = UUID.randomUUID(),
    @Column(name = "order_id") var orderid: UUID = UUID.randomUUID(),
    @Column(name = "price") var price:Int = 0,
    @Column(name = "created_at") var createdate:Timestamp? = null,
    @Column(name = "status") @Enumerated(EnumType.STRING) var status:PaymentStatus = PaymentStatus.COMPLETED
)