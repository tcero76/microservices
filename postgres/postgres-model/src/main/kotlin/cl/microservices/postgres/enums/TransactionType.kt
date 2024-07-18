package cl.microservices.postgres.enums

enum class TransactionType(val type:String) {
    DEBIT("DEBIT"),
    CREDIT("CREDIT");
}