package com.yunesta.mpesa.LoanRepayments

import com.yunesta.mpesa.loan.Loan
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LoanRepaymentsRepository : JpaRepository<LoanRepayments, Long> {
    fun findByLoan(loan: Loan): LoanRepayments?
    fun findAllByLoan(loan: Loan): List<LoanRepayments>
}