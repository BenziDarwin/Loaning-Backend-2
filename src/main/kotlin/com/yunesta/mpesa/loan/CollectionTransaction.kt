package com.yunesta.mpesa.loan

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Entity
@Table(name = "collection_transactions")
data class CollectionTransaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @Column(nullable = false)
    var time: ZonedDateTime,

    @Column(nullable = false)
    var txnID: String,

    @Column(nullable = false, precision = 10, scale = 2)
    var amount: BigDecimal,

    @Column(nullable = false)
    var balance: BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var method: PaymentMethod,
)