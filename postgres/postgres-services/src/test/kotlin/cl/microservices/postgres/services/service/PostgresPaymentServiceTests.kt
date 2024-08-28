package cl.microservices.postgres.services.service

import cl.microservices.postgres.enums.PaymentStatus
import cl.microservices.postgres.enums.TransactionType
import cl.microservices.postgres.model.CreditEntry
import cl.microservices.postgres.model.CreditHistory
import cl.microservices.postgres.model.Payments
import cl.microservices.postgres.services.repo.CreditEntryRepo
import cl.microservices.postgres.services.repo.CreditHistoryRepo
import cl.microservices.postgres.services.repo.PaymentsRepo
import cl.microservices.postgres.services.services.PostgresPaymentServiceImpl
import cl.microservices.postgres.services.exception.InconsistencyData
import cl.microservices.postgres.services.exception.ResourceNotFoundException
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.function.Executable
import org.mockito.*
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.never
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import java.sql.Timestamp
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList

@ExtendWith(MockitoExtension::class)
class PostgresPaymentServiceTests {

    @Mock
    var creditEntryRepo: CreditEntryRepo? = null
    @Mock
    var creditHistoryRepo: CreditHistoryRepo? = null
    @Mock
    var paymentsRepo: PaymentsRepo? = null
    @InjectMocks
    lateinit var paymentService: PostgresPaymentServiceImpl

    var customerId:UUID = UUID.randomUUID()
    var payment:Payments = Payments()
    var creditHistory1:CreditHistory = CreditHistory()
    var creditEntry:CreditEntry = CreditEntry()
    var price:Int = 0
    var total:Int = 0

    @BeforeEach
    fun init() {
        customerId = UUID.randomUUID()
        price = 50
        total = 50
        payment = Payments(
            id = UUID.randomUUID(),
            customerid = customerId,
            orderid = UUID.randomUUID(),
            price = price,
            createdate = Timestamp.from(Instant.now()),
            status = PaymentStatus.COMPLETED)
        creditHistory1 = CreditHistory(
            UUID.randomUUID(),
            customerId,
            price,
            TransactionType.CREDIT
        )
        creditEntry = CreditEntry(
            UUID.randomUUID(),
            customerId,
            price
        )
    }


    @Test
    fun givenCommandPayment_whenPersist_thenSave() {
        given(creditHistoryRepo?.findByCustomerid(customerId))
            .willReturn(arrayListOf(creditHistory1))
        given(creditEntryRepo?.findByCustomerid(customerId))
            .willReturn(creditEntry)
        BDDMockito.given<Payments>(paymentsRepo?.save(any<Payments>())).willReturn(payment)
        val paymentPersist = paymentService.paymentPersist(price, customerId, total)
        Assertions.assertThat(paymentPersist).isNotNull()
    }

    @Test
    fun givenCreditHistoryEmpty_whenPersist_thenExcepcion() {
//        given
        given(creditHistoryRepo?.findByCustomerid(customerId)).willReturn(ArrayList<CreditHistory>())
        // when
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException::class.java, Executable {
            paymentService?.paymentPersist(price, customerId, total)
        })
//         then
        Mockito.verify<PaymentsRepo>(paymentsRepo, never())
            .save(ArgumentMatchers.any(Payments::class.java))
    }
    @Test
    fun givenCreditEntryNull_whenPersist_thenExcepcion() {
//        given
        given(creditHistoryRepo?.findByCustomerid(customerId)).willReturn(arrayListOf(creditHistory1))
        given(creditEntryRepo?.findByCustomerid(customerId)).willReturn(null)
        // when
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException::class.java, Executable {
            paymentService?.paymentPersist(price, customerId, total)
        })
//         then
        Mockito.verify<PaymentsRepo>(paymentsRepo, never())
            .save(ArgumentMatchers.any(Payments::class.java))
    }
    @Test
    fun givenCreditHistoryInconsistency_whenPersist_thenExcepcion() {
//        given
        creditEntry.totalcreditamount = creditEntry.totalcreditamount + 1
        given(creditHistoryRepo?.findByCustomerid(customerId)).willReturn(arrayListOf(creditHistory1))
        given(creditEntryRepo?.findByCustomerid(customerId)).willReturn(creditEntry)
        // when
        org.junit.jupiter.api.Assertions.assertThrows(InconsistencyData::class.java, Executable {
            paymentService?.paymentPersist(price, customerId, total)
        })
//         then
        Mockito.verify<PaymentsRepo>(paymentsRepo, never())
            .save(ArgumentMatchers.any(Payments::class.java))
    }

    @Test
    fun givenCreditHistoryExceedAmount_whenPersist_thenException() {
        //given
        total = 600
        price = 600
        given(creditHistoryRepo?.findByCustomerid(customerId)).willReturn(arrayListOf(creditHistory1))
        given(creditEntryRepo?.findByCustomerid(customerId)).willReturn(creditEntry)
        // when
        org.junit.jupiter.api.Assertions.assertThrows(InconsistencyData::class.java, Executable {
            paymentService?.paymentPersist(price, customerId, total)
        })
//         then
        Mockito.verify<PaymentsRepo>(paymentsRepo, never())
            .save(ArgumentMatchers.any(Payments::class.java))

    }
}