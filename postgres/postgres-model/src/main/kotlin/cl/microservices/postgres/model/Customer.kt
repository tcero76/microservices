package cl.microservices.postgres.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "user_entity", schema = "public")
data class UserEntity (
    @Id
    var id:String = "",
    @Column(name = "email")
    var email:String = "",
    @Column(name = "email_constraint")
    var email_constraint:String = "",
    @Column(name = "email_verified")
    var email_verified:Boolean = false,
    @Column(name = "enabled")
    var enabled:Boolean = true,
    @Column(name = "federation_link")
    var federation_link:String = "",
    @Column(name = "first_name")
    var first_name:String = "",
    @Column(name = "last_name")
    var last_name:String = "",
    @Column(name = "realm_id")
    var realm_id:String = "",
    @Column(name = "username")
    var username:String = "",
    @Column(name = "created_timestamp")
    var created_timestamp:Long = 0L,
    @Column(name = "service_account_client_link")
    var service_account_client_link:String = "",
    @Column(name = "not_before")
    var not_before:Int = 0) {
}