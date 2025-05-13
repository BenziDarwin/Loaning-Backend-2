package com.yunesta.mpesa.authentication

import com.yunesta.mpesa.roles.Role

data class AuthenticationResponse(
    val id: Long? = null,
    val token: String? = null,
    val name: String? = null,
    val email: String? = null,
    val roles: Set<Role>? = null
)
