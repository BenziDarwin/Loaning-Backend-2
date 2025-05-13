package com.yunesta.mpesa.loan

import com.yunesta.mpesa.LoanRepayments.LoanRepayments
import com.yunesta.mpesa.helpers.MessageNotifications
import com.yunesta.mpesa.member.Member
import com.yunesta.mpesa.member.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.math.RoundingMode

@Service
class LoanService(
    private val loanRepository: LoanRepository,
    private val memberRepository: MemberRepository,
    private val messageNotifications: MessageNotifications
) {
    @Transactional
    fun createLoan(request: Loan): Loan {
        val member = memberRepository.findById(request.member.id!!)
            .orElseThrow { IllegalArgumentException("Member not found") }

        val town = request.member.town ?: throw IllegalArgumentException("Member's town is required")

        validateMemberForLoan(member)

        val loan = calculateAndCreateLoan(request, member)
        val savedLoan = loanRepository.save(loan)

        // Prepare SMS message
        val message ="Hello ${member.firstName} ${member.secondName} , Your loan of ksh ${loan.amount} has been approved. You shall make daily payments of ksh ${loan.dailyAmount} for ${loan.numberOfDays} days. The loan is to be completed by ${loan.completionDate}. Thank you."

        messageNotifications.sendSms(listOf(member.phoneNumber), message)
        return savedLoan
    }


    @Transactional(readOnly = true)
    fun getLoanById(id: Long): Loan {
        return loanRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Loan not found") }
    }

    @Transactional(readOnly = true)
    fun getLoans(): List<Loan> {
        return loanRepository.findAll()
    }

    @Transactional
    fun updateLoan(id: Long, request: Loan): Loan {
        val loan = loanRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Loan not found") }

        validateLoanUpdate(loan)

        updateLoanDetails(loan)
        return loanRepository.save(loan)
    }

    @Transactional
    fun deleteLoan(id: Long) {
        val loan = loanRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Loan not found") }

        validateLoanDeletion(loan)
        loanRepository.delete(loan)
    }

    private fun validateMemberForLoan(member: Member) {
        if (member.status != "ACTIVE") {
            if (member.status == "BLACKLISTED") {
                throw  IllegalStateException("This member has been blacklisted!")
            }
            throw IllegalStateException("Member is not active")
        }

        // Check for existing active loans
        if (loanRepository.existsByMemberAndStatusIn(member, setOf(LoanStatus.ACTIVE, LoanStatus.OVERDUE))) {
            throw IllegalStateException("Member has an active or overdue loan")
        }
    }

    private fun calculateAndCreateLoan(request: Loan, member: Member): Loan {
        val interestAmount = request.amount.multiply(BigDecimal(request.interestPercentage / 100))
        val totalExpected = request.amount.plus(interestAmount)
        val dailyAmount = totalExpected.divide(BigDecimal(request.numberOfDays), 2, RoundingMode.HALF_UP)
        val completionDate = ZonedDateTime.now().plusDays(request.numberOfDays.toLong())

        return Loan(
            member = member,
            phoneNumber = member.phoneNumber,
            amount = request.amount,
            interestPercentage = request.interestPercentage,
            numberOfDays = request.numberOfDays,
            collectorCommission = request.collectorCommission,
            dateBorrowed = ZonedDateTime.now(),
            totalExpected = totalExpected,
            interestExpected = interestAmount,
            dailyAmount = dailyAmount,
            completionDate = completionDate,
            balance = totalExpected,
            id = null,
            status = LoanStatus.ACTIVE,
            totalFines = BigDecimal.ZERO,
            repayments = arrayOf<LoanRepayments>().toMutableList<LoanRepayments>(),
        )
    }

    private fun validateLoanUpdate(loan: Loan) {
        if (loan.status == LoanStatus.COMPLETED || loan.status == LoanStatus.DEFAULTED) {
            throw IllegalStateException("Cannot update completed or defaulted loan")
        }
    }

    @Transactional
     fun changeLoanStatus(ld: Long) {
        var loan = loanRepository.findById(ld).orElseThrow();
        if(loan.status == LoanStatus.ARCHIVED) {
            if(loan.completionDate < ZonedDateTime.now()) {
                loan.status = LoanStatus.OVERDUE
            } else {
                loan.status = LoanStatus.ACTIVE
            }
        } else {
            loan.status = LoanStatus.ARCHIVED
        }
    }

    private fun validateLoanDeletion(loan: Loan) {
        if (loan.status != LoanStatus.ACTIVE || loan.repayments.isNotEmpty()) {
            throw IllegalStateException("Cannot delete loan with repayments or non-active status")
        }
    }

    private fun updateLoanDetails(loan: Loan) {
        loan.amount.let {
            if (loan.repayments.isEmpty()) {
                loan.amount = it
                recalculateLoanDetails(loan)
            }
        }
        loan.interestPercentage.let {
            if (loan.repayments.isEmpty()) {
                loan.interestPercentage = it
                recalculateLoanDetails(loan)
            }
        }
        loan.numberOfDays.let {
            loan.numberOfDays = it
            loan.completionDate = loan.dateBorrowed.plusDays(it.toLong())
            recalculateLoanDetails(loan)
        }
        loan.collectorCommission.let { loan.collectorCommission = it }
        loan.status.let { loan.status = it }
    }

    private fun recalculateLoanDetails(loan: Loan) {
        loan.interestExpected = loan.amount.multiply(BigDecimal(loan.interestPercentage / 100))
        loan.totalExpected = loan.amount.plus(loan.interestExpected)
        loan.dailyAmount = loan.totalExpected.divide(BigDecimal(loan.numberOfDays), 2, RoundingMode.HALF_UP)
        loan.balance = loan.totalExpected.subtract(
            loan.repayments.fold(BigDecimal.ZERO) { acc, repayment -> acc.plus(repayment.amount) }
        )
    }
}