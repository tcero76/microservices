package cl.microservices.postgres.services.repo

import cl.microservices.postgres.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserEntityRepo : JpaRepository<UserEntity,String> {

}