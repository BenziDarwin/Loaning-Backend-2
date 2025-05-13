package com.yunesta.mpesa.colletor

import jakarta.persistence.*
import lombok.*
import java.util.Date

@Entity
@Table(name = "collectors")
data class Collector(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

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

    @Column(nullable = true)
    var password: String?
)