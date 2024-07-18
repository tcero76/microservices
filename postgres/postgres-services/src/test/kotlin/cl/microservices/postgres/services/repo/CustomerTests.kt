package cl.microservices.postgres.services.repo

import cl.microservices.postgres.model.Customer
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.sql.Timestamp
import java.time.Instant
import java.util.UUID

@DataJpaTest
class CustomerTests(@Autowired val userEntityRepo: UserEntityRepo) {

    @Test
    @DisplayName("JUnit test for findAll UserEntity")
    fun givenUserEntity_whenFindAll_thenUserEntityList() {
        val customer1 = Customer(
            UUID.randomUUID(),
            "leolastra@hotmail.com",
            "none",
            true,
            true,
            "",
            "Leonardo",
            "Lastra",
            "123",
            "app_user",
            Timestamp.from(
                Instant.now()
            ).time,
            "",
            0
        )
        val customer2 = Customer(
            UUID.randomUUID(),
            "leonardo.lastra@gmail.com",
            "none",
            true,
            true,
            "",
            "Leonardo",
            "Lastra",
            "123",
            "admin",
            Timestamp.from(
                Instant.now()
            ).time,
            "",
            0
        )

        userEntityRepo.save(customer1)
        userEntityRepo.save(customer2)

        val userEntityList = userEntityRepo.findAll()

        Assertions.assertThat(userEntityList).isNotNull()
        Assertions.assertThat(userEntityList.size).isEqualTo(2)
    }
}