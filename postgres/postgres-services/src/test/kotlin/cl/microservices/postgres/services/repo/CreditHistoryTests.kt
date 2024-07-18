package cl.microservices.postgres.services.repo

import cl.microservices.postgres.enums.TransactionType
import cl.microservices.postgres.model.CreditHistory
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.util.*

@DataJpaTest
class CreditHistoryTests(@Autowired private val creditHistoryRepo: CreditHistoryRepo) {

    @Test
    fun givenCreditHistory_whenSaveAll_thenFindByCustomer(){
        //given
        val customerid = UUID.randomUUID()
        val creditHistory1 = CreditHistory(UUID.randomUUID(),
            customerid,
            100,
            TransactionType.CREDIT)
        val creditHistory2 = CreditHistory(UUID.randomUUID(),
            customerid,
            10,
            TransactionType.CREDIT)
        val creditHistory3 = CreditHistory(UUID.randomUUID(),
            customerid,
            20,
            TransactionType.CREDIT)

        //when
        creditHistoryRepo.saveAll(arrayListOf(creditHistory1, creditHistory2, creditHistory3))
        val creditHistories = creditHistoryRepo.findByCustomerid(customerid)

        //then
        Assertions.assertThat(creditHistories).isNotNull
        Assertions.assertThat(
            creditHistories
                .map { it.amount }
                .sum()
                ).isEqualTo(130)
    }
}