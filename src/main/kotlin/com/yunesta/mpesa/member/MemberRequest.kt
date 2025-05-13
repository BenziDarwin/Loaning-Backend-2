package com.yunesta.mpesa.member

import java.util.*

data class MemberRequest(
    val firstName: String,
    val secondName: String,
    val thirdName: String,
    val dateOfBirth: Date,
    val gender: String,
    val townId: Long,
    val collectorId: Long?,
    val email: String?,
    val phoneNumber: String,
    val profilePicture: String,
    val idFrontImage: String,
    val idBackImage: String,
    val status: String,
    val grossLent: Double,
    val netProfit: Double,
    val totalCycle: Int,
    val clientValue: Int
)
