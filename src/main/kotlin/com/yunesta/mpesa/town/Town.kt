package com.yunesta.mpesa.town

import com.yunesta.mpesa.colletor.Collector
import jakarta.persistence.*
import lombok.*
import java.util.Date

@Entity
@Table(name = "towns")
data class Town(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = true)
    var nickname: String?,

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    var enrollmentDate: Date = Date(),

    @ManyToOne
    @JoinColumn(name = "collector_id", nullable = false)
    var collector: Collector,

    @Column(nullable = false)
    var loanPortfolio: Double,

    @Column(nullable = true)
    var profilePicture: String?
)

