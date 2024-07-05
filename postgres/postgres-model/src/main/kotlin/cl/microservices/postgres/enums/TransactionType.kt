package cl.microservices.postgres.enums

enum class Transaction_Type(val type:String) {
    DEBIT("DEBIT"),
    CREDIT("CREDIT");
}