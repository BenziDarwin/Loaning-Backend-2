package com.yunesta.mpesa.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository: JpaRepository<User, Long>  {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    fun findByEmail(email: String?): Optional<User>
}