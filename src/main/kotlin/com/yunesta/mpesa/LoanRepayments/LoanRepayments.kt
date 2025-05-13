package com.yunesta.mpesa.LoanRepayments

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.yunesta.mpesa.loan.CollectionTransaction
import com.yunesta.mpesa.loan.Loan
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.math.BigDecimal
import java.time.ZonedDateTime

@Entity
@Table(name = "loan_repayments")
@Data
@AllArgsConstructor
@NoArgsConstructor
data class LoanRepayments(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_id", nullable = false)
    @JsonIgnoreProperties("repayments")
    var loan: Loan,

    @Column(nullable = false)
    var date: ZonedDateTime,

    @Column(nullable = false, precision = 10, scale = 2)
    var amount: BigDecimal,

    @Column(nullable = false, precision = 10, scale = 2)
    var fine: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false, precision = 10, scale = 2)
    var balance: BigDecimal,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "repayment_id")
    var transactions: MutableList<CollectionTransaction> = mutableListOf(),
)
