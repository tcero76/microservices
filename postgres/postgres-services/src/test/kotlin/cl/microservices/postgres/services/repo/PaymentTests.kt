package cl.microservices.postgres.services.repo

import cl.microservices.postgres.enums.PaymentStatus
import cl.microservices.postgres.model.Payments
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.sql.Timestamp
import java.time.Instant
import java.util.*

@DataJpaTest
class PaymentTests (@Autowired val paymentsRepo:PaymentsRepo) {

    @Test
    fun givenPayment_whenFindAll_thenPaymentList() {
        val customer_id =  UUID.randomUUID()
        val payments1 = Payments(UUID.randomUUID(),
            customer_id,
            UUID.randomUUID(),
            100,
            Timestamp.from(Instant.now()),
            PaymentStatus.COMPLETED
        )
        val payments2 = Payments(UUID.randomUUID(),
            customer_id,
            UUID.randomUUID(),
            100,
            Timestamp.from(Instant.now()),
            PaymentStatus.COMPLETED
        )

        paymentsRepo.save(payments1)
        paymentsRepo.save(payments2)
        var payments = paymentsRepo.findAll()

        Assertions.assertThat(payments).isNotNull()
        Assertions.assertThat(payments.toList()?.size).isEqualTo(2)
    }
}
