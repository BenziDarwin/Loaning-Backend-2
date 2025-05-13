package com.yunesta.mpesa.loan

import com.yunesta.mpesa.LoanRepayments.LoanRepayments
import com.yunesta.mpesa.member.Member
import jakarta.persistence.*
import lombok.AllArgsConstructor
import java.math.BigDecimal
import java.time.LocalDate
import lombok.Data
import lombok.NoArgsConstructor
import java.time.ZonedDateTime


@Entity
@Table(name = "loans")
@Data
@AllArgsConstructor
@NoArgsConstructor
data class Loan(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member,

    @Column(nullable = false)
    var phoneNumber: String,

    @Column(nullable = false, precision = 10, scale = 2)
    var amount: BigDecimal,

    @Column(nullable = false)
    var interestPercentage: Double,

    @Column(nullable = false)
    var numberOfDays: Int,

    @Column(nullable = false)
    var collectorCommission: Double,

    @Column(nullable = false)
    var dateBorrowed: ZonedDateTime,

    @Column(nullable = false, precision = 10, scale = 2)
    var totalExpected: BigDecimal,

    @Column(nullable = false, precision = 10, scale = 2)
    var interestExpected: BigDecimal,

    @Column(nullable = false, precision = 10, scale = 2)
    var dailyAmount: BigDecimal,

    @Column(nullable = false)
    var completionDate: ZonedDateTime,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: LoanStatus = LoanStatus.ACTIVE,

    @Column(nullable = false, precision = 10, scale = 2)
    var balance: BigDecimal,

    @Column(nullable = false, precision = 10, scale = 2)
    var totalFines: BigDecimal = BigDecimal.ZERO,

    @OneToMany(mappedBy = "loan", cascade = [CascadeType.ALL], orphanRemoval = true)
    var repayments: MutableList<LoanRepayments> = mutableListOf()
)
