package com.yunesta.mpesa.town

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TownRepository: JpaRepository<Town, Long> {
}