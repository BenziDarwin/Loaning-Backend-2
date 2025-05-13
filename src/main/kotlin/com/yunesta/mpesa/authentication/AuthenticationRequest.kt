package com.yunesta.mpesa.authentication

import org.springframework.stereotype.Component

@Component
data class AuthenticationRequest(
    var email: String = "",
    var password: String = ""
)
