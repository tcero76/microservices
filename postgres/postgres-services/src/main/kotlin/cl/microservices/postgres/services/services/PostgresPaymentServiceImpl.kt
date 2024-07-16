package cl.microservices.postgres.services.services

import cl.microservices.postgres.enums.PaymentStatus
import cl.microservices.postgres.enums.TransactionType
import cl.microservices.postgres.model.CreditHistory
import cl.microservices.postgres.model.Payments
import cl.microservices.postgres.services.exception.InconsistencyData
import cl.microservices.postgres.services.exception.ResourceNotFoundException
import cl.microservices.postgres.services.repo.CreditEntryRepo
import cl.microservices.postgres.services.repo.CreditHistoryRepo
import cl.microservices.postgres.services.repo.PaymentsRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.time.Instant
import java.util.*

@Service
class PaymentServiceImpl @Autowired constructor(
    val paymentsRepo: PaymentsRepo,
    val creditHistoryRepo: CreditHistoryRepo,
    val creditEntryRepo: CreditEntryRepo
) : PaymentService {

    @Transactional
    override fun paymentPersist(price:Int, customer_id:UUID,  total:Int):Optional<Payments> {
        val total = commandPayment.items.sumOf { it.subTotal }
        if (commandPayment.price != total) {
            throw InconsistencyData("No coincide el tatal ${total}")
        }
        val creditHistoriesOpt = creditHistoryRepo.findByCustomerid(commandPayment.customerId)
        if(creditHistoriesOpt.isEmpty()) {
            throw ResourceNotFoundException("No se encuentra CreditHistory para el cliente con Id: ${commandPayment.customerId}")
        }
        val totalHistory = totalHistory(creditHistoriesOpt)
        val creditEntry = creditEntryRepo.findByCustomerid(commandPayment.customerId)
        if (creditEntry == null) {
            throw ResourceNotFoundException("No se encuentra CreditEntry para el cliente con Id: ${commandPayment.customerId}")
        }
        if (totalHistory != creditEntry.totalcreditamount) {
            throw InconsistencyData("No coincide el crédito total${creditEntry.totalcreditamount} con lo calculado del historial(${totalHistory})")
        }
        val paymentOpt = Optional.of(paymentsRepo.save(
            Payments(
                id = UUID.randomUUID(),
                customerid = commandPayment.customerId,
                orderid = UUID.randomUUID(),
                price = commandPayment.price,
                createdate = Timestamp.from(Instant.now()),
                status = PaymentStatus.COMPLETED
            )))
        val creditEntrySave = creditEntry
        creditEntrySave.totalcreditamount.minus(paymentOpt.get().price)
        creditEntryRepo.save(creditEntrySave)
        val creditHistorySave = CreditHistory(UUID.randomUUID(), commandPayment.customerId, commandPayment.price, TransactionType.DEBIT)
        creditHistoryRepo.save(creditHistorySave)
        return paymentOpt;
    }

    override fun getPayments(s: String): PaymentsResponse {
//        creditEntryRepo.findByCustomerid(s)
        TODO("Not yet implemented")
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