package com.yunesta.mpesa.collector

import com.yunesta.mpesa.colletor.Collector
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CollectorRepository: JpaRepository<Collector, Long> {
}