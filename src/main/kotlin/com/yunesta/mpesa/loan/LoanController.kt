package com.yunesta.mpesa.loan

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/loans")
class LoanController(private val loanService: LoanService) {
    @PostMapping
    fun createLoan(@RequestBody request: Loan): ResponseEntity<Loan> {
        return ResponseEntity.ok(loanService.createLoan(request))
    }

    @GetMapping("/{id}")
    fun getLoan(@PathVariable id: Long): ResponseEntity<Loan> {
        return ResponseEntity.ok(loanService.getLoanById(id))
    }

    @GetMapping
    fun getLoans(): ResponseEntity<List<Loan>> {
        return ResponseEntity.ok(loanService.getLoans());
    }

    @PutMapping("/archive/{id}")
    fun changeStatus(@PathVariable("id") id: Long):ResponseEntity<Void> {
        loanService.changeLoanStatus(id);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/{id}")
    fun updateLoan(
        @PathVariable id: Long,
        @RequestBody request: Loan
    ): ResponseEntity<Loan> {
        return ResponseEntity.ok(loanService.updateLoan(id, request))
    }

    @DeleteMapping("/{id}")
    fun deleteLoan(@PathVariable id: Long): ResponseEntity<Unit> {
        loanService.deleteLoan(id)
        return ResponseEntity.noContent().build()
    }
}