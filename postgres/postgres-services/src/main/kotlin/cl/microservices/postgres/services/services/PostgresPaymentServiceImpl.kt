package cl.microservices.postgres.services.services

import cl.microservices.postgres.enums.PaymentStatus
import cl.microservices.postgres.enums.TransactionType
import cl.microservices.postgres.model.CreditEntry
import cl.microservices.postgres.model.CreditHistory
import cl.microservices.postgres.model.Customer
import cl.microservices.postgres.model.Payments
import cl.microservices.postgres.services.exception.InconsistencyData
import cl.microservices.postgres.services.exception.ResourceNotFoundException
import cl.microservices.postgres.services.repo.CreditEntryRepo
import cl.microservices.postgres.services.repo.CreditHistoryRepo
import cl.microservices.postgres.services.repo.PaymentsRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.time.Instant
import java.util.*

@Service
class PostgresPaymentServiceImpl @Autowired constructor(
    val paymentsRepo: PaymentsRepo,
    val creditHistoryRepo: CreditHistoryRepo,
    val creditEntryRepo: CreditEntryRepo
) : PostgresPaymentService {

    @Transactional
    override fun paymentPersist(price: Int, customerId: UUID, total: Int):Optional<CreditEntry> {
        creditEntryRepo.findAll()
        if (price != total) {
            throw InconsistencyData("No coincide el total ${total}")
        }
        val creditHistoriesOpt = creditHistoryRepo.findByCustomerid(customerId)
        if(creditHistoriesOpt.isEmpty()) {
            throw ResourceNotFoundException("No se encuentra CreditHistory para el cliente con Id: ${customerId}")
        }
        val totalHistory = totalHistory(creditHistoriesOpt)
        val creditEntry = creditEntryRepo.findByCustomerid(customerId)
        if (creditEntry == null) {
            throw ResourceNotFoundException("No se encuentra CreditEntry para el cliente con Id: ${customerId}")
        }
        if (totalHistory != creditEntry.totalcreditamount) {
            throw InconsistencyData("No coincide el crédito total${creditEntry.totalcreditamount} con lo calculado del historial(${totalHistory})")
        }
        if (creditEntry.totalcreditamount.minus(price) < 0) {
            throw InconsistencyData("Se requiere mayor crédito para realizar la compra")
        }
        val paymentOpt = Optional.of(paymentsRepo.save(
            Payments(
                id = UUID.randomUUID(),
                customerid = customerId,
                orderid = UUID.randomUUID(),
                price = price,
                createdate = Timestamp.from(Instant.now()),
                status = PaymentStatus.COMPLETED
            )))
        creditEntry.totalcreditamount = creditEntry.totalcreditamount.minus(paymentOpt.get().price)
        val creditEntrySaved = creditEntryRepo.save(creditEntry)
        val creditHistorySave = CreditHistory(UUID.randomUUID(), customerId, price, TransactionType.DEBIT)
        creditHistoryRepo.save(creditHistorySave)
        return Optional.of(creditEntrySaved);
    }

    override fun getPayments(customerId: UUID): CreditEntry {
        return creditEntryRepo.findByCustomerid(customerId)
    }

    private fun totalHistory(creditHistoriesOpt: MutableList<CreditHistory>): Int {
        val totalHistorialCredit = creditHistoriesOpt
            .filter { it.typecredit == TransactionType.CREDIT }
            .sumOf { it.amount }
        val totalHistorialDebit = creditHistoriesOpt
            .filter { it.typecredit == TransactionType.DEBIT }
            .sumOf { it.amount }
        val totalHistorial = totalHistorialCredit - totalHistorialDebit
        return totalHistorial
    }
}