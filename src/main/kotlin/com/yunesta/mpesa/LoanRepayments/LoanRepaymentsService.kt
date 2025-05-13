package com.yunesta.mpesa.LoanRepayments

import com.yunesta.mpesa.helpers.MessageNotifications
import com.yunesta.mpesa.loan.CollectionTransaction
import com.yunesta.mpesa.loan.Loan
import com.yunesta.mpesa.loan.LoanRepository
import com.yunesta.mpesa.loan.PaymentMethod
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Service
class LoanRepaymentsService(
    private val loanRepaymentRepository: LoanRepaymentsRepository,
    private val loansRepository: LoanRepository,
    private val messageNotifications: MessageNotifications
) {

    // Create a new repayment with the first transaction
    @Transactional
    fun createRepayment(repayment: LoanRepayments): LoanRepayments {
        // Check if a repayment already exists for the loan
        val existingRepayment = loanRepaymentRepository.findByLoan(repayment.loan)
        val transactionAmount = repayment.transactions[0].amount

        return if (existingRepayment != null) {
            // Ensure the new balance does not go negative
            val newBalance = existingRepayment.balance.subtract(transactionAmount)
            if (newBalance < BigDecimal.ZERO) {
                throw IllegalArgumentException("Repayment amount exceeds the outstanding balance.")
            }

            // Update repayment details
            repayment.transactions[0].id = null

            // Set the balance on the new transaction
            repayment.transactions[0].balance = newBalance

            // Add the new transaction to existing repayment
            existingRepayment.transactions.add(repayment.transactions[0])

            // Update the total amount paid (amount should include both previous payments and current payment)
            existingRepayment.amount = existingRepayment.amount.add(transactionAmount)

            // Update balance
            existingRepayment.balance = newBalance
            existingRepayment.loan.balance = newBalance

            // Save the updated loan and repayment records
            val message = "Hello ${existingRepayment.loan.member.firstName} ${existingRepayment.loan.member.secondName}, You have made a payment of Ksh $transactionAmount towards clearing your loan leaving a balance of ksh ${newBalance}. Thank you."
            messageNotifications.sendSms(listOf(existingRepayment.loan.member.phoneNumber), message)
            loansRepository.save(existingRepayment.loan)
            loanRepaymentRepository.save(existingRepayment)
        } else {
            // Ensure the new repayment does not exceed the loan balance
            if (repayment.loan.balance.subtract(transactionAmount) < BigDecimal.ZERO) {
                throw IllegalArgumentException("Repayment amount exceeds the outstanding balance.")
            }

            // Calculate the new balance
            val newBalance = repayment.loan.balance.subtract(transactionAmount)

            // Set the balance on the transaction
            repayment.transactions[0].balance = newBalance

            // Create a new repayment record
            val newRepayment = LoanRepayments(
                id = null,
                loan = repayment.loan,
                date = ZonedDateTime.now(),
                amount = transactionAmount, // The first transaction amount paid
                fine = BigDecimal.ZERO,
                balance = newBalance, // Subtract transaction amount from loan balance
                transactions = mutableListOf(repayment.transactions[0])
            )

            // Update loan balance
            repayment.loan.balance = newBalance
            loansRepository.save(repayment.loan)

            val message = "Hello ${repayment.loan.member.firstName} ${repayment.loan.member.secondName}, You have made a payment of Ksh $transactionAmount towards clearing your loan leaving a balance of ksh ${newBalance}. Thank you."
            messageNotifications.sendSms(listOf(repayment.loan.member.phoneNumber), message)

            // Save the new repayment record
            loanRepaymentRepository.save(newRepayment)
        }
    }
    // Read a repayment by ID
    fun getRepaymentById(id: Long): LoanRepayments? {
        return loanRepaymentRepository.findById(id).orElse(null)
    }

    // Read all repayments for a specific loan
    fun getRepaymentsByLoan(loan: Loan): List<LoanRepayments> {
        return loanRepaymentRepository.findAllByLoan(loan)
    }

    // Read all repayments
    fun getRepayments(): List<LoanRepayments> {
        return loanRepaymentRepository.findAll()
    }

    // Update a repayment
    @Transactional
    fun updateRepayment(id: Long, amount: BigDecimal, fine: BigDecimal, balance: BigDecimal, method: PaymentMethod): LoanRepayments? {
        val repayment = loanRepaymentRepository.findById(id).orElse(null) ?: return null
        repayment.amount = amount
        repayment.fine = fine
        repayment.balance = balance
        return loanRepaymentRepository.save(repayment)
    }

    // Delete a repayment by ID
    @Transactional
    fun deleteRepayment(id: Long): Boolean {
        return if (loanRepaymentRepository.existsById(id)) {
            loanRepaymentRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    // Add a transaction to an existing repayment
    @Transactional
    fun addTransactionToRepayment(repaymentId: Long, transaction: CollectionTransaction): LoanRepayments? {
        val repayment = loanRepaymentRepository.findById(repaymentId).orElse(null) ?: return null
        repayment.transactions.add(transaction)
        repayment.balance = repayment.balance.subtract(transaction.amount)
        return loanRepaymentRepository.save(repayment)
    }

    @Transactional
    fun createFine(fine: LoanRepayments): LoanRepayments {
        val nairobiZone = ZoneId.of("Africa/Nairobi")
        // Find the loan first
        val existingRepayment = loanRepaymentRepository.findByLoan(fine.loan)
        val fineAmount = fine.transactions[0].amount

        return if (existingRepayment != null) {
            // Calculate the new balance after adding the fine
            val newBalance = existingRepayment.balance.add(fineAmount)

            // Add the fine to the existing repayment
            val fineTransaction = CollectionTransaction(
                id = null,
                amount = fineAmount,
                time = fine.transactions[0].time.withZoneSameLocal(nairobiZone),
                method = PaymentMethod.FINE,
                txnID = "Fine added on ${fine.date.format(DateTimeFormatter.ISO_LOCAL_DATE)}",
                balance = newBalance
            )

            existingRepayment.transactions.add(fineTransaction)

            // Update the total fine amount
            existingRepayment.fine = existingRepayment.fine.add(fineAmount)

            // Update the balance - adding a fine increases the balance
            existingRepayment.balance = newBalance

            // Update loan balance
            existingRepayment.loan.balance = newBalance

            // Send notification
            val message = "Hello ${existingRepayment.loan.member.firstName} ${existingRepayment.loan.member.secondName}, you have been fined Ksh ${fineAmount} and your loan now stands at ${newBalance}. Kindly endeavor to clear your loan."
            messageNotifications.sendSms(listOf(existingRepayment.loan.member.phoneNumber), message)

            loanRepaymentRepository.save(existingRepayment)
        } else {
            // Calculate the new balance
            val newBalance = fine.loan.balance.add(fineAmount)

            // Create a new repayment record with the fine
            val fineTransaction = CollectionTransaction(
                id = null,
                amount = fineAmount,
                time = fine.transactions[0].time.withZoneSameLocal(nairobiZone),
                method = PaymentMethod.FINE,
                txnID = "Fine added on ${fine.date.format(DateTimeFormatter.ISO_LOCAL_DATE)}",
                balance = newBalance
            )

            val newRepayment = LoanRepayments(
                id = null,
                loan = fine.loan,
                date = ZonedDateTime.now(),
                amount = BigDecimal.ZERO, // No repayment amount, just fine
                fine = fineAmount, // The fine amount
                balance = newBalance, // Add fine amount to loan balance
                transactions = mutableListOf(fineTransaction)
            )

            // Update loan balance
            fine.loan.balance = newBalance
            loansRepository.save(fine.loan)

            // Send notification
            val message = "Hello ${newRepayment.loan.member.firstName} ${newRepayment.loan.member.secondName}, you have been fined Ksh ${fineAmount} and your loan now stands at ${newBalance}. Kindly endeavor to clear your loan."
            messageNotifications.sendSms(listOf(newRepayment.loan.member.phoneNumber), message)

            // Save the new repayment record
            loanRepaymentRepository.save(newRepayment)
        }
    }
}