package com.yunesta.mpesa.LoanRepayments

import com.yunesta.mpesa.loan.CollectionTransaction
import com.yunesta.mpesa.loan.Loan
import com.yunesta.mpesa.loan.PaymentMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/api/v1/loan-repayments")
class LoanRepaymentsController(
    private val loanRepaymentsService: LoanRepaymentsService
) {

    // Create a new repayment
    @PostMapping
    fun createRepayment(@RequestBody repayment: LoanRepayments): ResponseEntity<LoanRepayments> {
        val createdRepayment = loanRepaymentsService.createRepayment(repayment)
        return ResponseEntity(createdRepayment, HttpStatus.CREATED)
    }

    // Get a repayment by ID
    @GetMapping("/{id}")
    fun getRepaymentById(@PathVariable id: Long): ResponseEntity<LoanRepayments> {
        val repayment = loanRepaymentsService.getRepaymentById(id)
        return repayment?.let {
            ResponseEntity(it, HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    // Get all repayments
    @GetMapping
    fun getAllRepayments(): ResponseEntity<List<LoanRepayments>> {
        val repayments = loanRepaymentsService.getRepayments()
        return ResponseEntity(repayments, HttpStatus.OK)
    }

    // Update a repayment
    @PutMapping("/{id}")
    fun updateRepayment(
        @PathVariable id: Long,
        @RequestParam amount: BigDecimal,
        @RequestParam fine: BigDecimal,
        @RequestParam balance: BigDecimal,
        @RequestParam method: PaymentMethod
    ): ResponseEntity<LoanRepayments> {
        val updatedRepayment = loanRepaymentsService.updateRepayment(id, amount, fine, balance, method)
        return updatedRepayment?.let {
            ResponseEntity(it, HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    // Delete a repayment by ID
    @DeleteMapping("/{id}")
    fun deleteRepayment(@PathVariable id: Long): ResponseEntity<Void> {
        val isDeleted = loanRepaymentsService.deleteRepayment(id)
        return if (isDeleted) {
            ResponseEntity(HttpStatus.NO_CONTENT)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    // Add a fine
    @PostMapping("/fine")
    fun createFine(@RequestBody fine: LoanRepayments): ResponseEntity<LoanRepayments> {
        val loanRepayments = loanRepaymentsService.createFine(fine)
        return ResponseEntity.ok(loanRepayments);
    }

    // Add a transaction to an existing repayment
    @PostMapping("/{repaymentId}/transactions")
    fun addTransactionToRepayment(
        @PathVariable repaymentId: Long,
        @RequestBody transaction: CollectionTransaction
    ): ResponseEntity<LoanRepayments> {
        val updatedRepayment = loanRepaymentsService.addTransactionToRepayment(repaymentId, transaction)
        return updatedRepayment?.let {
            ResponseEntity(it, HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }
}