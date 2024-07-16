package cl.microservices.postgres.services.repo

import cl.microservices.postgres.model.UserEntity
import cl.microservices.postgres.services.repo.UserEntityRepo
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import java.sql.Timestamp
import java.time.Instant

@DataJpaTest
class UserEntityTests(@Autowired val userEntityRepo: UserEntityRepo) {

    @Test
    @DisplayName("JUnit test for findAll UserEntity")
    fun givenUserEntity_whenFindAll_thenUserEntityList() {
        val userEntity1 = UserEntity(
            "1",
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
        val userEntity2 = UserEntity(
            "2",
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

        userEntityRepo.save(userEntity1)
        userEntityRepo.save(userEntity2)

        val userEntityList = userEntityRepo.findAll()

        Assertions.assertThat(userEntityList).isNotNull()
        Assertions.assertThat(userEntityList.size).isEqualTo(2)
    }
}