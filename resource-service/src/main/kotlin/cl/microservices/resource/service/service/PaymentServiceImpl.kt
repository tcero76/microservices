package cl.microservices.resource.service.service

import cl.microservices.postgres.enums.PaymentStatus
import cl.microservices.postgres.enums.Transaction_Type
import cl.microservices.postgres.model.CreditHistory
import cl.microservices.postgres.model.Payments
import cl.microservices.postgres.services.repo.CreditEntryRepo
import cl.microservices.postgres.services.repo.CreditHistoryRepo
import cl.microservices.postgres.services.repo.PaymentsRepo
import cl.microservices.resource.service.dto.CommandPayment
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
    override fun paymentPersist(commandPayment: CommandPayment):Boolean {
        val totalProducts = commandPayment.items.sumOf { it.subTotal }
        val creditHistoriesOpt = Optional.of(creditHistoryRepo.findByCustomerid(commandPayment.customerId))
        if(creditHistoriesOpt.isEmpty) {
            return false
        }
        val totalHistory = totalHistory(creditHistoriesOpt)
        val creditEntry = creditEntryRepo.findByCustomerid(commandPayment.customerId)
        if (totalHistory != creditEntry.totalcreditamount) {
            return false
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
        if (paymentOpt.isEmpty || paymentOpt.get().price != totalProducts) {
            return false
        }
        return true;
    }

    private fun totalHistory(creditHistoriesOpt: Optional<MutableList<CreditHistory>>): Int {
        val totalHistorialCredit = creditHistoriesOpt.get()
            .filter { it.typecredit == Transaction_Type.CREDIT }
            .sumOf { it.amount }
        val totalHistorialDebit = creditHistoriesOpt.get()
            .filter { it.typecredit == Transaction_Type.DEBIT }
            .sumOf { it.amount }
        val totalHistorial = totalHistorialCredit - totalHistorialDebit
        return totalHistorial
    }
}