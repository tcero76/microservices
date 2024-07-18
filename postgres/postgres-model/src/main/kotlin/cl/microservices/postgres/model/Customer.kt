package cl.microservices.postgres.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "customer", schema = "payment")
data class Customer (
    @Id
    var customer_id:UUID = UUID.randomUUID(),
    @Column(name = "username")
    var username:String = "",
    @Column(name = "password")
    var password:String = "",
    @Column(name = "authorities")
    var authorities:String = "")