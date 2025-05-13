package com.yunesta.mpesa.permissions

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface PermissionsRepository: JpaRepository<Permission, Long> {
    fun findByName(name: String): Optional<Permission>
}