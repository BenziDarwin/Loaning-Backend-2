package com.yunesta.mpesa.loan

import com.yunesta.mpesa.member.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

// Assuming Loan and LoanStatus classes are defined somewhere
@Repository
interface LoanRepository : JpaRepository<Loan, Long> {
    fun existsByMemberAndStatusIn(member: Member, statusSet: Set<LoanStatus>): Boolean
}
