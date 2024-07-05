package cl.microservices.postgres.services.repo

import cl.microservices.postgres.model.CreditEntry
import cl.microservices.postgres.services.repo.CreditEntryRepo
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.util.UUID

@DataJpaTest
class CreditEntriesTests (@Autowired private val creditEntryRepo: CreditEntryRepo) {

    @Test
    fun givenCreditEntry_whenUpdate_thenCreditEntryUpdated() {
        val customerid = UUID.randomUUID()
        val id = UUID.randomUUID()
        val creditEntry1 = CreditEntry(
            id,
            customerid,
            500
        )
        creditEntryRepo.save(creditEntry1)
        val findById = creditEntryRepo.findById(id).get()
        findById.totalcreditamount=400
        val creditEntryUpdated = creditEntryRepo.save(findById)
        Assertions.assertThat(creditEntryUpdated.totalcreditamount).isEqualTo(400);
    }

    @Test
    fun givenCreditEntry_whenFindByCustomerid_thenCreditEntry() {
        val customerid = UUID.randomUUID()
        val id = UUID.randomUUID()
        val creditEntry1 = CreditEntry(
            id,
            customerid,
            500
        )
        creditEntryRepo.save(creditEntry1)
        val (id1, customer_id, total_credit_amount) = creditEntryRepo.findByCustomerid(customerid)!!

        Assertions.assertThat(id1).isEqualTo(creditEntry1.id)
        Assertions.assertThat(customer_id).isEqualTo(creditEntry1.customerid)
        Assertions.assertThat(total_credit_amount).isEqualTo(creditEntry1.totalcreditamount)

    }
}