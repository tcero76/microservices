package cl.microservices.resource.service.service

import cl.microservices.postgres.enums.PaymentStatus
import cl.microservices.resource.service.dto.CommandPayment
import cl.microservices.resource.service.dto.OrderItemDto
import cl.microservices.postgres.enums.Transaction_Type
import cl.microservices.postgres.model.CreditEntry
import cl.microservices.postgres.model.CreditHistory
import cl.microservices.postgres.model.Payments
import cl.microservices.postgres.services.repo.CreditEntryRepo
import cl.microservices.postgres.services.repo.CreditHistoryRepo
import cl.microservices.postgres.services.repo.PaymentsRepo
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.sql.Timestamp
import java.time.Instant
import java.util.*

@ExtendWith(MockitoExtension::class)
class PaymentServiceTests {

    @Mock
    var creditEntryRepo: CreditEntryRepo? = null
    @Mock
    var creditHistoryRepo: CreditHistoryRepo? = null
    @Mock
    var paymentsRepo: PaymentsRepo? = null
    @InjectMocks
    var paymentService: PaymentServiceImpl? = null

    var customerid1:UUID = UUID.randomUUID()
    var commandPayment: CommandPayment = CommandPayment()
    var payment:Payments = Payments()
    var creditHistory1:CreditHistory = CreditHistory()
    var creditEntity1:CreditEntry = CreditEntry()

    @BeforeEach
    fun init() {
        customerid1 = UUID.randomUUID()
        commandPayment = CommandPayment(customerid1
            ,500,
            arrayListOf(
                OrderItemDto(UUID.randomUUID().toString(), 10, 20,200),
                OrderItemDto(UUID.randomUUID().toString(), 30, 10, 300)
            )
        )
        payment = Payments(
            id = null,
            customerid = commandPayment.customerId,
            orderid = null,
            price = commandPayment.price,
            createdate = null,
            status = PaymentStatus.COMPLETED)
        creditHistory1 = CreditHistory(
            UUID.randomUUID(),
            customerid1,
            commandPayment.price,
            Transaction_Type.CREDIT
        )
        creditEntity1 = CreditEntry(
            UUID.randomUUID(),
            customerid1,
            commandPayment.price
        )
    }


    @Test
    fun givenCommandPayment_whenPersist_thenSave() {
        given(creditHistoryRepo?.findByCustomerid(customerid1))
            .willReturn(arrayListOf(creditHistory1))
        given(creditEntryRepo?.findByCustomerid(customerid1))
            .willReturn(creditEntity1)
        given(paymentsRepo?.save(payment))
            .willReturn(Payments(
                id = UUID.randomUUID(),
                customerid = commandPayment.customerId,
                orderid = UUID.randomUUID(),
                price = commandPayment.price,
                createdate = Timestamp.from(Instant.now()),
                status = PaymentStatus.COMPLETED))

        val paymentPersist = paymentService?.paymentPersist(commandPayment)

        Assertions.assertThat(paymentPersist).isTrue()
    }

    @Test
    fun givenCommandPayment_whenPersist_thenExcepcion() {
        var commandPaymentError = CommandPayment(customerid1
            ,400,
            arrayListOf(
                OrderItemDto(UUID.randomUUID().toString(), 10, 20,200),
                OrderItemDto(UUID.randomUUID().toString(), 30, 10, 300)
            )
        )
        var paymentError = Payments(
            id = null,
            customerid = commandPaymentError.customerId,
            orderid = UUID.randomUUID(),
            price = commandPaymentError.price,
            createdate = null,
            status = PaymentStatus.COMPLETED)
        given(creditHistoryRepo?.findByCustomerid(customerid1))
            .willReturn(arrayListOf(creditHistory1))
        given(creditEntryRepo?.findByCustomerid(customerid1))
            .willReturn(creditEntity1)
        given(paymentsRepo?.save(paymentError))
            .willReturn(Payments(
                id = UUID.randomUUID(),
                customerid = commandPaymentError.customerId,
                orderid = UUID.randomUUID(),
                price = commandPaymentError.price,
                createdate = Timestamp.from(Instant.now()),
                status = PaymentStatus.COMPLETED))

        val paymentPersist = paymentService?.paymentPersist(commandPaymentError)

        Assertions.assertThat(paymentPersist).isFalse()
    }
}