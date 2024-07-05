package cl.microservices.postgres.services.repo

import cl.microservices.postgres.enums.Transaction_Type
import cl.microservices.postgres.model.CreditEntry
import cl.microservices.postgres.model.CreditHistory
import cl.microservices.postgres.services.repo.CreditHistoryRepo
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
            Transaction_Type.CREDIT)
        val creditHistory2 = CreditHistory(UUID.randomUUID(),
            customerid,
            10,
            Transaction_Type.CREDIT)
        val creditHistory3 = CreditHistory(UUID.randomUUID(),
            customerid,
            20,
            Transaction_Type.CREDIT)

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