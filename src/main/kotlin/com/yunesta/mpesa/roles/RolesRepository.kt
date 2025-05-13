package com.yunesta.mpesa.roles

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface RolesRepository:JpaRepository<Role, Long> {
    fun findByName(name: String): Optional<Role>
    fun existsByName(name: String): Boolean
}