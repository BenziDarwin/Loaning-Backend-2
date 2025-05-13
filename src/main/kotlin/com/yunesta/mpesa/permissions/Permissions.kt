package com.yunesta.mpesa.permissions

import jakarta.persistence.*
import lombok.*

@Entity
@Table(name = "permissions")
@Data
data class Permission(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    @Column(nullable = false, unique = true)
    val name: String
)
