package com.yunesta.mpesa.member

import com.fasterxml.jackson.annotation.JsonIgnore
import com.yunesta.mpesa.colletor.Collector
import com.yunesta.mpesa.loan.Loan
import com.yunesta.mpesa.town.Town
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "members")
data class Member(
    @Id
    @Column(length = 20)
    var id: String = "YT" + UUID.randomUUID().toString().substring(0, 8).uppercase(),

    @Column(nullable = false)
    var firstName: String,

    @Column(nullable = false)
    var secondName: String,

    @Column(nullable = false)
    var thirdName: String,

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    var dateOfBirth: Date,

    @Column(nullable = false)
    var gender: String,

    @ManyToOne
    @JoinColumn(name = "town_id", nullable = false)
    var town: Town,

    @ManyToOne
    @JoinColumn(name = "collector_id")
    var collector: Collector?,

    @Column(nullable = true)
    var email: String?,

    @Column(nullable = false)
    var phoneNumber: String,

    @Column(nullable = false)
    var profilePicture: String,

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    var enrollmentDate: Date = Date(),

    @Column(nullable = false)
    var idFrontImage: String,

    @Column(nullable = false)
    var idBackImage: String,

    @Column(nullable = false)
    var status: String,

    @Column(nullable = false)
    var grossLent: Double,

    @Column(nullable = false)
    var netProfit: Double,

    @Column(nullable = false, unique = true)
    var nin: String,

    @Column(nullable = false)
    var totalCycle: Int,

    @Column(nullable = false)
    var clientValue: Int,

    @OneToMany(mappedBy = "member", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    var loans: MutableList<Loan> = mutableListOf()
)